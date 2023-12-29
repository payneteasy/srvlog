package com.payneteasy.srvlog.wicket.component;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import java.util.List;

/**
 * Date: 17.01.13 Time: 15:28
 */
public  class ButtonGroupPanel extends Panel{
    public ButtonGroupPanel(String id, List<Integer> indexers) {
        this(id, indexers, false);
    }
    public ButtonGroupPanel(String id, List<Integer> indexers, final boolean isAjax) {
        super(id);
        this.currentIndex = indexers.get(0);

        holderContainer = new WebMarkupContainer("holder-container");
        holderContainer.setOutputMarkupId(true);
        add(holderContainer);

        ListView<Integer> listView = new ListView<Integer>("button-groups", indexers) {
            @Override
            protected void populateItem(final ListItem<Integer> item) {
                final Integer group = item.getModelObject();
                final AbstractLink groupButton;
                if(isAjax){
                    item.setOutputMarkupId(true);
                    groupButton = new AjaxLink<Void>("button"){

                        @Override
                        public void onClick(AjaxRequestTarget target) {
                            doOnAjaxClick(group, target);
                            setCurrentIndex(group);
                            target.add(holderContainer);
                        }
                    };
                }else {
                    groupButton = new Link<Void>("button") {
                        @Override
                        public void onClick() {
                            doOnClick(group);
                            setCurrentIndex(group);
                        }
                    };
                }
                groupButton.add(new Label("button-name", group));
                groupButton.add(new AttributeAppender("class", (IModel<Object>) () -> {
                    if (getCurrentIndex().equals(group)) {
                        return "active";
                    }
                    return "";
                }, " "));
                item.add(groupButton);
            }
        };
        holderContainer.add(listView);
    }

    protected  void doOnClick(Integer currentIndex){}
    protected void doOnAjaxClick(Integer currentIndex, AjaxRequestTarget target){}

    private Integer currentIndex;
    private WebMarkupContainer holderContainer;

    public Integer getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(Integer currentIndex) {
        this.currentIndex = currentIndex;
    }
}
