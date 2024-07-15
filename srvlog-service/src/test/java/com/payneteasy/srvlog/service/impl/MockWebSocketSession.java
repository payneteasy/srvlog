package com.payneteasy.srvlog.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.websocket.api.BatchMode;
import org.eclipse.jetty.websocket.api.CloseStatus;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.SuspendToken;
import org.eclipse.jetty.websocket.api.UpgradeRequest;
import org.eclipse.jetty.websocket.api.UpgradeResponse;
import org.eclipse.jetty.websocket.api.WebSocketBehavior;
import org.eclipse.jetty.websocket.api.WriteCallback;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.time.Duration;

public class MockWebSocketSession implements Session {

    private final MockWsRemoteEndpoint remoteEndpoint = new MockWsRemoteEndpoint();

    LogBroadcastingResponse getLogBroadcastingResponse() {
        return remoteEndpoint.getLogBroadcastingResponse();
    }

    @Override
    public void close() {

    }

    @Override
    public void close(CloseStatus closeStatus) {

    }

    @Override
    public void close(int i, String s) {

    }

    @Override
    public void disconnect() {

    }

    @Override
    public SocketAddress getLocalAddress() {
        return null;
    }

    @Override
    public String getProtocolVersion() {
        return null;
    }

    @Override
    public RemoteEndpoint getRemote() {
        return remoteEndpoint;
    }

    @Override
    public SocketAddress getRemoteAddress() {
        return null;
    }

    @Override
    public UpgradeRequest getUpgradeRequest() {
        return null;
    }

    @Override
    public UpgradeResponse getUpgradeResponse() {
        return null;
    }

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public SuspendToken suspend() {
        return null;
    }

    @Override
    public WebSocketBehavior getBehavior() {
        return null;
    }

    @Override
    public Duration getIdleTimeout() {
        return null;
    }

    @Override
    public int getInputBufferSize() {
        return 0;
    }

    @Override
    public int getOutputBufferSize() {
        return 0;
    }

    @Override
    public long getMaxBinaryMessageSize() {
        return 0;
    }

    @Override
    public long getMaxTextMessageSize() {
        return 0;
    }

    @Override
    public long getMaxFrameSize() {
        return 0;
    }

    @Override
    public boolean isAutoFragment() {
        return false;
    }

    @Override
    public void setIdleTimeout(Duration duration) {

    }

    @Override
    public void setInputBufferSize(int i) {

    }

    @Override
    public void setOutputBufferSize(int i) {

    }

    @Override
    public void setMaxBinaryMessageSize(long l) {

    }

    @Override
    public void setMaxTextMessageSize(long l) {

    }

    @Override
    public void setMaxFrameSize(long l) {

    }

    @Override
    public void setAutoFragment(boolean b) {

    }

    public static class MockWsRemoteEndpoint implements RemoteEndpoint {

        private static final ObjectMapper jsonMapper = new ObjectMapper();

        private LogBroadcastingResponse logBroadcastingResponse;

        @Override
        public void sendBytes(ByteBuffer byteBuffer) {

        }

        @Override
        public void sendBytes(ByteBuffer byteBuffer, WriteCallback writeCallback) {

        }

        @Override
        public void sendPartialBytes(ByteBuffer byteBuffer, boolean b) {

        }

        @Override
        public void sendPartialBytes(ByteBuffer byteBuffer, boolean b, WriteCallback writeCallback) {

        }

        @Override
        public void sendString(String text) throws IOException {
            logBroadcastingResponse = jsonMapper.readValue(
                    text,
                    LogBroadcastingResponse.class
            );
        }

        @Override
        public void sendString(String s, WriteCallback writeCallback) {

        }

        @Override
        public void sendPartialString(String s, boolean b) {

        }

        @Override
        public void sendPartialString(String s, boolean b, WriteCallback writeCallback) {

        }

        @Override
        public void sendPing(ByteBuffer byteBuffer) {

        }

        @Override
        public void sendPing(ByteBuffer byteBuffer, WriteCallback writeCallback) {

        }

        @Override
        public void sendPong(ByteBuffer byteBuffer) {

        }

        @Override
        public void sendPong(ByteBuffer byteBuffer, WriteCallback writeCallback) {

        }

        @Override
        public BatchMode getBatchMode() {
            return null;
        }

        @Override
        public void setBatchMode(BatchMode batchMode) {

        }

        @Override
        public int getMaxOutgoingFrames() {
            return 0;
        }

        @Override
        public void setMaxOutgoingFrames(int i) {

        }

        @Override
        public SocketAddress getRemoteAddress() {
            return null;
        }

        @Override
        public void flush() {

        }

        public LogBroadcastingResponse getLogBroadcastingResponse() {
            return logBroadcastingResponse;
        }
    }
}
