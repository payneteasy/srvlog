package com.payneteasy.srvlog.adapter.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.payneteasy.srvlog.adapter.json.messages.SaveLogsResponse;

import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

public class ProcessJsonException extends Exception {

    private static  final ObjectWriter WRITER = new ObjectMapper().setSerializationInclusion(NON_NULL).writerFor(SaveLogsResponse.class);

    private final int    statusCode;
    private final String requestId;

    public ProcessJsonException(int statusCode, String message, Exception aCause) {
        this("server-" + UUID.randomUUID(), statusCode, message, aCause);
    }

    public ProcessJsonException(String aRequestId, int statusCode, String message, Exception aCause) {
        super(message, aCause);
        requestId = aRequestId;
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public byte[] createJson() {
        try {
            SaveLogsResponse response = SaveLogsResponse.error(requestId, getMessage());
            return WRITER.writeValueAsBytes(response);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Cannot write error message", e);
        }
    }
}
