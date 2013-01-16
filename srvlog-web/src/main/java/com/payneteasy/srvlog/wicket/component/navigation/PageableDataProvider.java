package com.payneteasy.srvlog.wicket.component.navigation;

import org.apache.wicket.model.IDetachable;

import java.util.Collection;

/**
 * Date: 16.01.13
 * Time: 16:57
 */
public abstract class PageableDataProvider<T> implements IDetachable {

    public abstract Collection<T> load(int offset, int limit);

    @Override
    public void detach() {
        // ignore it
    }
}
