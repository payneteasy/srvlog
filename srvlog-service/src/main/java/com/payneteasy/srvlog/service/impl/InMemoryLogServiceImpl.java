package com.payneteasy.srvlog.service.impl;

import com.payneteasy.srvlog.data.LogData;
import com.payneteasy.srvlog.service.IInMemoryLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class InMemoryLogServiceImpl implements IInMemoryLogService {

    private static final int DEFAULT_PROGRAM_LOG_STORAGE_CAPACITY = 1000;

    private final ConcurrentMap<String, ConcurrentMap<String, InMemoryLogList>> logStorage = new ConcurrentHashMap<>();
    private final int programLogStorageCapacity;

    @Autowired
    public InMemoryLogServiceImpl(@Value("${programLogStorageCapacity}") int programLogStorageCapacity) {
        this.programLogStorageCapacity = programLogStorageCapacity > 0 ?
                programLogStorageCapacity : DEFAULT_PROGRAM_LOG_STORAGE_CAPACITY;
    }

    public void handleReceivedLogData(LogData logData) {
        if (saveLog(logData)) {
            WebSocketLogEndpoint.broadcastLogData(logData);
        }
    }

    private boolean saveLog(LogData logData) {

        String host = logData.getHost();
        String program = logData.getProgram();

        if (Objects.isNull(host) || Objects.isNull(program)) return false;

        ConcurrentMap<String, InMemoryLogList> hostLogStorage = logStorage.computeIfAbsent(
                host, h -> new ConcurrentHashMap<>()
        );

        InMemoryLogList programLogList = hostLogStorage.computeIfAbsent(
                program, p -> new InMemoryLogList(programLogStorageCapacity)
        );

        return programLogList.add(logData);
    }
}
