package com.payneteasy.srvlog.wicket;

import com.payneteasy.srvlog.wicket.page.BasePage;
import com.payneteasy.srvlog.wicket.page.LoginPage;
import com.payneteasy.srvlog.wicket.page.OnlineLogMonitorPage;
import com.payneteasy.srvlog.wicket.security.SrvlogAuthorizationStrategy;
import org.apache.wicket.Page;
import org.apache.wicket.core.util.file.WebApplicationPath;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;

/**
 * Date: 21.12.12 Time: 15:50
 */
public class SrvlogUIApplication extends WebApplication{
    @Override
    protected void init() {
        getResourceSettings().setThrowExceptionOnMissingResource(false);
        getResourceSettings().getResourceFinders().add(new WebApplicationPath(getServletContext(), "src/main/java"));

        addSpringComponentInjector();

        getSecuritySettings().setAuthorizationStrategy(new SrvlogAuthorizationStrategy());

        //PAGES
        mountPage("login", LoginPage.class);
        mountPage("logs", OnlineLogMonitorPage.class);
    }

    protected void addSpringComponentInjector(){
        getComponentInstantiationListeners().add(new SpringComponentInjector(this));
    }

    @Override
    public Class<? extends Page> getHomePage() {
        return BasePage.class;
    }

}
