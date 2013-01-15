package com.payneteasy.srvlog.wicket.component.repeater;

import java.io.Serializable;

/**
 * Date: 14.01.13 Time: 21:38
 */
public interface INavigable extends Serializable{
    int getCurrentPage();
    void setCurrentPage(int page);
    int getItemsPerPage();
    int getCurrentPageItemsCount();
    boolean hasNextPage();
}
