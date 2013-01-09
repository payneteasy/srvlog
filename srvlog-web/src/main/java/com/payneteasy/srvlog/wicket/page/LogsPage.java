package com.payneteasy.srvlog.wicket.page;

import com.payneteasy.srvlog.data.LogData;
import com.payneteasy.srvlog.service.ILogCollector;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.security.access.annotation.Secured;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Date: 09.01.13
 */
@Secured("ROLE_ADMIN")
public class LogsPage extends BasePage {

    @SpringBean
    private ILogCollector logCollector;

    public LogsPage() {
        final FilterModel filterModel = new FilterModel();
        Form<FilterModel> searchForm = new Form<FilterModel>("search-form", Model.<FilterModel>of(filterModel));
        add(searchForm);

        searchForm.add(
                new DropDownChoice<Integer>(
                        "search-filter-latestChoice"
                        , new PropertyModel<Integer>(filterModel, "latestLogs")
                        , Arrays.asList(25, 50, 100)
                )
        );

        searchForm.add(new ListView<LogData>("search-log-data", new PropertyModel<List<LogData>>(filterModel, "logData")) {
            @Override
            protected void populateItem(ListItem<LogData> item) {
                LogData logData = item.getModelObject();
                item.add(new Label("log-date", logData.getDate()));
            }
        });

        searchForm.add(new Button("search-btn") {
            @Override
            public void onSubmit() {
                filterModel.setLogData(logCollector.loadLatest(filterModel.getLatestLogs()));
            }
        });
    }

    public class FilterModel implements Serializable {
        private Integer latestLogs = 25;
        private List<LogData> logData;

        public Integer getLatestLogs() {
            return latestLogs;
        }

        public void setLatestLogs(Integer latestLogs) {
            this.latestLogs = latestLogs;
        }

        public List<LogData> getLogData() {
            return logData;
        }

        public void setLogData(List<LogData> logData) {
            this.logData = logData;
        }
    }
}
