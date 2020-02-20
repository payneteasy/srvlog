package com.payneteasy.srvlog.wicket.component.navigation;

import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.util.file.IResourceFinder;
import org.apache.wicket.util.file.Path;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 16.01.13
 * Time: 13:30
 */
public class TestDataNavigationApplication extends WebApplication {

    @Override
    protected void init() {
        List<IResourceFinder> resourceFinders = new ArrayList<>();
        resourceFinders.add(new Path("../srvlog-web/src/test/java"));
        resourceFinders.addAll(getResourceSettings().getResourceFinders());
        getResourceSettings().setResourceFinders(resourceFinders);
        addSpring();
        mountPage("main", TestDataNavigationPage.class);

    }

    protected void addSpring() {
        getComponentInstantiationListeners().add(new SpringComponentInjector(this));
    }

    @Override
    public Class<? extends Page> getHomePage() {
        return TestDataNavigationPage.class;
    }


}
