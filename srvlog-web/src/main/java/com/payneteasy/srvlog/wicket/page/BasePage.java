package com.payneteasy.srvlog.wicket.page;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.springframework.security.access.annotation.Secured;

/**
 * Date: 21.12.12 Time: 15:53
 */
@Secured("ROLE_ADMIN")
public class BasePage extends WebPage{
    private Class<? extends Page> pageClass;

    public BasePage() {
        this(null, MainPage.class);
    }

    public BasePage(PageParameters pageParameters, Class<? extends Page> pageClass) {
        this.pageClass = pageClass;

        //BAR MENU
        addBarLink("main", MainPage.class);
        addBarLink("logs", LogMonitorPage.class);
        addBarLink("online-logs", OnlineLogMonitorPage.class);
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
