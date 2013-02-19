package com.payneteasy.srvlog.data;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * Date: 17.02.13 Time: 17:17
 */
public class OssecAlertData implements Serializable {
    private String ossecAlertType;
    private String ossecAlertCount;

    @Column(name = "ossec_alert_type")
    public String getOssecAlertType() {
        return ossecAlertType;
    }

    public void setOssecAlertType(String ossecAlertType) {
        this.ossecAlertType = ossecAlertType;
    }

    @Column(name = "ossec_alert_count")
    public String getOssecAlertCount() {
        return ossecAlertCount;
    }

    public void setOssecAlertCount(String ossecAlertCount) {
        this.ossecAlertCount = ossecAlertCount;
    }

    @Override
    public String toString() {
        return "OssecAlertData{" +
                "ossecAlertType='" + ossecAlertType + '\'' +
                ", ossecAlertCount='" + ossecAlertCount + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OssecAlertData that = (OssecAlertData) o;

        if (ossecAlertCount != null ? !ossecAlertCount.equals(that.ossecAlertCount) : that.ossecAlertCount != null)
            return false;
        if (ossecAlertType != null ? !ossecAlertType.equals(that.ossecAlertType) : that.ossecAlertType != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = ossecAlertType != null ? ossecAlertType.hashCode() : 0;
        result = 31 * result + (ossecAlertCount != null ? ossecAlertCount.hashCode() : 0);
        return result;
    }
}
