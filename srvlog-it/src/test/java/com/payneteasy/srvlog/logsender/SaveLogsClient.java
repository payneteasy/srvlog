package com.payneteasy.srvlog.logsender;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.payneteasy.srvlog.adapter.json.ISaveLogsService;
import com.payneteasy.srvlog.adapter.json.messages.SaveLogsRequest;
import com.payneteasy.srvlog.adapter.json.messages.SaveLogsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

public class SaveLogsClient implements ISaveLogsService {

    private static final Logger LOG = LoggerFactory.getLogger(SaveLogsClient.class);

    private final String       baseUrl;
    private final ObjectReader reader;
    private final ObjectWriter writer;

    public SaveLogsClient(String baseUrl) {
        this.baseUrl = baseUrl;

        ObjectMapper mapper = new ObjectMapper().setSerializationInclusion(NON_NULL);

        reader = mapper.readerFor(SaveLogsResponse.class);
        writer = mapper.writerFor(SaveLogsRequest.class);
    }

    @Override
    public SaveLogsResponse saveLogs(String aRemoteIpAddress, SaveLogsRequest aLogs) {
        return sendJson(baseUrl + "/save-logs", aLogs);
    }

    private <T> T sendJson(String url, Object aRequest) {

        HttpURLConnection connection = prepareConnection(url);
        connection.setRequestProperty("token", "test-token-197dc68c-34e5-11eb-9fbd-7bb165d4566c");
        
        try {
            connection.connect();
        } catch (IOException e) {
            throw new IllegalStateException("Cannot connect to " + url, e);
        }

        try {
            String json = writer.writeValueAsString(aRequest);
            LOG.debug("Sending to {} json: {}", url, json);
            connection.getOutputStream().write(json.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new IllegalStateException("Cannot get output stream for " + url, e);
        }

        int status;
        try {
            status = connection.getResponseCode();
        } catch (IOException e) {
            throw new IllegalStateException("Cannot wait for response code for " + url, e);
        }

        if (status != 200) {
            throw new IllegalStateException("Wrong response code " + status);
        }

        InputStream inputStream;
        try {
            inputStream = status >= 400 ? connection.getErrorStream() : connection.getInputStream();
        } catch (IOException e) {
            throw new IllegalStateException("Cannot get input stream for " + url, e);
        }

        try {
            byte[] body = readAllBytes(inputStream);
            String json = new String(body, StandardCharsets.UTF_8);
            LOG.debug("Received json: {}", json);
            return reader.readValue(json);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read value from " + url, e);
        }

    }

    private HttpURLConnection prepareConnection(String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setReadTimeout(10_000);
            connection.setConnectTimeout(10_000);
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            return connection;
        } catch (IOException e) {
            throw new IllegalStateException("Cannot create connection to " + url, e);
        }
    }

    public static byte[] readAllBytes(InputStream aInputStream) throws IOException {
        List<byte[]> list   = new ArrayList<>();
        byte[]       buffer = new byte[2048];
        int          count;

        while( (count = aInputStream.read(buffer)) >= 0) {
            byte[] bytes = new byte[count];
            System.arraycopy(buffer, 0, bytes, 0, count);
            list.add(bytes);
        }

        int size = 0;
        for (byte[] bytes : list) {
            size += bytes.length;
        }

        byte[] output = new byte[size];
        int position = 0;
        for (byte[] bytes : list) {
            System.arraycopy(bytes, 0, output, position, bytes.length);
            position += bytes.length;
        }
        return output;
    }

}
