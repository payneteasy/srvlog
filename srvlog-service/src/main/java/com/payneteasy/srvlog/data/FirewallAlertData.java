package com.payneteasy.srvlog.data;

import java.io.Serializable;

/**
 * Date: 17.02.13 Time: 17:15
 */
public class FirewallAlertData implements Serializable {
    private String alertClass;
    private String alertCount;

    public String getAlertClass() {
        return alertClass;
    }

    public void setAlertClass(String alertClass) {
        this.alertClass = alertClass;
    }

    public String getAlertCount() {
        return alertCount;
    }

    public void setAlertCount(String alertCount) {
        this.alertCount = alertCount;
    }

    @Override
    public String toString() {
        return "FirewallAlertData{" +
                "alertClass='" + alertClass + '\'' +
                ", alertCount='" + alertCount + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FirewallAlertData that = (FirewallAlertData) o;

        if (alertClass != null ? !alertClass.equals(that.alertClass) : that.alertClass != null) return false;
        if (alertCount != null ? !alertCount.equals(that.alertCount) : that.alertCount != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = alertClass != null ? alertClass.hashCode() : 0;
        result = 31 * result + (alertCount != null ? alertCount.hashCode() : 0);
        return result;
    }
}
