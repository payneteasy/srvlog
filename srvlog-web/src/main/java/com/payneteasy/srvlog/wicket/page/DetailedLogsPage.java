package com.payneteasy.srvlog.wicket.page;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * Date: 17.02.13 Time: 17:56
 */
public class DetailedLogsPage extends BasePage{
    private Class<? extends Page> sideBarPageClass;
    public DetailedLogsPage(PageParameters pageParameters, Class<? extends Page> sideBarPageClass) {
        super(pageParameters, DetailedLogsPage.class);
        this.sideBarPageClass = sideBarPageClass;

        addSideBarLink("firewall-alert", FirewallAlertDataPage.class);
        addSideBarLink("firewall-drop", FirewallDropDataPage.class);
        addSideBarLink("ossec-alert", OssecAlertDataPage.class);

    }

    private void addSideBarLink(String linkId, Class<? extends DetailedLogsPage> pageClass){
        WebMarkupContainer webMarkupContainer = new WebMarkupContainer(linkId+"-container");
        BookmarkablePageLink<Page> bookmarkablePageLink = new BookmarkablePageLink<Page>(linkId, pageClass);
        if(this.sideBarPageClass.equals(pageClass)){
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
