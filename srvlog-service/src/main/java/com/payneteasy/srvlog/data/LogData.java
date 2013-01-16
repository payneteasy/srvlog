package com.payneteasy.srvlog.data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * Date: 04.01.13
 */
public class LogData implements Serializable {
    private Date date;
    private String host;
    private Integer facility;
    private Integer severity;
    private String message;
    private Long id;

    @Column(name = "log_date")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Column(name = "host")
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Column(name = "facility")
    public Integer getFacility() {
        return facility;
    }

    public void setFacility(Integer facility) {
        this.facility = facility;
    }

    @Column(name = "severity")
    public Integer getSeverity() {
        return severity;
    }

    public void setSeverity(Integer severity) {
        this.severity = severity;
    }

    @Column(name = "message")
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    @Column(name = "log_id")
    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LogData logData = (LogData) o;

        if (id != null ? !id.equals(logData.id) : logData.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "LogData{" +
                "date=" + date +
                ", host='" + host + '\'' +
                ", facility=" + facility +
                ", severity=" + severity +
                ", message='" + message + '\'' +
                ", id=" + id +
                '}';
    }
}
