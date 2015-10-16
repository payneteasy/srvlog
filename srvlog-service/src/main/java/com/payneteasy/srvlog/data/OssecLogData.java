package com.payneteasy.srvlog.data;

import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Id;

/**
 * Data transfer object.
 * This class uses to save additional data from ossec message for message searching.
 *
 * @author imenem
 */
public class OssecLogData {
    private Long id;
    private Long logId;
    private Date date;
    private String identifier;
    private String hash;

    @Id
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "log_id")
    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    @Column(name = "date")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Column(name = "identifier")
    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Column(name = "hash")
    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.logId);
        hash = 37 * hash + Objects.hashCode(this.date);
        hash = 37 * hash + Objects.hashCode(this.identifier);
        hash = 37 * hash + Objects.hashCode(this.hash);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final OssecLogData other = (OssecLogData) obj;
        if (!Objects.equals(this.logId, other.logId)) {
            return false;
        }
        if (!Objects.equals(this.date, other.date)) {
            return false;
        }
        if (!Objects.equals(this.identifier, other.identifier)) {
            return false;
        }
        if (!Objects.equals(this.hash, other.hash)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "OssecLogData{" +
            "id=" + id + "," +
            "logId=" + logId + "," +
            "date=" + date + "," +
            "identifier=" + identifier + "," +
            "hash=" + hash
            + "}";
    }

}
