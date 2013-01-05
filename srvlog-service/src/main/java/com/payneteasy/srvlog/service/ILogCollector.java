package com.payneteasy.srvlog.service;

import com.payneteasy.srvlog.data.LogData;

/**
 * Date: 03.01.13 Time: 15:04
 */
public interface ILogCollector {

    /**
     * Persists log data
     * @param logData represents log message
     */
    void saveLog(LogData logData);

}
