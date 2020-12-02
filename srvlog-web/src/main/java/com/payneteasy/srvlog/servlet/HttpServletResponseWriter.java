package com.payneteasy.srvlog.servlet;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.payneteasy.srvlog.adapter.json.messages.SaveLogsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class HttpServletResponseWriter {

    private static final Logger LOG = LoggerFactory.getLogger(HttpServletResponseWriter.class);

    private final HttpServletResponse response;
    private final ObjectWriter        objectWriter;

    public HttpServletResponseWriter(HttpServletResponse response, ObjectWriter objectWriter) {
        this.response = response;
        this.objectWriter = objectWriter;
    }

    public void writeError(int aStatusCode, String aReason, Exception aException) {
        LOG.error(aReason, aException);
        writeError(aStatusCode, aReason);
    }

    public void writeError(int aStatusCode, String aReason) {
        LOG.error(aReason);
        try {
            response.setStatus(aStatusCode);
            response.setContentType("application/json");
            writeJson(SaveLogsResponse.error(null, aReason));
        } catch (Exception e) {
            LOG.error("Cannot write response", e);
        }
    }

    public void writeJson(Object aMessage) {
        response.setContentType("application/json");
        PrintWriter writer;
        try {
            writer = response.getWriter();
        } catch (Exception e) {
            LOG.error("Cannot get writer", e);
            return;
        }

        try {
            objectWriter.writeValue(writer, aMessage);
        } catch (Exception e) {
            LOG.error("Cannot write value", e);
        }
    }
}
