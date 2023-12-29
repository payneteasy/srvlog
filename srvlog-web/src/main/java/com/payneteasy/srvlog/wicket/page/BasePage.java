package com.payneteasy.srvlog.wicket.page;

import com.payneteasy.srvlog.service.ILogCollector;
import com.payneteasy.srvlog.wicket.page.detailed.FirewallAlertDataPage;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.security.access.annotation.Secured;

/**
 * Date: 21.12.12 Time: 15:53
 */
@Secured("ROLE_ADMIN")
public class BasePage extends WebPage {

    private Class<? extends Page> pageClass;
    public final static String HAS_UNPROCESSED_HOSTS_PARAMETER = "has-unpr-host";

    private Boolean showWarning;

    @SpringBean
    private ILogCollector logCollector;

    public BasePage() {
        this(null, DashboardPage.class);
    }

    public BasePage(PageParameters pageParameters, Class<? extends Page> pageClass) {
        this.pageClass = pageClass;

        //BAR MENU
        addBarLink("dashboard", DashboardPage.class);
        addBarLink("logs", LogMonitorPage.class);
        addBarLink("online-logs", OnlineLogMonitorPage.class);
        addBarLink("add-hosts", AddHostsPage.class);
        addBarLink("detailed-logs", FirewallAlertDataPage.class);
        addBarLink("terminal", TerminalPage.class);


        IModel<Boolean> model = new LoadableDetachableModel<>() {
            @Override
            protected Boolean load() {
                return logCollector.hasUnprocessedLogs();
            }
        };

        //SHOW WARNINGS
        Link<Boolean> warningsLink = new Link<Boolean>("warnings-link", model) {
            @Override
            public void onClick() {
                logCollector.saveUnprocessedLogs();
                setResponsePage(getPageClass());
            }

            @Override
            public boolean isVisible() {
                return getModel().getObject();
            }
        };
        add(warningsLink);

        Link<Void> showUnprocessedHostsName = new Link<Void>("show-unprocessed-hosts-link") {
            @Override
            public void onClick() {
                PageParameters newPageParameter = new PageParameters();
                newPageParameter.add(HAS_UNPROCESSED_HOSTS_PARAMETER, HAS_UNPROCESSED_HOSTS_PARAMETER);
                setResponsePage(AddHostsPage.class, newPageParameter);
            }
        };
        add(showUnprocessedHostsName);
    }

    @Override
    protected void onRender() {
        showWarning = logCollector.hasUnprocessedLogs();
        super.onRender();
    }

    private void addBarLink(String linkId, Class<? extends Page> pageClass){
        WebMarkupContainer webMarkupContainer = new WebMarkupContainer(linkId+"-container");
        BookmarkablePageLink<Page> bookmarkablePageLink = new BookmarkablePageLink<Page>(linkId, pageClass);
        if(this.pageClass.equals(pageClass)){
            webMarkupContainer.add(new AttributeModifier("class", (IModel<Object>) () -> "active"));
        }
        webMarkupContainer.add(bookmarkablePageLink);
        add(webMarkupContainer);
    }
}
