package com.payneteasy.srvlog.service.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.payneteasy.srvlog.data.LogData;
import com.payneteasy.srvlog.service.IInMemoryLogService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;

@Component("webSocketEndpoint")
@Scope(scopeName = "websocket")
@ServerEndpoint(WebSocketLogEndpoint.WS_LOG_EPNT_PATH)
public class WebSocketLogEndpoint {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketLogEndpoint.class);

    public static final String WS_LOG_EPNT_PATH = "/ws-log";

    private static final Set<WebSocketLogEndpoint> connections = new CopyOnWriteArraySet<>();

    private static final ObjectMapper jsonMapper = new ObjectMapper();

    private static IInMemoryLogService inMemoryLogService;

    private Session session;
    private volatile String subscriptionHost;
    private volatile String subscriptionProgram;
    private volatile SubscriptionState subscriptionState;

    private enum SubscriptionState {
        INITIAL, ONLINE_BROADCASTING
    }

    @OnOpen
    public void openConnection(Session session) {
        this.session = session;
        connections.add(this);
        this.subscriptionState = WebSocketLogEndpoint.SubscriptionState.INITIAL;
    }

    @OnClose
    public void closeConnection() {
        connections.remove(this);
    }

    @OnMessage
    public void onMessage(String message) {

        try {
            LogSubscriptionRequest logSubscriptionRequest = WebSocketLogEndpoint.jsonMapper.readValue(message, LogSubscriptionRequest.class);
            SubscriptionState subscriptionState = SubscriptionState.valueOf(logSubscriptionRequest.getSubscriptionState());

            if (SubscriptionState.INITIAL.equals(subscriptionState) || SubscriptionState.INITIAL.equals(this.subscriptionState)) {

                String host = logSubscriptionRequest.getHost();
                String program = logSubscriptionRequest.getProgram();

                if (StringUtils.isEmpty(host) || StringUtils.isEmpty(program)) {

                    LogSubscriptionResponse incorrectRequestParametersResponse = new LogSubscriptionResponse(
                            false, Collections.emptyList(), "Incorrect request parameters"
                    );
                    String incorrectRequestParametersResponseJson = jsonMapper.writeValueAsString(incorrectRequestParametersResponse);
                    sendTextThruConnection(this, incorrectRequestParametersResponseJson);

                    return;
                }

                synchronized (this) {
                    this.subscriptionHost = host;
                    this.subscriptionProgram = program;
                    this.subscriptionState = SubscriptionState.INITIAL;
                }

                List<LogData> logDataList = inMemoryLogService.getLogDataListByHostAndProgram(host, program);
                LogSubscriptionResponse logSubscriptionResponse = new LogSubscriptionResponse(true, logDataList, null);
                String logSubscriptionResponseJson = jsonMapper.writeValueAsString(logSubscriptionResponse);
                sendTextThruConnection(this, logSubscriptionResponseJson);

                this.subscriptionState = SubscriptionState.ONLINE_BROADCASTING;
            }

        } catch (Exception e) {

            String errorId = UUID.randomUUID().toString();
            logger.error(String.format("Error while handling web socket message. Error id [%s]", errorId), e);

            LogSubscriptionResponse unsuccessfulResponse = new LogSubscriptionResponse(
                    false, Collections.emptyList(), String.format("Error while handling web socket message on server side. Error id [%s]", errorId)
            );

            try {
                String unsuccessfulResponseJson = jsonMapper.writeValueAsString(unsuccessfulResponse);
                sendTextThruConnection(this, unsuccessfulResponseJson);
            } catch (IOException e2) {
                logger.error("Error while sending web socket unsuccessful log subscription response", e2);
            }
        }
    }

    @OnError
    public void onError(Throwable e) {
        logger.error("Error during web socket log communication", e);
    }

    public static void broadcastLogDataToSubscribers(LogData logData) {

        String host = logData.getHost();
        String program = logData.getProgram();

        LogSubscriptionResponse logSubscriptionResponse = new LogSubscriptionResponse(
                true, Collections.singletonList(logData), null
        );

        String logDataText = null;
        try {
            logDataText = jsonMapper.writeValueAsString(logSubscriptionResponse);
        } catch (JsonProcessingException e) {
            logger.error("Error while writing web socket json log data response", e);
        }

        for (WebSocketLogEndpoint connection : connections) {

            boolean isBroadcastCandidate;

            synchronized (connection) {
                isBroadcastCandidate = WebSocketLogEndpoint.SubscriptionState.ONLINE_BROADCASTING.equals(connection.subscriptionState)
                        && host.equalsIgnoreCase(connection.subscriptionHost)
                        && program.equalsIgnoreCase(connection.subscriptionProgram);
            }

            if (isBroadcastCandidate) {
                try {
                    sendTextThruConnection(connection, logDataText);
                } catch (IOException e) {
                    connections.remove(connection);
                    try {
                        connection.session.close();
                    } catch (IOException ce) {
                        logger.warn("Error while closing web socket conversation due to IOException", ce);
                    }
                }
            }
        }
    }

    private static void sendTextThruConnection(WebSocketLogEndpoint connection, String text) throws IOException {
        synchronized (connection) {
            connection.session.getBasicRemote().sendText(text);
        }
    }

    public void setInMemoryLogService(IInMemoryLogService inMemoryLogService) {
        WebSocketLogEndpoint.inMemoryLogService = inMemoryLogService;
    }

    private static class LogSubscriptionRequest {

        private String host;
        private String program;
        private String subscriptionState;

        public String getHost() {
            return host;
        }

        public String getProgram() {
            return program;
        }

        public String getSubscriptionState() {
            return subscriptionState;
        }
    }

    private static class LogSubscriptionResponse {

        private boolean success;
        private List<LogData> logDataList;
        private String errorMessage;

        public LogSubscriptionResponse(boolean success, List<LogData> logDataList, String errorMessage) {
            this.success = success;
            this.logDataList = logDataList;
            this.errorMessage = errorMessage;
        }

        public boolean isSuccess() {
            return success;
        }

        public List<LogData> getLogDataList() {
            return logDataList;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }
}
