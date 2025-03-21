package com.payneteasy.srvlog.adapter.json;

import com.payneteasy.http.server.api.handler.IHttpRequestHandler;
import com.payneteasy.http.server.api.request.HttpRequest;
import com.payneteasy.http.server.api.request.HttpRequestHeaders;
import com.payneteasy.http.server.api.request.HttpRequestLine;
import com.payneteasy.http.server.api.response.HttpResponse;
import com.payneteasy.http.server.api.response.HttpResponseHeaders;
import com.payneteasy.http.server.api.response.HttpResponseMessageBody;
import com.payneteasy.http.server.api.response.HttpResponseStatusLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.UUID;

import static java.util.Collections.emptyList;

public class SaveLogsHttpRequestHandler implements IHttpRequestHandler {

    private static final Logger LOG = LoggerFactory.getLogger(SaveLogsHttpRequestHandler.class);

    private static final HttpResponseMessageBody EMPTY_BODY    = new HttpResponseMessageBody(new byte[0]);
    private static final HttpResponseHeaders     EMPTY_HEADERS = new HttpResponseHeaders(emptyList());
    private static final HttpResponse            NOT_FOUND     = new HttpResponse(new HttpResponseStatusLine(404, "Not found"), EMPTY_HEADERS, EMPTY_BODY);
    private static final HttpResponse            UNAUTHORIZED  = new HttpResponse(new HttpResponseStatusLine(401, "Unauthorized"), EMPTY_HEADERS, EMPTY_BODY);

    private final SaveLogsJsonProcessor jsonProcessor;
    private final String                path;
    private final String                token;

    public SaveLogsHttpRequestHandler(ISaveLogsService aSaveLogsService, String aPath, String aToken) {
        path = aPath;
        token = aToken;
        jsonProcessor = new SaveLogsJsonProcessor(aSaveLogsService);
    }

    @Override
    public HttpResponse handleRequest(HttpRequest aRequest) {
        HttpRequestLine requestLine = aRequest.getRequestLine();

        if (!path.equals(requestLine.getRequestUri())) {
            LOG.warn("Path {} not found", requestLine.getRequestUri());
            return NOT_FOUND;
        }

        if (!isTokenOk(aRequest.getHeaders())) {
            return UNAUTHORIZED;
        }

        try {
            InetSocketAddress remoteAddress = aRequest.getRemoteAddress();
            byte[]            outputBytes   = jsonProcessor.processJson(remoteAddress.getAddress().getHostAddress(), aRequest.getBody().asBytes());
            return new HttpResponse(
                    new HttpResponseStatusLine(200, "OK")
                    , EMPTY_HEADERS
                    , new HttpResponseMessageBody(outputBytes)
            );
        } catch (ProcessJsonException e) {
            return new HttpResponse(
                    new HttpResponseStatusLine(e.getStatusCode(), "Error")
                    , EMPTY_HEADERS
                    , new HttpResponseMessageBody(e.createJson())
            );
        } catch (Exception e) {
            String errorId = UUID.randomUUID().toString();
            LOG.error("Cannot process request. ErrorID = {}", errorId, e);
            return new HttpResponse(
                      new HttpResponseStatusLine(500, "Unknown Error: " + errorId + " - " + e.getMessage())
                    , EMPTY_HEADERS
                    , EMPTY_BODY
            );
        }
    }

    private boolean isTokenOk(HttpRequestHeaders aHeaders) {
        String  requestToken = aHeaders.getString("Token");
        boolean ok           = token.equals(requestToken);
        if (!ok) {
            LOG.warn("Bad token {}", requestToken);
        }
        return ok;
    }
}
