package com.payneteasy.srvlog.adapter.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.payneteasy.srvlog.adapter.json.messages.SaveLogsRequest;
import com.payneteasy.srvlog.adapter.json.messages.SaveLogsResponse;

public class SaveLogsJsonProcessor {

    private final ISaveLogsService saveLogsService;
    private final ObjectReader     reader;
    private final ObjectWriter     writer;


    public SaveLogsJsonProcessor(ISaveLogsService saveLogsService) {
        this.saveLogsService = saveLogsService;

        ObjectMapper mapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);

        reader = mapper.readerFor(SaveLogsRequest.class);
        writer = mapper.writerFor(SaveLogsResponse.class);
    }


    public byte[] processJson(String aRemoteIpAddress, byte[] aJson) throws ProcessJsonException {

        SaveLogsRequest  request  = parseRequest(aJson);
        SaveLogsResponse response = saveLogsService.saveLogs(aRemoteIpAddress, request);

        try {
            return writer.writeValueAsBytes(response);
        } catch (JsonProcessingException e) {
            throw new ProcessJsonException(request.getRequestId(), 500, "Cannot write logs", e);
        }
    }

    private SaveLogsRequest parseRequest(byte[] aJson) throws ProcessJsonException {
        try {
            return reader.readValue(aJson);
        } catch (UnrecognizedPropertyException e) {
            throw new ProcessJsonException(405, "Unknown field '" + e.getPropertyName().replace("com.payneteasy.srvlog.adapter.json.messages.", "") + "'", e);
        } catch (JsonParseException |JsonMappingException e) {
            throw new ProcessJsonException(405, "Json: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new ProcessJsonException(405, "Unknown exception while parsing json", e);
        }
    }
}
