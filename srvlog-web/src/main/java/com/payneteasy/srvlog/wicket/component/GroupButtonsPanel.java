package com.payneteasy.srvlog.wicket.component;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * Date: 11.01.13
 */
public class GroupButtonsPanel extends Panel{
    public GroupButtonsPanel(String id, IModel<?> model,final Component updatedComponent) {
        super(id, model);

    }

    private void addButtonToGroup(final Component updatedComponent){
        AjaxLink<Void> ajaxLink = new AjaxLink<Void>("group-button") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                target.add(updatedComponent);
            }
        };
        add(ajaxLink);
    }

}
