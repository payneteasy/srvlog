package com.payneteasy.srvlog.service.impl;

import com.payneteasy.srvlog.data.LogData;
import com.payneteasy.srvlog.service.IInMemoryLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service("inMemoryLogService")
public class InMemoryLogServiceImpl implements IInMemoryLogService {

    private static final int DEFAULT_PROGRAM_LOG_STORAGE_CAPACITY = 1000;

    private final ConcurrentMap<String, ConcurrentMap<String, InMemoryLogStorage>> logStorage = new ConcurrentHashMap<>();
    private final int programLogStorageCapacity;

    @Autowired
    public InMemoryLogServiceImpl(@Value("${programLogStorageCapacity}") int programLogStorageCapacity) {
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
    public void handleReceivedLogData(LogData logData) {
        if (saveLog(logData)) {
            WebSocketLogEndpoint.broadcastLogDataToSubscribers(logData);
        }
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

    private boolean saveLog(LogData logData) {

        String host = logData.getHost();
        String program = logData.getProgram();

        if (Objects.isNull(host) || Objects.isNull(program)) return false;

        ConcurrentMap<String, InMemoryLogStorage> hostLogStorage = logStorage.computeIfAbsent(
                host, h -> new ConcurrentHashMap<>()
        );

        InMemoryLogStorage programLogList = hostLogStorage.computeIfAbsent(
                program, p -> new InMemoryLogStorage(programLogStorageCapacity)
        );

        return programLogList.add(logData);
    }
}
