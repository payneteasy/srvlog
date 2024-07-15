package com.payneteasy.srvlog.wicket.page.detailed;

import com.payneteasy.srvlog.data.FirewallAlertData;
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
 * Date: 17.02.13 Time: 17:48
 */
public class FirewallAlertDataPage extends DetailedFormPage{
    @SpringBean
    private ILogCollector logCollector;

    public FirewallAlertDataPage(PageParameters pageParameters) {
        super(pageParameters, FirewallAlertDataPage.class);

        LoadableDetachableModel<List<FirewallAlertData>> listDataModel = new LoadableDetachableModel<>() {
            @Override
            protected List<FirewallAlertData> load() {
                if (getForm().hasError()) {
                    return Collections.emptyList();
                }
                return logCollector.getFirewallAlertData(getFilterDetailedModel().getDate());
            }
        };

        final ListView<FirewallAlertData> listView = new ListView<>("list-view", listDataModel) {
            @Override
            protected void populateItem(ListItem<FirewallAlertData> item) {
                FirewallAlertData firewallAlertData = item.getModelObject();
                item.add(new Label("alert-class", firewallAlertData.getAlertClass()));
                item.add(new Label("alert-count", firewallAlertData.getAlertCount()));
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
