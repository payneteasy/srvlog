package com.payneteasy.srvlog.wicket.component.repeater;

import java.io.Serializable;

/**
 * Date: 14.01.13 Time: 21:34
 */
public interface IPositionOptions extends Serializable {
    /** Display from row */
    public int getFromRow();

    /** Items per page */
    public int getItemsPerPage();
}
