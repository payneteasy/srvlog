package com.payneteasy.srvlog.wicket.component.navigation;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import java.io.Serializable;

/**
 * Date: 16.01.13
 * Time: 18:16
 */
public class UncountablyPageableNavigator<T extends Serializable> extends Panel {

    private final IUncountablyPageable pageable;

    @Override
    public boolean isVisible() {
        return !pageable.isEmpty();
    }

    public UncountablyPageableNavigator(String id, IUncountablyPageable aPageable) {
        super(id);
        this.pageable = aPageable;
        add(new Link("paging-next") {
            @Override
            public boolean isEnabled() {
                return pageable.hasNextPage();    //To change body of overridden methods use File | Settings | File Templates.
            }

            @Override
            public void onClick() {
                pageable.setCurrentPage(pageable.getCurrentPage() + 1);
            }
        });

        add(new Link("paging-previous") {

            @Override
            public boolean isEnabled() {
                return (pageable.getCurrentPage() > 0);
            }

            @Override
            public void onClick() {
                pageable.setCurrentPage(pageable.getCurrentPage() - 1);
            }
        });

        add(new Link("paging-first") {
            @Override
            public boolean isEnabled() {
                return (pageable.getCurrentPage() > 0);
            }

            @Override
            public void onClick() {
                pageable.setCurrentPage(0);
            }
        });

        add(new Label("paging-from-row", new Model() {
            @Override
            public Serializable getObject() {
                return pageable.getFromRow();    //To change body of overridden methods use File | Settings | File Templates.
            }
        })
        );

        add(new Label("paging-to-row", new Model() {
            @Override
            public Serializable getObject() {
                return pageable.getToRow();    //To change body of overridden methods use File | Settings | File Templates.
            }
        })
        );
    }
}
