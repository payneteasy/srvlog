package com.payneteasy.srvlog.service;

import com.payneteasy.srvlog.data.LogData;

public interface IInMemoryLogService {

    void handleReceivedLogData(LogData logData);
}
