package com.payneteasy.srvlog.wicket.component.navigation;

/**
 * Date: 16.01.13
 * Time: 16:42
 */
public interface IUncountablyPageable {

    int getCurrentPage();

    boolean hasNextPage();

    void setCurrentPage(int page);

}
