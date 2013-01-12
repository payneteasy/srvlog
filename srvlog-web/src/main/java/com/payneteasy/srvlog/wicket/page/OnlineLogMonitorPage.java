package com.payneteasy.srvlog.wicket.page;

import com.payneteasy.srvlog.data.LogData;
import com.payneteasy.srvlog.service.ILogCollector;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.security.access.annotation.Secured;

import java.io.Serializable;
import java.util.List;

/**
 * Date: 09.01.13
 */
@Secured("ROLE_ADMIN")
public class OnlineLogMonitorPage extends BasePage {

    @SpringBean
    private ILogCollector logCollector;

    public OnlineLogMonitorPage(PageParameters pageParameters) {
        super(pageParameters, OnlineLogMonitorPage.class);
        final FilterModel filterModel = new FilterModel();

        addButtonToGroup(25, filterModel);
        addButtonToGroup(50, filterModel);
        addButtonToGroup(100, filterModel);

        IModel<List<LogData>> logDataModel = new LoadableDetachableModel<List<LogData>>() {
            @Override
            protected List<LogData> load() {
                return logCollector.loadLatest(filterModel.getLatestLogs());
            }
        };

        add(new ListView<LogData>("search-log-data", logDataModel) {
            @Override
            protected void populateItem(ListItem<LogData> item) {
                LogData logData = item.getModelObject();
                item.add(new Label("log-date", DateFormatUtils.SMTP_DATETIME_FORMAT.format(logData.getDate().getTime())));
                item.add(new Label("log-severity", logData.getSeverity()));
                item.add(new Label("log-facility", logData.getFacility()));
                item.add(new Label("log-host", logData.getHost()));
                item.add(new Label("log-message", logData.getMessage()));
            }
        });
    }

    private void addButtonToGroup(final Integer latestLogs, final FilterModel filterModel) {
        Link<Void> groupButton = new Link<Void>("group-button-" + latestLogs) {
            @Override
            public void onClick() {
                filterModel.setLatestLogs(latestLogs);
            }
        };

        groupButton.add(new AttributeAppender("class", new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject() {
                if (latestLogs.equals(filterModel.getLatestLogs())) {
                    return "active";
                }
                return "";
            }
        }, " "));
        add(groupButton);
    }

    public class FilterModel implements Serializable {
        private Integer latestLogs = 25;

        public Integer getLatestLogs() {
            return latestLogs;
        }

        public void setLatestLogs(Integer latestLogs) {
            this.latestLogs = latestLogs;
        }
    }
}
