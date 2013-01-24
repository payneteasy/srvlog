package com.payneteasy.srvlog.wicket.component.navigation;

import java.io.Serializable;

/**
 * Date: 16.01.13
 * Time: 16:42
 */
public interface IUncountablyPageable {

    int getCurrentPage();

    boolean hasNextPage();

    void setCurrentPage(int page);

    int getFromRow();

    int getToRow();

    boolean isEmpty();
}
