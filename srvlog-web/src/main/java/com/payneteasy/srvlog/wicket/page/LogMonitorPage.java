package com.payneteasy.srvlog.wicket.page;

import com.payneteasy.srvlog.data.*;
import com.payneteasy.srvlog.service.ILogCollector;
import com.payneteasy.srvlog.service.IndexerServiceException;
import com.payneteasy.srvlog.util.DateRange;
import com.payneteasy.srvlog.util.DateRangeType;
import static com.payneteasy.srvlog.util.DateRangeType.EXACTLY_TIME;
import com.payneteasy.srvlog.wicket.component.ButtonGroupPanel;
import com.payneteasy.srvlog.wicket.component.daterange.DateRangePanel;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import com.payneteasy.srvlog.wicket.component.navigation.PageableDataProvider;
import com.payneteasy.srvlog.wicket.component.navigation.UncountablyPageableListView;
import com.payneteasy.srvlog.wicket.component.navigation.UncountablyPageableNavigator;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.extensions.yui.calendar.DateTimeField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.*;

import java.io.Serializable;
import java.util.*;

import static com.payneteasy.srvlog.utils.LogDataTableUtil.setHighlightCssClassBySeverity;
import com.payneteasy.srvlog.wicket.component.daterange.DateRangePanel.DateRangeModel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.joda.time.DateTime;

/**
 * Date: 11.01.13
 */
public class LogMonitorPage extends BasePage {

    /**
     * Parameters key for start time in date range filter.
     */
    public static final String TIME_FROM = "TIME_FROM";

    /**
     * Parameters key for filter pattern.
     */
    public static final String PATTERN = "PATTERN";

