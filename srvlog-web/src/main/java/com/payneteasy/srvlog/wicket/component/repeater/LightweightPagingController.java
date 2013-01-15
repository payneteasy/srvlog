package com.payneteasy.srvlog.wicket.component.repeater;

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import java.util.List;

/**
 * Encapsulates logic which relates to a lightweight paging where 'count'
 * methods are not used. With such a paging, only 'list' method is called. It
 * requests more records than it should to know is there a next page or not.
 * In such a pager, there are 'First' and 'Back' links as well as 'Next' link,
 * but there're no links to pages beyond that next page (there's even no 'Last'
 * link).
 *
 * @param <T>
 */
public class LightweightPagingController<T> implements IPagingController<T> {

    private final IModel<List<T>> listWithExtraElementModel;
    //private final IModel<List<T>> listModel;
    private final INavigable navigable;

    private int currentPage = 0;

    public LightweightPagingController(final IPagingLister<T> lister, final PositionOptions positionOptions) {
        listWithExtraElementModel = new LoadableDetachableModel<List<T>>() {
            @Override
            protected List<T> load() {
                List<T> result = lister.list(new IPositionOptions() {
                    @Override
                    public int getItemsPerPage() {
                        // requesting one additional row so we'll know if
                        // there's a next page
                        return positionOptions.getItemsPerPage() + 1;
                    }

                    @Override
                    public int getFromRow() {
                        return positionOptions.getFromRow();
                    }
                });
                if (result.size() > positionOptions.getItemsPerPage()) {
                    return result.subList(0, result.size() - 1);
                } else {
                    return result;
                }
            }


        };
//        listModel = new AbstractReadOnlyModel<List<T>>() {
//            @Override
//            public List<T> getObject() {
//                // this discards the extra element so it's not displayed
//                List<T> list = listWithExtraElementModel.getObject();
//                if (list.size() > positionOptions.getItemsPerPage()) {
//                    return list.subList(0, list.size() - 1);
//                } else {
//                    return list;
//                }
//            }
//
//            @Override
//            public void detach() {
//                super.detach();
//                listWithExtraElementModel.detach();
//            }
//        };
        navigable = new INavigable() {

            @Override
            public int getCurrentPage() {
                return currentPage;
            }

            @Override
            public void setCurrentPage(int page) {
                currentPage = page;
                positionOptions.setFromRow(page * positionOptions.getItemsPerPage());
                //LightweightPagingController.this.detach();
            }

            @Override
            public int getItemsPerPage() {
                return positionOptions.getItemsPerPage();
            }

            @Override
            public int getCurrentPageItemsCount() {
                // there's an implementation convention: when getting a list, we request
                // N + 1 items where N is items per page. If there's that extra element,
                // than there's next page. That's why we don't count the item beyond N.
                return Math.min(listWithExtraElementModel.getObject().size(), positionOptions.getItemsPerPage());
            }

            @Override
            public boolean hasNextPage() {
                // there's an implementation convention: when getting a list, we request
                // N + 1 items where N is items per page. If there's that extra element,
                // than there's next page
                return listWithExtraElementModel.getObject().size() > positionOptions.getItemsPerPage();
            }
        };
    }

    /* (non-Javadoc)
     * @see com.payneteasy.paynet.ui.web.wicket.component.advancedsearch.IPagingController#getListModel()
     */
    @Override
    public IModel<List<T>> getListModel() {
        //return listModel;
        return listWithExtraElementModel;
    }

    /* (non-Javadoc)
     * @see com.payneteasy.paynet.ui.web.wicket.component.advancedsearch.IPagingController#getNavigable()
     */
    @Override
    public INavigable getNavigable() {
        return navigable;
    }

    /* (non-Javadoc)
     * @see com.payneteasy.paynet.ui.web.wicket.component.advancedsearch.IPagingController#detach()
     */
    @Override
    public void detach() {
        listWithExtraElementModel.detach();
    }
}
