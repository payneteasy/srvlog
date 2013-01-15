package com.payneteasy.srvlog.wicket.component.repeater;

import java.io.Serializable;
import java.util.List;

/**
 * Date: 14.01.13 Time: 21:52
 */
public interface IPagingLister<T> extends Serializable{
    List<T> list(IPositionOptions position);
}
