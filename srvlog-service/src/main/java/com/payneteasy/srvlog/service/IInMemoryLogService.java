package com.payneteasy.srvlog.service;

import com.payneteasy.srvlog.data.LogData;

import java.util.List;

public interface IInMemoryLogService {

    void handleReceivedLogData(LogData logData);

    List<LogData> getLogDataListByHostAndProgram(String host, String program);
}
