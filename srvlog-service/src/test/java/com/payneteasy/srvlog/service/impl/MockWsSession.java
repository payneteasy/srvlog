package com.payneteasy.srvlog.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.websocket.CloseReason;
import javax.websocket.Extension;
import javax.websocket.MessageHandler;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.net.URI;
import java.nio.ByteBuffer;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MockWsSession implements Session {

    private final MockBasicRemote basicRemote = new MockBasicRemote();

    LogBroadcastingResponse getLogBroadcastingResponse() {
        return basicRemote.getLogBroadcastingResponse();
    }

    @Override
    public WebSocketContainer getContainer() {
        return null;
    }

    @Override
    public void addMessageHandler(MessageHandler handler) throws IllegalStateException {

    }

    @Override
    public <T> void addMessageHandler(Class<T> clazz, MessageHandler.Whole<T> handler) {

    }

    @Override
    public <T> void addMessageHandler(Class<T> clazz, MessageHandler.Partial<T> handler) {

    }

    @Override
    public Set<MessageHandler> getMessageHandlers() {
        return null;
    }

    @Override
    public void removeMessageHandler(MessageHandler handler) {

    }

    @Override
    public String getProtocolVersion() {
        return null;
    }

    @Override
    public String getNegotiatedSubprotocol() {
        return null;
    }

    @Override
    public List<Extension> getNegotiatedExtensions() {
        return null;
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public long getMaxIdleTimeout() {
        return 0;
    }

    @Override
    public void setMaxIdleTimeout(long milliseconds) {

    }

    @Override
    public void setMaxBinaryMessageBufferSize(int length) {

    }

    @Override
    public int getMaxBinaryMessageBufferSize() {
        return 0;
    }

    @Override
    public void setMaxTextMessageBufferSize(int length) {

    }

    @Override
    public int getMaxTextMessageBufferSize() {
        return 0;
    }

    @Override
    public RemoteEndpoint.Async getAsyncRemote() {
        return null;
    }

    @Override
    public RemoteEndpoint.Basic getBasicRemote() {
        return basicRemote;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void close() {

    }

    @Override
    public void close(CloseReason closeReason) {

    }

    @Override
    public URI getRequestURI() {
        return null;
    }

    @Override
    public Map<String, List<String>> getRequestParameterMap() {
        return null;
    }

    @Override
    public String getQueryString() {
        return null;
    }

    @Override
    public Map<String, String> getPathParameters() {
        return null;
    }

    @Override
    public Map<String, Object> getUserProperties() {
        return null;
    }

    @Override
    public Principal getUserPrincipal() {
        return null;
    }

    @Override
    public Set<Session> getOpenSessions() {
        return null;
    }

    static class MockBasicRemote implements RemoteEndpoint.Basic {

        private static final ObjectMapper jsonMapper = new ObjectMapper();

        private LogBroadcastingResponse logBroadcastingResponse;

        @Override
        public void sendText(String text) throws IOException {
            logBroadcastingResponse = jsonMapper.readValue(
                    text,
                    LogBroadcastingResponse.class
            );
        }

        @Override
        public void sendBinary(ByteBuffer data) {

        }

        @Override
        public void sendText(String partialMessage, boolean isLast) {

        }

        @Override
        public void sendBinary(ByteBuffer partialByte, boolean isLast) {

        }

        @Override
        public OutputStream getSendStream() {
            return null;
        }

        @Override
        public Writer getSendWriter() {
            return null;
        }

        @Override
        public void sendObject(Object data) {

        }

        @Override
        public void setBatchingAllowed(boolean allowed) {

        }

        @Override
        public boolean getBatchingAllowed() {
            return false;
        }

        @Override
        public void flushBatch() {

        }

        @Override
        public void sendPing(ByteBuffer applicationData) throws IllegalArgumentException {

        }

        @Override
        public void sendPong(ByteBuffer applicationData) throws IllegalArgumentException {

        }

        public LogBroadcastingResponse getLogBroadcastingResponse() {
            return logBroadcastingResponse;
        }
    }
}