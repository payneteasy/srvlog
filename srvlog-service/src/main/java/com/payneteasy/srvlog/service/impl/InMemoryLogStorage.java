package com.payneteasy.srvlog.service.impl;

import com.google.common.annotations.VisibleForTesting;
import com.payneteasy.srvlog.data.LogData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

class InMemoryLogStorage {

    private final LogData[] logDataBuffer;
    private final int bufferSize;
    private int currentLogDataPointer;

    public InMemoryLogStorage(int bufferSize) {
        this.bufferSize = bufferSize;
        this.logDataBuffer = new LogData[bufferSize];
        this.currentLogDataPointer = 0;
    }

    public synchronized boolean add(LogData logData) {

        logDataBuffer[currentLogDataPointer] = logData;

        if (currentLogDataPointer < bufferSize - 1) {
            currentLogDataPointer++;
        } else {
            currentLogDataPointer = 0;
        }

        return true;
    }

    public synchronized List<LogData> asLogList() {

        List<LogData> logDataList = new ArrayList<>();

        for (int i = 0; i < bufferSize; i++) {
            LogData logData = logDataBuffer[i];
            if (Objects.nonNull(logData)) {
                logDataList.add(logData);
            }
        }

        if (logDataList.size() > 1) {
            logDataList.sort(Comparator.comparing(LogData::getDate));
        }

        return logDataList;
    }

    @VisibleForTesting
    public int getPointerValue() {
        return currentLogDataPointer;
    }

    @VisibleForTesting
    public LogData[] getLogDataBuffer() {
        return logDataBuffer;
    }
}
