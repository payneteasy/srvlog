package com.payneteasy.srvlog.wicket.page.detailed;

import com.payneteasy.srvlog.data.OssecAlertData;
import com.payneteasy.srvlog.service.ILogCollector;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Collections;
import java.util.List;

/**
 * Date: 17.02.13 Time: 17:49
 */
public class OssecAlertDataPage extends DetailedFormPage{
    @SpringBean
    private ILogCollector logCollector;
    public OssecAlertDataPage(PageParameters pageParameters) {
        super(pageParameters, OssecAlertDataPage.class);

        LoadableDetachableModel<List<OssecAlertData>> listDataModel = new LoadableDetachableModel<List<OssecAlertData>>() {
            @Override
            protected List<OssecAlertData> load() {
                if(getForm().hasError()){
                    return Collections.emptyList();
                }
                return logCollector.getOssecAlertData(getFilterDetailedModel().getDate());
            }
        };

        final ListView<OssecAlertData> listView = new ListView<OssecAlertData>("list-view", listDataModel) {
            @Override
            protected void populateItem(ListItem<OssecAlertData> item) {
                OssecAlertData ossecAlertData = item.getModelObject();
                item.add(new Label("type", ossecAlertData.getOssecAlertType()));
                item.add(new Label("count", ossecAlertData.getOssecAlertCount()));
            }
        };
        add(listView);

        add(new WebMarkupContainer("no-data"){
            @Override
            public boolean isVisible() {
                return listView.size()==0;
            }
        });

    }
}
