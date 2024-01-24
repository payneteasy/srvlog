package com.payneteasy.srvlog.util;

/**
 * Date: 15.01.13 Time: 14:14
 */
public enum DateRangeType {
    TODAY("TODAY"),
    YESTERDAY("YESTERDAY"),
    THIS_WEEK("THIS_WEEK"),
    LAST_WEEK("LAST_WEEK"),
    THIS_MONTH("THIS_MONTH"),
    LAST_MONTH("LAST_MONTH"),
    EXACTLY_DATE("EXACTLY_DATE"),
    EXACTLY_TIME("EXACTLY_TIME");

    private String typeDisplayName;

    DateRangeType(String typeDisplayName) {
        this.typeDisplayName = typeDisplayName;
    }

    public String getTypeDisplayName() {
        return typeDisplayName;
    }

    public void setTypeDisplayName(String typeDisplayName) {
        this.typeDisplayName = typeDisplayName;
    }
}
