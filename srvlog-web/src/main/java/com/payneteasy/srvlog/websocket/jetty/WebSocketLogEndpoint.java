package com.payneteasy.srvlog.websocket.jetty;

import com.payneteasy.srvlog.service.ILogBroadcastingService;
import com.payneteasy.srvlog.service.impl.Subscription;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.eclipse.jetty.websocket.api.Session;

import java.util.concurrent.CountDownLatch;

public class WebSocketLogEndpoint extends WebSocketAdapter {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketLogEndpoint.class);

    private final ILogBroadcastingService logBroadcastingService;

    private final CountDownLatch closureLatch = new CountDownLatch(1);

    public WebSocketLogEndpoint(ILogBroadcastingService logBroadcastingService) {
        this.logBroadcastingService = logBroadcastingService;
    }

    @Override
    public void onWebSocketConnect(Session session) {
        super.onWebSocketConnect(session);
        logBroadcastingService.saveBroadcastingSession(session, Subscription.initialState());
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        super.onWebSocketClose(statusCode, reason);
        logBroadcastingService.removeBroadcastingSession(getSession());
        closureLatch.countDown();
    }

    @Override
    public void onWebSocketText(String message) {
        super.onWebSocketText(message);
        logBroadcastingService.handleLogBroadcastingRequest(getSession(), message);
    }

    @Override
    public void onWebSocketError(Throwable cause) {
        super.onWebSocketError(cause);
        logBroadcastingService.removeBroadcastingSession(getSession());
        logger.error("Error during web socket log communication", cause);
    }
}