    /**
     * Parser for date and time.
     */
    private final SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public LogMonitorPage(PageParameters pageParameters) {
        super(pageParameters, LogMonitorPage.class);

        FeedbackPanel feedbackPanel = new FeedbackPanel("feedback-panel");
        add(feedbackPanel);

        final FilterModel filterModel = new FilterModel();

        if(!pageParameters.isEmpty()){
            fillFilterModel(filterModel, pageParameters);
        }

        final Form<FilterModel> form = new Form<FilterModel>("form", Model.of(filterModel));
        add(form);


        form.add(new TextField<String>("pattern", new PropertyModel<String>(filterModel, "pattern")));

//        DATE RANGE FILTER
        DateRangePanel dateRangePanel = new DateRangePanel("date-range", filterModel.getDateRangeModel());
        form.add(dateRangePanel);

        // SEVERITY CHOICE FILTER
        ListMultipleChoice<LogLevel> severityChoice = new ListMultipleChoice<LogLevel>(
                "severity-choice"
                , new PropertyModel<List<LogLevel>>(filterModel, "severities")
                , LogLevel.getLogEnumList(), new IChoiceRenderer<LogLevel>() {
            @Override
            public Object getDisplayValue(LogLevel object) {
                return object.name();
            }

            @Override
            public String getIdValue(LogLevel object, int index) {
                return object.name();
            }
        });
        form.add(severityChoice);

        //FACILITY CHOICE FILTER
        //TODO needed refactoring this component
        ListMultipleChoice<LogFacility> facilityChoice = new ListMultipleChoice<LogFacility>(
                "facility-choice"
                , new PropertyModel<List<LogFacility>>(filterModel, "facilities")
                , LogFacility.getLogEnumList(), new IChoiceRenderer<LogFacility>() {
            @Override
            public Object getDisplayValue(LogFacility object) {
                return object.name();
            }

            @Override
            public String getIdValue(LogFacility object, int index) {
                return object.name();
            }
        });
        form.add(facilityChoice);

        //HOST CHOICE FILTER
        List<HostData> hostData = logCollector.loadHosts();
        ListMultipleChoice<HostData> hostDataChoice = new ListMultipleChoice<HostData>(
                "hostData-choice"
                , new PropertyModel<List<HostData>>(filterModel, "hosts")
                , hostData
                , new IChoiceRenderer<HostData>() {
            @Override
            public Object getDisplayValue(HostData object) {
                return object.getHostname();
            }

            @Override
            public String getIdValue(HostData object, int index) {
                return object.getHostname();
            }
        });
        form.add(hostDataChoice);

        //LIST LOG DATA
        PageableDataProvider<LogData> dataProvider = new PageableDataProvider<LogData>() {
            @Override
            public Collection<LogData> load(int offset, int limit) {
                try {
                    if(form.hasError()){
                        return Collections.emptyList();
                    }else {
                        return logCollector.search(
                                filterModel.getDateRangeModel().getDateRange().getFromDate()
                                , filterModel.getDateRangeModel().getDateRange().getToDate()
                                , filterModel.getFacilityIds()
                                , filterModel.getSeverityIds()
                                , filterModel.getHostIds()
                                , filterModel.getPattern()
                                , offset
                                , limit);
                    }
                } catch (IndexerServiceException e) {
                    error("Error while retrieving log data: " + e.getMessage()); //TODO fetch message from resource file
                    return Collections.emptyList();
                }
            }
        };

        final UncountablyPageableListView<LogData> listView = new UncountablyPageableListView<LogData>("list-log-data", dataProvider, filterModel.getItemPrePage()) {
            @Override
            protected void populateItem(Item<LogData> item) {
                LogData logData = item.getModelObject();
                String logLevel = LogLevel.forValue(logData.getSeverity());
                item.add(new Label("log-date", DateFormatUtils.format(logData.getDate().getTime(), "yyyy-MM-dd HH:mm:ss")));
                item.add(new Label("log-severity", logLevel));
                item.add(new Label("log-facility", LogFacility.forValue(logData.getFacility())));
                item.add(new Label("log-host", logData.getHost()));
                item.add(new Label("log-program", logData.getProgram()==null? "-":logData.getProgram()));
                item.add(new Label("log-message", logData.getMessage()));
                setHighlightCssClassBySeverity(logLevel, item);

                if (logData.hasSnortLogs()) {
                    PageParameters linkParameters = new PageParameters();

                    linkParameters.add("hash", logData.getHash());

                    BookmarkablePageLink<SnortLogMonitorPage> link = new BookmarkablePageLink<SnortLogMonitorPage>(
                        "log-snort-logs-link",
                        SnortLogMonitorPage.class,
                        linkParameters
                    );

                    item.add(link);
                }
                else {
                    item.add(new Label("log-snort-logs-link", ""));
                }
            }
        };
        form.add(listView);

        final UncountablyPageableNavigator<LogData> pagingNavigator = new UncountablyPageableNavigator<LogData>("paging-navigator", listView);
        form.add(pagingNavigator);

        form.add(new WebMarkupContainer("no-data"){
            @Override
            public boolean isVisible() {
                return listView.isEmpty();
            }
        });

        ButtonGroupPanel buttonGroupPanel = new ButtonGroupPanel("item-perPage", Arrays.asList(25, 50, 100)){
            @Override
            protected void doOnClick(Integer currentIndex) {
                filterModel.setItemPrePage(currentIndex);
                listView.setItemsPerPage(filterModel.getItemPrePage());
            }
        };
        form.add(buttonGroupPanel);

        form.add(new Button("search-button") {
            @Override
            public void onSubmit() {
                listView.setCurrentPage(0);
            }
        });

    }

