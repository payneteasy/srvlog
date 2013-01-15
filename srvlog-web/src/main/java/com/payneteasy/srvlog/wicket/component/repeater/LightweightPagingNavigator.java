package com.payneteasy.srvlog.wicket.component.repeater;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.LoadableDetachableModel;

import java.io.Serializable;

/**
 * Page navigator (google like) which does not display a link to the last page
 * (as underlying service does not return a total count).
 * <pre>
 * « First ‹ Previous 41 - 60 of 523 Next › Last »
 * </pre>
 */
public class LightweightPagingNavigator extends Panel {
    public LightweightPagingNavigator(String id, final INavigable navigable) {
        super(id);
        this.navigable = navigable;

        addLink(new LinkInfo("paging-first") {
            int getPage() { return 0; }
            boolean isVisible() { return navigable.getCurrentPage() > 1; }
        });

        addLink(new LinkInfo("paging-previous") {
            int getPage() { return navigable.getCurrentPage() - 1; }
            boolean isVisible() { return navigable.getCurrentPage() > 0; }
        });

        addLabel(new LabelInfo("paging-from") {
            int getPage() {
                return (navigable.getCurrentPageItemsCount() == 0 && navigable.getCurrentPage() == 0)
                        ? 0 : navigable.getCurrentPage() * navigable.getItemsPerPage() + 1;
            }
        });

        addLabel(new LabelInfo("paging-to") {
            int getPage() {
                if (navigable.getCurrentPageItemsCount() == 0) {
                    return (navigable.getCurrentPage()+1) * navigable.getItemsPerPage() + navigable.getCurrentPageItemsCount();
                }
                return navigable.getCurrentPage() * navigable.getItemsPerPage() + navigable.getCurrentPageItemsCount();
            }
        });

        addLink(new LinkInfo("paging-next") {
            int getPage() { return navigable.getCurrentPage() + 1; }
            boolean isVisible() { return navigable.hasNextPage() ; }
        });
    }

    private void addLabel(final LabelInfo aInfo) {
        String id = aInfo.getId();

        Label label = new Label(id, new LoadableDetachableModel<String>() {
            protected String load() {
                return String.valueOf(aInfo.getPage());
            }
        });
        add(label);
    }

    private void addLink(final LinkInfo info) {
        String id = info.getId();
        // synk link
        Link<String> link = new Link<String>(id) {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean isVisible() {
                return info.isVisible();
            }

            public void onClick() {
                navigable.setCurrentPage(info.getPage());
            }
        };
        add(link);

    }

    private abstract class LabelInfo implements Serializable {
        public LabelInfo(String id) {
            this.id = id;
        }

        String getId() {
            return id;
        }

        abstract int getPage();

        private final String id;
    }

    private abstract class LinkInfo implements Serializable {

        public LinkInfo(String id) {
            this.id = id;
        }

        String getId() {
            return id;
        }

        abstract int getPage();

        abstract boolean isVisible();

        private final String id;
    }

    private final INavigable navigable;
}
