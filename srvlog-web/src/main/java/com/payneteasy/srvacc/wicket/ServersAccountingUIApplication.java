package com.payneteasy.srvacc.wicket;

import com.payneteasy.srvacc.wicket.page.BasePage;
import com.payneteasy.srvacc.wicket.page.LoginPage;
import com.payneteasy.srvacc.wicket.security.ServersAccountingAuthorizationStrategy;
import org.apache.wicket.IResourceFactory;
import org.apache.wicket.Page;
import org.apache.wicket.core.util.file.WebApplicationPath;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.resource.IResource;

import java.util.Locale;

/**
 * Date: 21.12.12 Time: 15:50
 */
public class ServersAccountingUIApplication extends WebApplication{
    @Override
    protected void init() {
        getResourceSettings().setThrowExceptionOnMissingResource(false);
        getResourceSettings().getResourceFinders().add(new WebApplicationPath(getServletContext(), "src/main/java"));
        getSecuritySettings().setAuthorizationStrategy(new ServersAccountingAuthorizationStrategy());

        //PAGES
        mountPage("login", LoginPage.class);
        mountPage("home", BasePage.class);
    }

    @Override
    public Class<? extends Page> getHomePage() {
        return BasePage.class;
    }
}
