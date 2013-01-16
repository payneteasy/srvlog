package com.payneteasy.srvlog.wicket.component.navigation.service;

import java.io.Serializable;

/**
 * Date: 16.01.13
 * Time: 13:53
 */
public class FakeData implements Serializable {

    private Long id;

    private String message;

    public FakeData(Long id, String message) {
        this.id = id;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FakeData)) return false;

        FakeData fakeData = (FakeData) o;

        if (!id.equals(fakeData.id)) return false;
        if (!message.equals(fakeData.message)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + message.hashCode();
        return result;
    }
}
