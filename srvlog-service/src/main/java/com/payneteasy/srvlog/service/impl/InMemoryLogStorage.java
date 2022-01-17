package com.payneteasy.srvlog.service.impl;

import com.payneteasy.srvlog.data.LogData;

import java.util.Arrays;
import java.util.List;

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

        if (currentLogDataPointer == bufferSize - 1) {
            currentLogDataPointer = 0;
        } else {
            currentLogDataPointer++;
        }

        return true;
    }

    public synchronized List<LogData> asLogList() {
        return Arrays.asList(logDataBuffer);
    }
}
