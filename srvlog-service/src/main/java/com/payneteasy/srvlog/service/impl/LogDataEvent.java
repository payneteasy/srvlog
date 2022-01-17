package com.payneteasy.srvlog.service.impl;

import com.lmax.disruptor.EventFactory;
import com.payneteasy.srvlog.data.LogData;

public class LogDataEvent implements EventFactory<LogDataEvent> {

    private LogData logData;

    public LogData getLogData() {
        return logData;
    }

    public void setLogData(LogData logData) {
        this.logData = logData;
    }

    @Override
    public LogDataEvent newInstance() {
        return new LogDataEvent();
    }
}
