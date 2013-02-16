package com.payneteasy.srvlog.wicket.component.navigation;

import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

/**
 * Date: 16.01.13
 * Time: 16:27
 */
public abstract class UncountablyPageableListView<T extends Serializable> extends RefreshingView<T> implements IUncountablyPageable {

    public static final int DEFAULT_NUM_ROWS = 25;
    private PageableDataProvider<T> pageableDataProvider;
    private int currentPage;
    private int itemsPerPage = Integer.MAX_VALUE;
    private boolean hasNextPage;
    private int itemsFetched;

    public UncountablyPageableListView(String id, PageableDataProvider<T> pageableDataProvider) {
        this(id, pageableDataProvider, DEFAULT_NUM_ROWS);
        this.pageableDataProvider = pageableDataProvider;
    }

    public UncountablyPageableListView(String id, PageableDataProvider<T> pageableDataProvider, int itemsPerPage) {
        super(id);

        this.pageableDataProvider = pageableDataProvider;
        this.itemsPerPage = itemsPerPage;
        this.setCurrentPage(0);
    }

    @Override
    protected Iterator<IModel<T>> getItemModels() {
        int offset = getCurrentPage() * itemsPerPage;
        Collection<T> collection = pageableDataProvider.load(offset, itemsPerPage + 1);
        itemsFetched = collection.size();
        hasNextPage = (itemsFetched - itemsPerPage) > 0;
        Iterator<IModel<T>> iterator = new ModelIterator<T>(collection, itemsPerPage);
        return iterator;
    }


    @Override
    public int getCurrentPage() {
        return currentPage;
    }

    @Override
    public boolean hasNextPage() {
        return hasNextPage;
    }

    @Override
    public void setCurrentPage(int page) {
        if (currentPage != page) {
            addStateChange(); //TODO I have no idea what is it for. Just copied from my colleague's code
        }
        currentPage = page;
    }

    public void setItemsPerPage(int itemsPerPage) {
        if(this.itemsPerPage != itemsPerPage){
            addStateChange(); //TODO I have no idea what is it for. Just copied from my colleague's code
        }
        this.itemsPerPage = itemsPerPage;
    }

    @Override
    public int getFromRow() {
        return (currentPage * itemsPerPage + 1);
    }

    @Override
    public int getToRow() {
        return (getFromRow() - 1) + Math.min(itemsFetched, itemsPerPage);
    }

    @Override
    protected void onDetach() {
        pageableDataProvider.detach();
        super.onDetach();
    }

    public boolean isEmpty() {
        return !(getToRow() > 0);
    }

    private static final class ModelIterator<T extends Serializable> implements Iterator<IModel<T>> {
        private final Iterator<? extends T> items;
        private final int max;
        private int index;

        ModelIterator(Collection<T> collection, int count) {
            max = count;
            this.items = collection.iterator();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public boolean hasNext() {
            return items != null && items.hasNext() && (index < max);
        }

        public IModel<T> next() {
            index++;
            return new Model<T>(items.next());
        }
    }

}
