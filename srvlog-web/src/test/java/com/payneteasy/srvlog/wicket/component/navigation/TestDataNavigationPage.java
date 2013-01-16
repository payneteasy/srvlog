package com.payneteasy.srvlog.wicket.component.navigation;

import com.payneteasy.srvlog.wicket.component.navigation.service.FakeData;
import com.payneteasy.srvlog.wicket.component.navigation.service.FakeDataLoaderService;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Collection;

/**
 * Date: 16.01.13
 * Time: 13:14
 */
public class TestDataNavigationPage extends WebPage {

    @SpringBean
    private FakeDataLoaderService loaderService;

    public TestDataNavigationPage() {

        UncountablyPageableListView<FakeData> listView = new UncountablyPageableListView<FakeData>("data-panel", new PageableDataProvider<FakeData>() {

            public Collection<FakeData> load(int offset, int limit) {
                return loaderService.loadFakePageableList(offset, limit);
            }

        }, 10) {
            @Override
            protected void populateItem(Item<FakeData> item) {
                item.add(new Label("id", new PropertyModel(item.getModel(), "id")));
                item.add(new Label("message", new PropertyModel(item.getModel(), "message")));
            }
        };
        add(listView);

        add(new UncountablyPageableNavigator<FakeData>("paging-navigator", listView));
    }
}
