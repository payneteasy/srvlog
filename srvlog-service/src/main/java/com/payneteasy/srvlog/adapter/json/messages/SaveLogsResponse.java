package com.payneteasy.srvlog.adapter.json.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.beans.ConstructorProperties;
import java.util.UUID;

public class SaveLogsResponse {

    private final String         requestId;
    private final SaveLogsStatus status;
    private final String         errorMessage;
    private final String         errorId;

    @ConstructorProperties({"requestId", "status", "errorMessage", "errorId"})
    private SaveLogsResponse(String requestId, SaveLogsStatus status, String errorMessage, String errorId) {
        this.requestId = requestId;
        this.status = status;
        this.errorMessage = errorMessage;
        this.errorId = errorId;
    }

    public static SaveLogsResponse error(String aRequestId, String aErrorMessage) {
        return new SaveLogsResponse(aRequestId, SaveLogsStatus.ERROR, aErrorMessage, UUID.randomUUID().toString());
    }

    public static SaveLogsResponse success(String aRequestId) {
        return new SaveLogsResponse(aRequestId, SaveLogsStatus.SUCCESS, null, null);
    }

    @JsonProperty("requestId" )
    public String getRequestId() {
        return requestId;
    }

    @JsonProperty("status" )
    public SaveLogsStatus getStatus() {
        return status;
    }

    @JsonProperty("errorMessage" )
    public String getErrorMessage() {
        return errorMessage;
    }

    @JsonProperty("errorId")
    public String getErrorId() {
        return errorId;
    }
}
