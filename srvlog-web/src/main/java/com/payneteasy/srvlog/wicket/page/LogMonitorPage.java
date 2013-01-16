package com.payneteasy.srvlog.wicket.page;

import com.payneteasy.srvlog.data.LogData;
import com.payneteasy.srvlog.data.LogFacility;
import com.payneteasy.srvlog.data.LogLevel;
import com.payneteasy.srvlog.service.ILogCollector;
import com.payneteasy.srvlog.service.IndexerServiceException;
import com.payneteasy.srvlog.util.DateRange;
import com.payneteasy.srvlog.util.DateRangeType;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.io.Serializable;
import java.util.*;

import static com.payneteasy.srvlog.wicket.component.repeater.LogDataTableUtil.setHighlightCssClass;

/**
 * Date: 11.01.13
 */
public class LogMonitorPage extends BasePage {

    public LogMonitorPage(PageParameters pageParameters) {
        super(pageParameters, LogMonitorPage.class);

        FeedbackPanel feedbackPanel = new FeedbackPanel("feedback-panel");
        add(feedbackPanel);

        final FilterModel filterModel = new FilterModel();

        Form<FilterModel> form = new Form<FilterModel>("form");
        add(form);

        form.add(new DropDownChoice<DateRangeType>(
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
        }));


        form.add(new Button("search-button") {
            @Override
            public void onSubmit() {

            }
        });

        IModel<List<LogData>> listDataModel = new LoadableDetachableModel<List<LogData>>() {
            @Override
            protected List<LogData> load() {
                try {
                    return logCollector.search(
                            filterModel.getDateRange().getFromDate()
                            , filterModel.getDateRange().getToDate()
                            , filterModel.getFacilities()
                            , filterModel.getSeverities()
                            , filterModel.getHosts()
                            , filterModel.getPattern()
                            , 0
                            , 26);
                } catch (IndexerServiceException e) {
                    error("Error while retrieving log data"); //TODO fetch message from resource file
                    return Collections.emptyList();
                }
            }
        };

        ListView<LogData> listLogDataView = new ListView<LogData>("list-log-data", listDataModel) {
            @Override
            protected void populateItem(ListItem<LogData> item) {
                LogData logData = item.getModelObject();
                String logLevel = LogLevel.forValue(logData.getSeverity());
                item.add(new Label("log-date", DateFormatUtils.SMTP_DATETIME_FORMAT.format(logData.getDate().getTime())));
                item.add(new Label("log-severity", logLevel));
                item.add(new Label("log-facility", LogFacility.forValue(logData.getFacility())));
                item.add(new Label("log-host", logData.getHost()));
                item.add(new Label("log-message", logData.getMessage()));
                setHighlightCssClass(logLevel, item);
            }
        };
        add(listLogDataView);
    }

    @SpringBean
    private ILogCollector logCollector;

    private class FilterModel implements Serializable {
        private DateRange dateRange;
        private DateRangeType dateRangeType;
        private List<Integer> facilities;
        private List<Integer> severities;
        private List<Integer> hosts;
        private String pattern;

        private FilterModel() {
            this.dateRange = DateRange.today();
            this.dateRangeType = DateRangeType.TODAY;
        }

        public DateRange getDateRange() {
            return dateRange;
        }

        public void setDateRangeType(DateRangeType dateRangeType) {
            this.dateRangeType = dateRangeType;
            setDateRange();
        }

        public DateRangeType getDateRangeType() {
            return dateRangeType;
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
                default:
                    throw new IllegalArgumentException("Unknown date range type: " + this.dateRangeType);
            }
        }

        public List<Integer> getFacilities() {
            return facilities;
        }

        public void setFacilities(List<Integer> facilities) {
            this.facilities = facilities;
        }

        public List<Integer> getSeverities() {
            return severities;
        }

        public void setSeverities(List<Integer> severities) {
            this.severities = severities;
        }

        public List<Integer> getHosts() {
            return hosts;
        }

        public void setHosts(List<Integer> hosts) {
            this.hosts = hosts;
        }

        public String getPattern() {
            return pattern;
        }

        public void setPattern(String pattern) {
            this.pattern = pattern;
        }
    }

}
