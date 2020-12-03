package com.payneteasy.srvlog.adapter.json.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.beans.ConstructorProperties;
import java.util.List;

public class SaveLogsRequest {

    @JsonProperty("requestId") private final String                requestId;
    @JsonProperty("messages" ) private final List<SaveLogsMessage> messages;

    @ConstructorProperties({"requestId", "messages"})
    public SaveLogsRequest(String requestId, List<SaveLogsMessage> messages) {
        this.requestId = requestId;
        this.messages = messages;
    }

    public String getRequestId() {
        return requestId;
    }

    public List<SaveLogsMessage> getMessages() {
        return messages;
    }
}
