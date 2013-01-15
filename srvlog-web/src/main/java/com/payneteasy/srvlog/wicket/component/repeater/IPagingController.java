package com.payneteasy.srvlog.wicket.component.repeater;

import org.apache.wicket.model.IModel;

import java.io.Serializable;
import java.util.List;

/**
 * Date: 14.01.13 Time: 21:37
 */
public interface IPagingController<T> extends Serializable{
    IModel<List<T>> getListModel();

    INavigable getNavigable();

    void detach();
}
