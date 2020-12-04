package com.payneteasy.srvlog.servlet;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.payneteasy.srvlog.adapter.json.ISaveLogsService;
import com.payneteasy.srvlog.adapter.json.messages.SaveLogsRequest;
import com.payneteasy.srvlog.adapter.json.messages.SaveLogsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SaveLogsServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(SaveLogsServlet.class);

    private ISaveLogsService saveLogsService;
    private String           token;

    private final ObjectReader reader;
    private final ObjectWriter writer;

    public SaveLogsServlet() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        
        reader = mapper.readerFor(SaveLogsRequest.class);
        writer = mapper.writerFor(SaveLogsResponse.class);
    }


    @Override
    public void init() throws ServletException {
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.findWebApplicationContext(getServletContext());
        saveLogsService = webApplicationContext.getBean(ISaveLogsService.class);
        if(saveLogsService == null) {
            throw new ServletException("No ILogCollector found");
        }

        token = getServletContext().getInitParameter("save-logs-token").trim();

        LOG.info("Initialized save-logs servlet ({})", getServletConfig().getServletName());
    }

    @Override
    protected void doPost(HttpServletRequest aRequest, HttpServletResponse aResponse) {
        HttpServletResponseWriter servletWriter = new HttpServletResponseWriter(aResponse, writer);

        if(!token.equals(aRequest.getHeader("token"))) {
            servletWriter.writeError(HttpServletResponse.SC_UNAUTHORIZED, "Wrong access token");
            return;
        }


        SaveLogsRequest saveRequest;
        try {
            saveRequest = reader.readValue(aRequest.getReader());
        } catch (UnrecognizedPropertyException e) {
            servletWriter.writeError(HttpServletResponse.SC_BAD_REQUEST, "Unknown field '" + e.getPropertyName().replace("com.payneteasy.srvlog.adapter.json.messages.", "") + "'", e);
            return;
        } catch (JsonParseException|JsonMappingException e) {
            servletWriter.writeError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage(), e);
            return;
        } catch (Exception e) {
            servletWriter.writeError(HttpServletResponse.SC_BAD_REQUEST, "Cannot parse input request", e);
            return;
        }

        SaveLogsResponse saveResponse;
        try {
            saveResponse = saveLogsService.saveLogs(aRequest.getRemoteAddr(), saveRequest);
        } catch (Exception e) {
            LOG.error("Cannot save logs", e);
            saveResponse = SaveLogsResponse.error(saveRequest.getRequestId(), "Cannot save logs");
        }

        servletWriter.writeJson(saveResponse);

    }
}
