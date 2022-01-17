package com.payneteasy.srvlog.service;

import com.payneteasy.srvlog.data.LogData;

import java.util.List;

public interface IInMemoryLogService {

    List<String> getHostNameList();

    List<String> getProgramNameList();

    List<LogData> getLogDataListByHostAndProgram(String host, String program);

    boolean saveLog(LogData logData);
}
