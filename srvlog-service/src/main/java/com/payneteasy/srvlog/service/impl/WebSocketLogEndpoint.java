package com.payneteasy.srvlog.service.impl;

import com.payneteasy.srvlog.data.LogData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/ws-log")
public class WebSocketLogEndpoint {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketLogEndpoint.class);

    private static final Set<WebSocketLogEndpoint> connections = new CopyOnWriteArraySet<>();

    private Session session;
    private String subscriptionHost;
    private String subscriptionProgram;

    @OnOpen
    public void openConnection(Session session) {
        this.session = session;
        connections.add(this);
    }

    @OnClose
    public void closeConnection() {
        connections.remove(this);
    }

    @OnMessage
    public void onMessage(String message) {
        //TODO impl
    }

    @OnError
    public void onError(Throwable e) {
        logger.error("Error during websocket log communication", e);
    }

    public static void broadcastLogData(LogData logData) {

        String host = logData.getHost();
        String program = logData.getProgram();

        //TODO write creation of logDataText var
        String logDataText = "";

        for (WebSocketLogEndpoint connection : connections) {

            if (host.equalsIgnoreCase(connection.subscriptionHost)
                    && program.equalsIgnoreCase(connection.subscriptionProgram)) {

                try {
                    synchronized (connection) {
                        connection.session.getBasicRemote().sendText(logDataText);
                    }
                } catch (IOException e) {
                    connections.remove(connection);
                    try {
                        connection.session.close();
                    } catch (IOException ce) {
                        logger.warn("Error while closing websocket conversation due to IOException", ce);
                    }
                }
            }
        }
    }

    private static class LogSubscriptionRequest {

        private String host;
        private String program;

        public String getHost() {
            return host;
        }

        public String getProgram() {
            return program;
        }
    }
}
