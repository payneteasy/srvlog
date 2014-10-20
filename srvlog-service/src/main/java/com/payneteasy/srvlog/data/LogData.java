package com.payneteasy.srvlog.data;

import com.payneteasy.srvlog.util.ComparisonUtils;

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
    private String program;
    private String hash;
    private int hasSnortLogs;

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

    @Column(name = "program")
    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    @Column(name = "hash")
    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    @Column(name = "has_snort_logs")
    public int getHasSnortLogs() {
        return hasSnortLogs;
    }

    public void setHasSnortLogs(int hasSnortLogs) {
        this.hasSnortLogs = hasSnortLogs;
    }

    public boolean hasSnortLogs() {
        switch (hasSnortLogs) {
            case 1:
                return true;
            case 0:
                return false;
            default:
                throw new RuntimeException("Unsupported value for hasSnortLogs: " + hasSnortLogs);
        }
    }

    public void hasSnortLogs(boolean hasSnortLogs) {
        this.hasSnortLogs = hasSnortLogs ? 1 : 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LogData)) return false;

        LogData logData = (LogData) o;

//        if (!date.equals(logData.date)) return false;
        if (facility != null ? !facility.equals(logData.facility) : logData.facility != null) return false;
        //if (host != null ? !host.equals(logData.host) : logData.host != null) return false;
        if (!ComparisonUtils.hostAddressesAreEqual(host, logData.host)) return false;


        if (id != null ? !id.equals(logData.id) : logData.id != null) return false;
        if (!message.equals(logData.message)) return false;
        if (program != null ? !program.equals(logData.program) : logData.program != null) return false;
        if (severity != null ? !severity.equals(logData.severity) : logData.severity != null) return false;

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
                ", program='" + program + '\'' +
                '}';
    }
}
