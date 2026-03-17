package com.payneteasy.srvlog.service.impl;

import com.payneteasy.srvlog.adapter.syslog.OssecSnortMessage;
import com.payneteasy.srvlog.data.*;
import com.payneteasy.srvlog.service.ILogCollector;
import com.payneteasy.srvlog.service.IndexerServiceException;
import com.payneteasy.srvlog.service.condition.MockModeCondition;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Mock ILogCollector for visual UI testing without database.
 * Returns sample data so Bootstrap layout can be verified.
 */
@Service
@Conditional(MockModeCondition.class)
@Primary
public class MockLogCollector implements ILogCollector {

    private static final List<HostData> SAMPLE_HOSTS = createSampleHosts();
    private static final List<LogData> SAMPLE_LOGS = createSampleLogs(25);

    @Override
    public void saveLog(LogData logData) {
        // no-op
    }

    @Override
    public List<LogData> loadLatest(int numberOfLogs, Long hostId) {
        int limit = Math.min(numberOfLogs, SAMPLE_LOGS.size());
        return new ArrayList<>(SAMPLE_LOGS.subList(0, limit));
    }

    @Override
    public void saveHost(HostData hostData) {
        // no-op
    }

    @Override
    public void saveHosts(List<HostData> hostDataList) {
        // no-op
    }

    @Override
    public List<HostData> loadHosts() {
        return new ArrayList<>(SAMPLE_HOSTS);
    }

    @Override
    public List<LogData> search(Date from, Date to, List<Integer> facilities, List<Integer> severities,
                               List<Integer> hosts, String pattern, int offset, int limit)
            throws IndexerServiceException {
        int toIndex = Math.min(offset + limit, SAMPLE_LOGS.size());
        return toIndex > offset ? new ArrayList<>(SAMPLE_LOGS.subList(offset, toIndex)) : Collections.emptyList();
    }

    @Override
    public void saveUnprocessedLogs() {
        // no-op
    }

    @Override
    public boolean hasUnprocessedLogs() {
        return true;
    }

    @Override
    public List<String> getUnprocessedHostsName() {
        return Collections.emptyList();
    }

    @Override
    public List<FirewallAlertData> getFirewallAlertData(Date date) {
        return Collections.emptyList();
    }

    @Override
    public List<FireWallDropData> getFirewallDropData(Date date) {
        return Collections.emptyList();
    }

    @Override
    public List<OssecAlertData> getOssecAlertData(Date data) {
        return Collections.emptyList();
    }

    @Override
    public List<SnortLogData> getSnortLogs(OssecSnortMessage ossecSnortMessage) {
        return Collections.emptyList();
    }

    @Override
    public List<OssecLogData> getOssecLogsByHash(String hash) {
        return Collections.emptyList();
    }

    @Override
    public void saveSnortLog(SnortLogData snortLogData) {
        // no-op
    }

    @Override
    public void saveOssecLog(OssecLogData ossecLogData) {
        // no-op
    }

    @Override
    public List<LogData> getLogsByHash(String hash) {
        return Collections.emptyList();
    }

    private static List<HostData> createSampleHosts() {
        List<HostData> hosts = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            HostData h = new HostData();
            h.setId((long) i);
            h.setHostname("host" + i);
            h.setIpAddress("192.168.1." + i);
            hosts.add(h);
        }
        return hosts;
    }

    private static List<LogData> createSampleLogs(int count) {
        List<LogData> logs = new ArrayList<>();
        String[] messages = {"Sample log message", "Connection established", "Request processed",
                "Cache hit", "User login", "Configuration loaded", "Backup completed"};
        for (int i = 1; i <= count; i++) {
            LogData log = new LogData();
            log.setId((long) i);
            log.setHost("host" + ((i % 5) + 1));
            log.setSeverity(i % 8);
            log.setFacility(i % 8);
            log.setProgram("app" + (i % 3));
            log.setMessage(messages[i % messages.length] + " #" + i);
            log.setDate(new Date(System.currentTimeMillis() - i * 60_000L));
            logs.add(log);
        }
        return logs;
    }
}
