package com.payneteasy.srvlog.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.payneteasy.srvlog.data.LogData;
import com.payneteasy.srvlog.service.ILogBroadcastingService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReference;

@Service("logBroadcastingService")
public class LogBroadcastingServiceImpl implements ILogBroadcastingService {

    private static final Logger logger = LoggerFactory.getLogger(LogBroadcastingServiceImpl.class);

    private static final ObjectMapper jsonMapper = new ObjectMapper();

    private final ConcurrentMap<Session, AtomicReference<Subscription>> subscriptionStorage = new ConcurrentHashMap<>();

    private final ConcurrentMap<String, ConcurrentMap<String, InMemoryLogStorage>> logStorage = new ConcurrentHashMap<>();

    private static final int DEFAULT_PROGRAM_LOG_STORAGE_CAPACITY = 1000;

    private final int programLogStorageCapacity;

    @Autowired
    public LogBroadcastingServiceImpl(@Value("${programLogStorageCapacity}") int programLogStorageCapacity) {
        this.programLogStorageCapacity = programLogStorageCapacity > 0 ?
                programLogStorageCapacity : DEFAULT_PROGRAM_LOG_STORAGE_CAPACITY;
    }

    @Override
    public List<String> getHostNameList() {
        return new ArrayList<>(logStorage.keySet());
    }

    @Override
    public List<String> getProgramNameList() {

        Set<String> programNamesSet = new HashSet<>();

        for (ConcurrentMap<String, InMemoryLogStorage> hostLogStorage : logStorage.values()) {
            programNamesSet.addAll(hostLogStorage.keySet());
        }

        return new ArrayList<>(programNamesSet);
    }

    @Override
    public List<LogData> getLogDataListByHostAndProgram(String host, String program) {

        ConcurrentMap<String, InMemoryLogStorage> hostLogStorage = logStorage.get(host);
        if (Objects.nonNull(hostLogStorage)) {
            InMemoryLogStorage programLogStorage = hostLogStorage.get(program);

            return Objects.nonNull(programLogStorage) ? programLogStorage.asLogList() : Collections.emptyList();
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public void saveBroadcastingSession(Session session, Subscription subscription) {
        subscriptionStorage.put(session, new AtomicReference<>(subscription));
    }

    @Override
    public void removeBroadcastingSession(Session session) {
        subscriptionStorage.remove(session);
    }

    @Override
    public void handleLogBroadcastingRequest(Session session, String requestText) {

        try {
            LogBroadcastingRequest logBroadcastingRequest = jsonMapper.readValue(requestText, LogBroadcastingRequest.class);
            Subscription.State subscriptionState = Subscription.State.valueOf(logBroadcastingRequest.getSubscriptionState());

            if (Subscription.State.INITIAL.equals(subscriptionState) || Subscription.State.INITIAL
                    .equals(subscriptionStorage.get(session).get().getSubscriptionState())) {

                String host = logBroadcastingRequest.getHost();
                String program = logBroadcastingRequest.getProgram();

                if (StringUtils.isEmpty(host) || StringUtils.isEmpty(program)) {

                    LogBroadcastingResponse incorrectRequestParametersResponse = new LogBroadcastingResponse(
                            false, Collections.emptyList(), "Incorrect request parameters"
                    );
                    String incorrectRequestParametersResponseJson = jsonMapper.writeValueAsString(incorrectRequestParametersResponse);

                    session.getBasicRemote().sendText(incorrectRequestParametersResponseJson);

                    return;
                }

                List<LogData> logDataList = getLogDataListByHostAndProgram(host, program);
                LogBroadcastingResponse logBroadcastingResponse = new LogBroadcastingResponse(true, logDataList, null);
                String logBroadcastingResponseJson = jsonMapper.writeValueAsString(logBroadcastingResponse);

                session.getBasicRemote().sendText(logBroadcastingResponseJson);

                subscriptionStorage.get(session).set(new Subscription(
                        host,
                        program,
                        Subscription.State.ONLINE_BROADCASTING
                    )
                );
            }

        } catch (Exception e) {

            String errorId = UUID.randomUUID().toString();
            logger.error(String.format("Error while handling web socket message. Error id [%s]", errorId), e);

            LogBroadcastingResponse unsuccessfulResponse = new LogBroadcastingResponse(
                    false, Collections.emptyList(), String.format("Error while handling web socket message on server side. Error id [%s]", errorId)
            );

            try {
                String unsuccessfulResponseJson = jsonMapper.writeValueAsString(unsuccessfulResponse);
                session.getBasicRemote().sendText(unsuccessfulResponseJson);
            } catch (IOException e2) {
                logger.error("Error while sending web socket unsuccessful log subscription response", e2);
            }
        }
    }

    @Override
    public void handleReceivedLogData(LogData logData) {

        String host = logData.getHost();
        String program = logData.getProgram();

        if (Objects.isNull(host) || Objects.isNull(program)) return;

        ConcurrentMap<String, InMemoryLogStorage> hostLogStorage = logStorage.computeIfAbsent(
                host, h -> new ConcurrentHashMap<>()
        );

        InMemoryLogStorage programLogList = hostLogStorage.computeIfAbsent(
                program, p -> new InMemoryLogStorage(programLogStorageCapacity)
        );

        if (programLogList.add(logData)) {
            broadcastLogDataToSubscribers(logData);
        }
    }

    public void broadcastLogDataToSubscribers(LogData logData) {

        String host = logData.getHost();
        String program = logData.getProgram();

        LogBroadcastingResponse logSubscriptionResponse = new LogBroadcastingResponse(
                true, Collections.singletonList(logData), null
        );

        String logDataText = null;
        try {
            logDataText = jsonMapper.writeValueAsString(logSubscriptionResponse);
        } catch (JsonProcessingException e) {
            logger.error("Error while writing web socket json log data response", e);
        }

        Iterator<Map.Entry<Session, AtomicReference<Subscription>>> subscriptionIterator =
                subscriptionStorage.entrySet().iterator();

        while (subscriptionIterator.hasNext()) {
            Map.Entry<Session, AtomicReference<Subscription>> subscriptionEntry = subscriptionIterator.next();

            if (subscriptionEntry.getValue().get().isBroadcastCandidateFor(host, program)) {
                try {
                    synchronized (subscriptionEntry.getKey()) {
                        subscriptionEntry.getKey().getBasicRemote().sendText(logDataText);
                    }
                } catch (IOException e) {
                    subscriptionIterator.remove();
                    try {
                        subscriptionEntry.getKey().close();
                    } catch (IOException ce) {
                        logger.warn("Error while closing web socket conversation due to IOException", ce);
                    }
                }
            }
        }
    }

    private static class LogBroadcastingRequest {

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

    private static class LogBroadcastingResponse {

        private final boolean success;
        private final List<LogData> logDataList;
        private final String errorMessage;

        public LogBroadcastingResponse(boolean success, List<LogData> logDataList, String errorMessage) {
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
