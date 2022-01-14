package com.payneteasy.srvlog.websocket;

import com.payneteasy.srvlog.service.ILogBroadcastingService;
import com.payneteasy.srvlog.service.impl.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/ws-log", configurator = ServletAwareConfig.class)
public class WebSocketLogEndpoint {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketLogEndpoint.class);

    static final String LOG_BROADCASTING_SERVICE_KEY = "logBroadcastingService";

    private ILogBroadcastingService logBroadcastingService;

    @OnOpen
    public void openConnection(Session session, EndpointConfig config) {
        this.logBroadcastingService = (ILogBroadcastingService) config.getUserProperties().get(LOG_BROADCASTING_SERVICE_KEY);
        logBroadcastingService.saveBroadcastingSession(session, Subscription.initialState());
    }

    @OnClose
    public void closeConnection(Session session) {
        logBroadcastingService.removeBroadcastingSession(session);
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        logBroadcastingService.handleLogBroadcastingRequest(session, message);
    }

    @OnError
    public void onError(Session session, Throwable e) {
        logBroadcastingService.removeBroadcastingSession(session);
        logger.error("Error during web socket log communication", e);
    }
}
