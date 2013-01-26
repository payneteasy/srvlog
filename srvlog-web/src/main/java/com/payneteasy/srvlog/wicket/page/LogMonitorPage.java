package com.payneteasy.srvlog.wicket.page;

import com.payneteasy.srvlog.data.*;
import com.payneteasy.srvlog.service.ILogCollector;
import com.payneteasy.srvlog.service.IndexerServiceException;
import com.payneteasy.srvlog.util.DateRange;
import com.payneteasy.srvlog.util.DateRangeType;
import com.payneteasy.srvlog.wicket.component.ButtonGroupPanel;
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
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.io.Serializable;
import java.util.*;

import static com.payneteasy.srvlog.wicket.page.LogDataTableUtil.setHighlightCssClassBySeverity;

/**
 * Date: 11.01.13
 */
public class LogMonitorPage extends BasePage {

    public LogMonitorPage(PageParameters pageParameters) {
        super(pageParameters, LogMonitorPage.class);

        FeedbackPanel feedbackPanel = new FeedbackPanel("feedback-panel");
        add(feedbackPanel);

        final FilterModel filterModel = new FilterModel();

        final Form<FilterModel> form = new Form<FilterModel>("form");
        add(form);

        //DATE RANGE FILTER
        form.add(new TextField<String>("pattern", new PropertyModel<String>(filterModel, "pattern")));

        final DropDownChoice<DateRangeType> dateRangeType = new DropDownChoice<DateRangeType>(
                "date-range-type"
                , new PropertyModel<DateRangeType>(filterModel, "dateRangeType")
                , Arrays.asList(DateRangeType.values())
                , new IChoiceRenderer<DateRangeType>() {
            @Override
            public Object getDisplayValue(DateRangeType object) {
                return object.name();
            }

            @Override
            public String getIdValue(DateRangeType object, int index) {
                return object.name();
            }
        });
        form.add(dateRangeType);
        dateRangeType.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                target.add(holderDateRangeContainer);
            }
        });

        holderDateRangeContainer = new WebMarkupContainer("holder-exactly-dateRange") {
            @Override
            public boolean isVisible() {
                return isVisibleDateField(filterModel.getDateRangeType());
            }
        };
        holderDateRangeContainer.setOutputMarkupPlaceholderTag(true);
        form.add(holderDateRangeContainer);

        DateTextField dateFromTextField = getExactlyDateTextField("dateFrom-field", form, filterModel, "exactlyDateFrom", "dateForm");
        holderDateRangeContainer.add(dateFromTextField);
        DateTextField dateToTextField = getExactlyDateTextField("dateTo-field", form, filterModel, "exactlyDateTo", "dateTo");
        holderDateRangeContainer.add(dateToTextField);

        DateTimeField dateFromTimeField = getExactlyDateTimeField("timeFrom-field", form, filterModel, "exactlyDateFrom", "timeFrom");
        holderDateRangeContainer.add(dateFromTimeField);
        DateTimeField dateToTimeField = getExactlyDateTimeField("timeTo-field", form, filterModel, "exactlyDateTo", "timeTo");
        holderDateRangeContainer.add(dateToTimeField);

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
                                filterModel.getDateRange().getFromDate()
                                , filterModel.getDateRange().getToDate()
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

    private DateTextField getExactlyDateTextField(String id, Form form, final FilterModel filterModel, String expression, String keyPrefix) {
        DateTextField dateTextField = new DateTextField(id, new PropertyModel<Date>(filterModel, expression), new PatternDateConverter(DATE_PATTERN, false)) {
            @Override
            public boolean isVisible() {
                return DateRangeType.EXACTLY_DATE == filterModel.getDateRangeType();
            }
        };
        dateTextField.add(new DatePicker());
        dateTextField.setRequired(true);
        return dateTextField;
    }

    private DateTimeField getExactlyDateTimeField(String id, Form form, final FilterModel filterModel, String expression, String keyPrefix) {
        DateTimeField dateTimeField = new DateTimeField(id, new PropertyModel<Date>(filterModel, expression)) {
            @Override
            protected boolean use12HourFormat() {
                return false;
            }

            @Override
            public boolean isVisible() {
                return DateRangeType.EXACTLY_TIME == filterModel.getDateRangeType();
            }

            @Override
            protected DateTextField newDateTextField(String id, PropertyModel<Date> dateFieldModel) {
                return DateTextField.forDatePattern(id, dateFieldModel, DATE_PATTERN);
            }

            @Override
            protected DatePicker newDatePicker() {
                return new DatePicker() {
                    @Override
                    protected String getDatePattern() {
                        return DATE_PATTERN;
                    }
                };
            }
        };
        dateTimeField.setRequired(true);
        return dateTimeField;
    }

    private boolean isVisibleDateField(DateRangeType type) {
        if (DateRangeType.EXACTLY_DATE == type || DateRangeType.EXACTLY_TIME == type) {
            return true;
        }
        return false;
    }

    @SpringBean
    private ILogCollector logCollector;
    private static final String DATE_PATTERN = "dd.MM.yyyy";
    private WebMarkupContainer holderDateRangeContainer;

    private class FilterModel implements Serializable {
        private DateRange dateRange;
        private DateRangeType dateRangeType;
        private Date exactlyDateFrom;
        private Date exactlyDateTo;

        private List<Integer> facilityIds;
        private List<LogFacility> facilities;

        private List<Integer> severityIds;
        private List<LogLevel> severities;

        private List<Integer> hostIds;
        private List<HostData> hosts;
        private String pattern;

        private Integer itemPrePage;

        private FilterModel() {
            this.dateRange = DateRange.today();
            this.dateRangeType = DateRangeType.TODAY;
            this.severities = new ArrayList<LogLevel>();
            this.facilities = new ArrayList<LogFacility>();
            this.itemPrePage = 25;

        }

        public void setDateRangeType(DateRangeType dateRangeType) {
            this.dateRangeType = dateRangeType;
            setDateRange();
        }

        public DateRangeType getDateRangeType() {
            return dateRangeType;
        }

        public Date getExactlyDateFrom() {
            return exactlyDateFrom;
        }

        public void setExactlyDateFrom(Date exactlyDateFrom) {
            this.exactlyDateFrom = exactlyDateFrom;
        }

        public Date getExactlyDateTo() {
            return exactlyDateTo;
        }

        public void setExactlyDateTo(Date exactlyDateTo) {
            this.exactlyDateTo = exactlyDateTo;
        }

        public DateRange getDateRange() {
            if (isVisibleDateField(this.dateRangeType)) {
                dateRange = new DateRange(exactlyDateFrom, exactlyDateTo);
            }
            return dateRange;
        }

        private void setDateRange() {
            switch (this.dateRangeType) {
                case TODAY:
                    dateRange = DateRange.today();
                    break;
                case YESTERDAY:
                    dateRange = DateRange.yesterday();
                    break;
                case THIS_WEEK:
                    dateRange = DateRange.thisWeek();
                    break;
                case LAST_WEEK:
                    dateRange = DateRange.lastWeek();
                    break;
                case THIS_MONTH:
                    dateRange = DateRange.thisMonth();
                    break;
                case LAST_MONTH:
                    dateRange = DateRange.lastMonth();
                    break;
                case EXACTLY_DATE:
                    break;
                case EXACTLY_TIME:
                    break;
                default:
                    throw new IllegalArgumentException("Unknown date range type: " + this.dateRangeType);
            }
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
