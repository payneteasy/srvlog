package com.payneteasy.srvlog.service.impl;

import com.payneteasy.srvlog.data.LogData;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

class InMemoryLogList {

    private final List<LogData> logList;

    private final int maxCapacity;

    public InMemoryLogList(int maxCapacity) {
        this.logList = new CopyOnWriteArrayList<>();
        this.maxCapacity = maxCapacity;
    }

    public boolean add(LogData logData) {
        if (logList.size() >= maxCapacity) {
            do {
                logList.remove(0);
            } while (logList.size() > maxCapacity - 1);
        }
        logList.add(logData);
        return true;
    }

    public LogData getFirst() {
        return logList.get(0);
    }

    public LogData getLast() {
        return logList.get(logList.size() - 1);
    }

    public int size() {
        return logList.size();
    }
}
