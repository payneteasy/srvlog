package com.payneteasy.srvlog.wicket.component.repeater;

/**
 * Date: 14.01.13 Time: 21:46
 */
public class PositionOptions implements IPositionOptions{
    @Override
    public int getFromRow() {
        return fromRow;
    }

    public void setFromRow(int fromRow) {
        this.fromRow = fromRow;
    }

    @Override
    public int getItemsPerPage() {
        return itemsPerPage;
    }

    public void setItemsPerPage(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PositionOptions that = (PositionOptions) o;

        if (fromRow != that.fromRow) return false;
        if (itemsPerPage != that.itemsPerPage) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = itemsPerPage;
        result = 31 * result + fromRow;
        return result;
    }

    private int itemsPerPage;
    private int fromRow;
}
