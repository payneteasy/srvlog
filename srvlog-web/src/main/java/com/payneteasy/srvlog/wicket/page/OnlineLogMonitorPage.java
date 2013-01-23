package com.payneteasy.srvlog.wicket.page;

import com.payneteasy.srvlog.data.LogData;
import com.payneteasy.srvlog.data.LogFacility;
import com.payneteasy.srvlog.data.LogLevel;
import com.payneteasy.srvlog.service.ILogCollector;
import com.payneteasy.srvlog.wicket.component.ButtonGroupPanel;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;
import org.springframework.security.access.annotation.Secured;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.payneteasy.srvlog.wicket.page.LogDataTableUtil.setHighlightCssClass;

/**
 * Date: 09.01.13
 */
@Secured("ROLE_ADMIN")
public class OnlineLogMonitorPage extends BasePage {

    @SpringBean
    private ILogCollector logCollector;
    private final WebMarkupContainer holderListView;

    public OnlineLogMonitorPage(PageParameters pageParameters) {
        super(pageParameters, OnlineLogMonitorPage.class);
        final FilterModel filterModel = new FilterModel();

        ButtonGroupPanel latesLogButtonGroupPanel = new ButtonGroupPanel("latestLog-groupButton-panel", Arrays.asList(25, 50, 100)) {
            @Override
            protected void doOnClick(Integer currentIndex) {
                filterModel.setLatestLogs(currentIndex);
            }
        };
        add(latesLogButtonGroupPanel);

        ButtonGroupPanel timeDurationGroupButtonPanel = new ButtonGroupPanel("timeDuration-groupButton-panel", Arrays.asList(2, 5, 10), true) {
            @Override
            protected void doOnAjaxClick(Integer currentIndex, AjaxRequestTarget target) {
                filterModel.setTimeDurationInSeconds(currentIndex);
                for (Behavior behavior : holderListView.getBehaviors()) {
                    if (behavior instanceof AjaxSelfUpdatingTimerBehavior) {
                        ((AjaxSelfUpdatingTimerBehavior) behavior).stop(target);
                    }
                }
                holderListView.add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(filterModel.getTimeDurationInSeconds())));
                target.add(holderListView);
            }
        };
        add(timeDurationGroupButtonPanel);


        IModel<List<LogData>> logDataModel = new LoadableDetachableModel<List<LogData>>() {
            @Override
            protected List<LogData> load() {
                return logCollector.loadLatest(filterModel.getLatestLogs());
            }
        };


        holderListView = new WebMarkupContainer("holder-search-log-data");
        holderListView.setOutputMarkupId(true);
        add(holderListView);

        ListView<LogData> listView = new ListView<LogData>("search-log-data", logDataModel) {
            @Override
            protected void populateItem(ListItem<LogData> item) {
                LogData logData = item.getModelObject();
                String logLevel = LogLevel.forValue(logData.getSeverity());
                item.add(new Label("log-date", DateFormatUtils.format(logData.getDate().getTime(), "yyyy-MM-dd HH:mm:ss")));
                item.add(new Label("log-severity", logLevel));
                item.add(new Label("log-facility", LogFacility.forValue(logData.getFacility())));
                item.add(new Label("log-host", logData.getHost()));
                item.add(new Label("log-program", logData.getProgram()==null? "-":logData.getProgram()));
                item.add(new Label("log-message", logData.getMessage()));
                setHighlightCssClass(logLevel, item);
            }
        };
        listView.setOutputMarkupId(true);
        holderListView.add(listView);

        //update panel
        holderListView.add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(filterModel.getTimeDurationInSeconds())));
    }

    public class FilterModel implements Serializable {
        private Integer latestLogs = 25;
        private Integer timeDurationInSeconds=2;

        public Integer getTimeDurationInSeconds() {
            return timeDurationInSeconds;
        }

        public void setTimeDurationInSeconds(Integer timeDurationInSeconds) {
            this.timeDurationInSeconds = timeDurationInSeconds;
        }

        public Integer getLatestLogs() {
            return latestLogs;
        }

        public void setLatestLogs(Integer latestLogs) {
            this.latestLogs = latestLogs;
        }
    }
}
