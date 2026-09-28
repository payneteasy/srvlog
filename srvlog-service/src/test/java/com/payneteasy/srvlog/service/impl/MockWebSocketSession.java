package com.payneteasy.srvlog.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.websocket.api.Callback;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.UpgradeRequest;
import org.eclipse.jetty.websocket.api.UpgradeResponse;
import org.eclipse.jetty.websocket.api.exceptions.WebSocketTimeoutException;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.function.Predicate;

public class MockWebSocketSession implements Session {

    private static final ObjectMapper jsonMapper = new ObjectMapper();

    private LogBroadcastingResponse logBroadcastingResponse;

    LogBroadcastingResponse getLogBroadcastingResponse() {
        return logBroadcastingResponse;
    }

    @Override
    public void sendText(String text, Callback callback) {
        try {
            logBroadcastingResponse = jsonMapper.readValue(text, LogBroadcastingResponse.class);
            callback.succeed();
        } catch (IOException e) {
            callback.fail(e);
        }
    }

    @Override
    public void demand() {

    }

    @Override
    public void sendBinary(ByteBuffer byteBuffer, Callback callback) {
        callback.succeed();
    }

    @Override
    public void sendPartialBinary(ByteBuffer byteBuffer, boolean last, Callback callback) {
        callback.succeed();
    }

    @Override
    public void sendPartialText(String text, boolean last, Callback callback) {
        callback.succeed();
    }

    @Override
    public void sendPing(ByteBuffer byteBuffer, Callback callback) {
        callback.succeed();
    }

    @Override
    public void sendPong(ByteBuffer byteBuffer, Callback callback) {
        callback.succeed();
    }

    @Override
    public void close(int statusCode, String reason, Callback callback) {
        callback.succeed();
    }

    @Override
    public void disconnect() {

    }

    @Override
    public SocketAddress getLocalSocketAddress() {
        return null;
    }

    @Override
    public SocketAddress getRemoteSocketAddress() {
        return null;
    }

    @Override
    public String getProtocolVersion() {
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
        return true;
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public void addIdleTimeoutListener(Predicate<WebSocketTimeoutException> listener) {

    }

    @Override
    public Duration getIdleTimeout() {
        return null;
    }

    @Override
    public void setIdleTimeout(Duration duration) {

    }

    @Override
    public int getInputBufferSize() {
        return 0;
    }

    @Override
    public void setInputBufferSize(int size) {

    }

    @Override
    public int getOutputBufferSize() {
        return 0;
    }

    @Override
    public void setOutputBufferSize(int size) {

    }

    @Override
    public long getMaxBinaryMessageSize() {
        return 0;
    }

    @Override
    public void setMaxBinaryMessageSize(long size) {

    }

    @Override
    public long getMaxTextMessageSize() {
        return 0;
    }

    @Override
    public void setMaxTextMessageSize(long size) {

    }

    @Override
    public long getMaxFrameSize() {
        return 0;
    }

    @Override
    public void setMaxFrameSize(long size) {

    }

    @Override
    public boolean isAutoFragment() {
        return false;
    }

    @Override
    public void setAutoFragment(boolean autoFragment) {

    }

    @Override
    public int getMaxOutgoingFrames() {
        return 0;
    }

    @Override
    public void setMaxOutgoingFrames(int maxOutgoingFrames) {

    }
}
