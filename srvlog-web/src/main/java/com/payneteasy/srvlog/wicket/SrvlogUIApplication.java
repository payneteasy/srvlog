package com.payneteasy.srvlog.wicket;

import com.google.common.collect.Lists;
import com.payneteasy.srvlog.wicket.page.LogMonitorPage;
import com.payneteasy.srvlog.wicket.page.LoginPage;
import com.payneteasy.srvlog.wicket.page.MainPage;
import com.payneteasy.srvlog.wicket.page.OnlineLogMonitorPage;
import com.payneteasy.srvlog.wicket.security.SrvlogAuthorizationStrategy;
import org.apache.wicket.Page;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.core.util.file.WebApplicationPath;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.util.file.IResourceFinder;
import org.apache.wicket.util.file.Path;

import java.util.List;

/**
 * Date: 21.12.12 Time: 15:50
 */
public class SrvlogUIApplication extends WebApplication{
    @Override
    protected void init() {
        getResourceSettings().setThrowExceptionOnMissingResource(false);

        if (this.getConfigurationType() == RuntimeConfigurationType.DEVELOPMENT) {
            List<IResourceFinder> resourceFinders = Lists.newArrayList();
            resourceFinders.add(new WebApplicationPath(getServletContext(), "../srvlog-web/src/main/java"));
            resourceFinders.addAll(getResourceSettings().getResourceFinders());
            getResourceSettings().setResourceFinders(resourceFinders);
        }

        getSecuritySettings().setAuthorizationStrategy(new SrvlogAuthorizationStrategy());

        addSpringComponentInjector();
        //PAGES
        mountPage("main", MainPage.class);
        mountPage("logs", LogMonitorPage.class);
        mountPage("online-logs", OnlineLogMonitorPage.class);

        mountPage("login", LoginPage.class);
    }

    protected void addSpringComponentInjector(){
        getComponentInstantiationListeners().add(new SpringComponentInjector(this));
    }

    @Override
    public Class<? extends Page> getHomePage() {
        return MainPage.class;
    }

}
