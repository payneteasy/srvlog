package com.payneteasy.srvlog.wicket.page;

import com.payneteasy.srvlog.data.HostData;
import com.payneteasy.srvlog.data.LogData;
import com.payneteasy.srvlog.data.LogFacility;
import com.payneteasy.srvlog.data.LogLevel;
import com.payneteasy.srvlog.service.ILogCollector;
import com.payneteasy.srvlog.wicket.component.ButtonGroupPanel;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;
import java.time.Duration;
import org.springframework.security.access.annotation.Secured;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.payneteasy.srvlog.utils.LogDataTableUtil.setHighlightCssClassBySeverity;

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
                updateComponentBehavior(target, filterModel);
                target.add(holderListView);
            }
        };
        add(timeDurationGroupButtonPanel);

        Form<FilterModel> hostChoiceForm = new Form<>("hostChoice-form");
        add(hostChoiceForm);
        DropDownChoice<HostData> hostChoices = new DropDownChoice<>("choices-host", new PropertyModel<>(filterModel, "hostData"), new LoadableDetachableModel<List<HostData>>() {
            @Override
            protected List<HostData> load() {
                return logCollector.loadHosts();
            }
        }, new ChoiceRenderer<>("hostname"));
        hostChoices.add(new AjaxFormComponentUpdatingBehavior("change") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                updateComponentBehavior(target, filterModel);
                target.add(holderListView);
            }
        });
        hostChoices.setNullValid(true);
        hostChoiceForm.add(hostChoices);

        IModel<List<LogData>> logDataModel = new LoadableDetachableModel<>() {
            @Override
            protected List<LogData> load() {
                return logCollector.loadLatest(filterModel.getLatestLogs(), checkForNullHost(filterModel.getHostData()));
            }
        };
        holderListView = new WebMarkupContainer("holder-search-log-data");
        holderListView.setOutputMarkupId(true);
        add(holderListView);

        ListView<LogData> listView = new ListView<>("search-log-data", logDataModel) {
            @Override
            protected void populateItem(ListItem<LogData> item) {
                LogData logData = item.getModelObject();
                String logLevel = LogLevel.forValue(logData.getSeverity());
                item.add(new Label("log-date", DateFormatUtils.format(logData.getDate().getTime(), "yyyy-MM-dd HH:mm:ss")));
                item.add(new Label("log-severity", logLevel));
                item.add(new Label("log-facility", LogFacility.forValue(logData.getFacility())));
                item.add(new Label("log-host", logData.getHost()));
                item.add(new Label("log-program", logData.getProgram() == null ? "-" : logData.getProgram()));
                item.add(new Label("log-message", logData.getMessage()));
                setHighlightCssClassBySeverity(logLevel, item);
            }
        };
        listView.setOutputMarkupId(true);
        holderListView.add(listView);

//        //update panel
        holderListView.add(new AjaxSelfUpdatingTimerBehavior(Duration.ofSeconds(filterModel.getTimeDurationInSeconds())){
            @Override
            protected void onPostProcessTarget(AjaxRequestTarget target) {
                animateLastTableRow(target);
            }
        });
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(getClass(), "js/jquery-ui-1.10.0.custom.min.js")));
    }

    private void updateComponentBehavior(AjaxRequestTarget target, final FilterModel filterModel) {
        for (Behavior behavior : holderListView.getBehaviors()) {
            if (behavior instanceof AjaxSelfUpdatingTimerBehavior) {
                ((AjaxSelfUpdatingTimerBehavior) behavior).stop(target);
            }
        }
        holderListView.add(new AjaxSelfUpdatingTimerBehavior(Duration.ofSeconds(filterModel.getTimeDurationInSeconds())){
            @Override
            protected void onPostProcessTarget(AjaxRequestTarget target) {
                animateLastTableRow(target);
            }
        });
    }

    private void animateLastTableRow(AjaxRequestTarget target) {
        target.appendJavaScript("animateLastTableRow()");
    }

    private Long checkForNullHost(HostData hostData) {
        return hostData!=null?hostData.getId():null;
    }

    public class FilterModel implements Serializable {
        private Integer latestLogs = 25;
        private Integer timeDurationInSeconds=2;
        private Date lastDate;
        private HostData hostData;

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

        public Date getLastDate() {
            return lastDate;
        }

        public void setLastDate(Date lastDate) {
            this.lastDate = lastDate;
        }

        public HostData getHostData() {
            return hostData;
        }

        public void setHostData(HostData hostData) {
            this.hostData = hostData;
        }
    }
}
