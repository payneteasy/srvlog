package com.payneteasy.srvlog.wicket;

import com.payneteasy.srvlog.wicket.page.BasePage;
import com.payneteasy.srvlog.wicket.page.LoginPage;
import com.payneteasy.srvlog.wicket.security.SrvlogAuthorizationStrategy;
import org.apache.wicket.Page;
import org.apache.wicket.core.util.file.WebApplicationPath;
import org.apache.wicket.protocol.http.WebApplication;

/**
 * Date: 21.12.12 Time: 15:50
 */
public class SrvlogUIApplication extends WebApplication{
    @Override
    protected void init() {
        getResourceSettings().setThrowExceptionOnMissingResource(false);
        getResourceSettings().getResourceFinders().add(new WebApplicationPath(getServletContext(), "src/main/java"));
        getSecuritySettings().setAuthorizationStrategy(new SrvlogAuthorizationStrategy());

        //PAGES
        mountPage("login", LoginPage.class);
        mountPage("home", BasePage.class);
    }

    @Override
    public Class<? extends Page> getHomePage() {
        return BasePage.class;
    }
}
