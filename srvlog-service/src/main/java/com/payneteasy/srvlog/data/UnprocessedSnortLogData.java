package com.payneteasy.srvlog.data;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Id;

/**
 * Data transfer object.
 * This class uses to save raw message from barnyard2 syslog_full output plugin.
 *
 * @author imenem
 */
public class UnprocessedSnortLogData {
    private long id;
    private Date date;
    private String identifier;
    private String message;

    @Id
    @Column(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    @Column(name = "message")
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
