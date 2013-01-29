package com.payneteasy.srvlog.wicket.page;

import com.payneteasy.srvlog.service.ILogCollector;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.security.access.annotation.Secured;

/**
 * Date: 21.12.12 Time: 15:53
 */
@Secured("ROLE_ADMIN")
public class BasePage extends WebPage {
    private Class<? extends Page> pageClass;
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

        //SHOW WARNINGS
        final Long numberUnprocessedHosts =  logCollector.getNumberUnprocessedHosts();
        Link<Void> warningsLink = new Link<Void>("warnings-link") {
            @Override
            public void onClick() {
                logCollector.saveUnprocessedHosts();
                setResponsePage(getPage());
            }

            @Override
            public boolean isVisible() {
                return numberUnprocessedHosts > 0;
            }
        };
        warningsLink.add(new Label("warnings-label", numberUnprocessedHosts));
        add(warningsLink);
    }

    private void addBarLink(String linkId, Class<? extends Page> pageClass){
        WebMarkupContainer webMarkupContainer = new WebMarkupContainer(linkId+"-container");
        BookmarkablePageLink<Page> bookmarkablePageLink = new BookmarkablePageLink<Page>(linkId, pageClass);
        if(this.pageClass.equals(pageClass)){
            webMarkupContainer.add(new AttributeModifier("class",  new AbstractReadOnlyModel<String>() {
                @Override
                public String getObject() {
                   return "active";
                }
            }));
        }
        webMarkupContainer.add(bookmarkablePageLink);
        add(webMarkupContainer);
    }
}
