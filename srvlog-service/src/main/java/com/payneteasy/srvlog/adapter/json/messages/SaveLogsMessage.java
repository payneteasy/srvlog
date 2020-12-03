package com.payneteasy.srvlog.adapter.json.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.beans.ConstructorProperties;

public class SaveLogsMessage {

    @JsonProperty("time")     private final long    time;
    @JsonProperty("program")  private final String  program;
    @JsonProperty("facility") private final Integer facility;
    @JsonProperty("severity") private final Integer severity;
    @JsonProperty("message")  private final String  message;

    @ConstructorProperties({"time", "program", "facility", "severity", "message"})
    public SaveLogsMessage(long time, String program, Integer facility, Integer severity, String message) {
        this.time = time;
        this.program = program;
        this.facility = facility;
        this.severity = severity;
        this.message = message;
    }

    public long getTime() {
        return time;
    }

    public String getProgram() {
        return program;
    }

    public Integer getFacility() {
        return facility;
    }

    public Integer getSeverity() {
        return severity;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "SaveLogsMessage{" +
                "time=" + time +
                ", program='" + program + '\'' +
                ", facility=" + facility +
                ", severity=" + severity +
                ", message='" + message + '\'' +
                '}';
    }
}
