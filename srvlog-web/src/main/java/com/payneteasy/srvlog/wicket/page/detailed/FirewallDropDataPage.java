package com.payneteasy.srvlog.wicket.page.detailed;

import com.payneteasy.srvlog.data.FireWallDropData;
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
public class FirewallDropDataPage extends DetailedFormPage{
    @SpringBean
    private ILogCollector logCollector;

    public FirewallDropDataPage(PageParameters pageParameters) {
        super(pageParameters, FirewallDropDataPage.class);

        LoadableDetachableModel<List<FireWallDropData>> listDataModel = new LoadableDetachableModel<List<FireWallDropData>>() {
            @Override
            protected List<FireWallDropData> load() {
                if(getForm().hasError()){
                    return Collections.emptyList();
                }
                return logCollector.getFirewallDropData(getFilterDetailedModel().getDate());
            }
        };

        final ListView<FireWallDropData> listView = new ListView<FireWallDropData>("list-view", listDataModel) {
            @Override
            protected void populateItem(ListItem<FireWallDropData> item) {
                FireWallDropData firewallDropData = item.getModelObject();
                item.add(new Label("protocol", firewallDropData.getProtocol()));
                item.add(new Label("drop-count", firewallDropData.getDropCount()));
                item.add(new Label("dist-ip", firewallDropData.getDestinationIp()));
                item.add(new Label("dist-port", firewallDropData.getDestinationPort()));
                item.add(new Label("source-ip", firewallDropData.getSourceIp()));
                item.add(new Label("source-port", firewallDropData.getSourcePort()));
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
