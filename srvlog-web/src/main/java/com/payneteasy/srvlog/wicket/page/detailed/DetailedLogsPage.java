package com.payneteasy.srvlog.wicket.page.detailed;

import com.payneteasy.srvlog.wicket.page.BasePage;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Page;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

/**
 * Date: 17.02.13 Time: 17:56
 */
public class DetailedLogsPage extends BasePage {
    private Class<? extends Page> sideBarPageClass;
    public DetailedLogsPage(PageParameters pageParameters, Class<? extends Page> sideBarPageClass) {
        super(pageParameters, DetailedLogsPage.class);
        this.sideBarPageClass = sideBarPageClass;

        addSideBarLink("firewall-alert", FirewallAlertDataPage.class);
        addSideBarLink("firewall-drop", FirewallDropDataPage.class);
        addSideBarLink("ossec-alert", OssecAlertDataPage.class);

    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.render(CssHeaderItem.forReference(new CssResourceReference(getClass(), "../css/main.css")));
        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(getClass(), "../js/bootstrap.min.js")));
    }

    private void addSideBarLink(String linkId, Class<? extends DetailedLogsPage> pageClass){
        WebMarkupContainer webMarkupContainer = new WebMarkupContainer(linkId+"-container");
        BookmarkablePageLink<Page> bookmarkablePageLink = new BookmarkablePageLink<>(linkId, pageClass);
        if(this.sideBarPageClass.equals(pageClass)){
            webMarkupContainer.add(new AttributeModifier("class", (IModel<String>) () -> "active"));
        }
        webMarkupContainer.add(bookmarkablePageLink);
        add(webMarkupContainer);
    }
}