    /**
     * Set filter model values from page parameters.
     *
     * @param       filterModel         Filter model.
     * @param       pageParameters      Page parameters.
     */
    private void fillFilterModel(FilterModel filterModel, PageParameters pageParameters) {
        StringValue severityValue = pageParameters.get(DashboardPage.SEVERITY);

        if (!severityValue.isEmpty()) {
            LogLevel severity = LogLevel.valueOf(severityValue.toString());
            filterModel.setSeverities(new ArrayList<LogLevel>(Arrays.asList(severity)));
        }

        DateRangeModel dateRangeModel = filterModel.getDateRangeModel();
        StringValue dateRangeTypeValue = pageParameters.get(DashboardPage.DATE_RANGE_TYPE);

        if (!dateRangeTypeValue.isEmpty()) {
            DateRangeType dateRangeType = DateRangeType.valueOf(dateRangeTypeValue.toString());
            dateRangeModel.setDateRangeType(dateRangeType);
        }

        StringValue dateTimeFromValue = pageParameters.get(TIME_FROM);

        if (!dateTimeFromValue.isEmpty()) {
            try {
                Date dateTimeFrom = dateParser.parse(dateTimeFromValue.toString());
                Date dateTimeTo = new DateTime(dateTimeFrom).plusMinutes(1).toDate();

                dateRangeModel.setDateRangeType(EXACTLY_TIME);
                dateRangeModel.setExactlyDateFrom(dateTimeFrom);
                dateRangeModel.setExactlyDateTo(dateTimeTo);
            }
            catch (ParseException ex) {
                throw new RuntimeException(ex);
            }
        }

        StringValue patternValue = pageParameters.get(PATTERN);

        if (!patternValue.isEmpty()) {
            filterModel.setPattern(patternValue.toString());
        }
    }

    @SpringBean
    private ILogCollector logCollector;

    private class FilterModel implements Serializable {
        private DateRangePanel.DateRangeModel dateRangeModel;
        private List<Integer> facilityIds;
        private List<LogFacility> facilities;

        private List<Integer> severityIds;
        private List<LogLevel> severities;

        private List<Integer> hostIds;
        private List<HostData> hosts;
        private String pattern;

        private Integer itemPrePage;

        private FilterModel() {
            this.severities = new ArrayList<LogLevel>();
            this.facilities = new ArrayList<LogFacility>();
            this.itemPrePage = 25;
            this.dateRangeModel = new DateRangePanel.DateRangeModel();
        }

        public DateRangePanel.DateRangeModel getDateRangeModel() {
            return dateRangeModel;
        }

        public void setDateRangeModel(DateRangePanel.DateRangeModel dateRangeModel) {
            this.dateRangeModel = dateRangeModel;
        }

        //FACILITY
        public List<Integer> getFacilityIds() {
            return facilityIds;
        }

        private void setFacilityIds(List<LogFacility> logFacilities) {
            this.facilityIds = getListIdsFromListEnum(logFacilities);
        }

        public List<LogFacility> getFacilities() {
            return facilities;
        }

        public void setFacilities(List<LogFacility> facilities) {
            this.facilities = facilities;
            setFacilityIds(facilities);
        }

        //SEVERITY
        public List<Integer> getSeverityIds() {
            return severityIds;
        }

        private void setSeverityIds(List<LogLevel> logLevels) {
            this.severityIds = getListIdsFromListEnum(logLevels);
        }

        public List<LogLevel> getSeverities() {
            return severities;
        }

        public void setSeverities(List<LogLevel> severities) {
            this.severities = severities;
            setSeverityIds(severities);
        }

        //HOST
        public List<Integer> getHostIds() {
            return hostIds;
        }

        private void setHostIds(List<HostData> hosts) {
            List<Integer> ids = new ArrayList<Integer>();
            for (HostData host : hosts) {
                ids.add(host.getId().intValue());
            }
            this.hostIds = ids;
        }

        public List<HostData> getHosts() {
            return hosts;
        }

        public void setHosts(List<HostData> hosts) {
            this.hosts = hosts;
            setHostIds(hosts);
        }

        public String getPattern() {
            return pattern;
        }

        public void setPattern(String pattern) {
            this.pattern = pattern;
        }

        public Integer getItemPrePage() {
            return itemPrePage;
        }

        public void setItemPrePage(Integer itemPrePage) {
            this.itemPrePage = itemPrePage;
        }

        private List<Integer> getListIdsFromListEnum(List<? extends LogEnum> logEnums) {
            List<Integer> ids = new ArrayList<Integer>(logEnums.size());
            for (LogEnum logEnum : logEnums) {
                ids.add(logEnum.getValue());
            }
            return ids;
        }
    }

}
