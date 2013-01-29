package com.payneteasy.srvlog.service;

import com.payneteasy.srvlog.data.HostData;
import com.payneteasy.srvlog.data.LogData;

import java.util.Date;
import java.util.List;

/**
 * Date: 03.01.13 Time: 15:04
 */
public interface ILogCollector {

    /**
     * Persists log data
     * @param logData represents log message
     */
    void saveLog(LogData logData);

    List<LogData> loadLatest(int number_of_logs);

    void saveHost(HostData hostData);

    List<HostData> loadHosts();

    List<LogData> search(Date from, Date to, List<Integer> facilities, List<Integer> severities, List<Integer> hosts, String pattern, int offset, int limit) throws IndexerServiceException;

    Long getNumberUnprocessedHosts();

    void saveUnprocessedHosts();
}
