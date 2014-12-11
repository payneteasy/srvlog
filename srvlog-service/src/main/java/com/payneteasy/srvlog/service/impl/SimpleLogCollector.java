package com.payneteasy.srvlog.service.impl;

import com.payneteasy.srvlog.adapter.syslog.OssecSnortMessage;
import com.payneteasy.srvlog.adapter.syslog.RawSnortMessage;
import com.payneteasy.srvlog.data.UnprocessedSnortLogData;
import com.payneteasy.srvlog.dao.ILogDao;
import com.payneteasy.srvlog.data.*;
import com.payneteasy.srvlog.service.IIndexerService;
import com.payneteasy.srvlog.service.ILogCollector;
import com.payneteasy.srvlog.service.IndexerServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Date: 05.01.13
 */
@Transactional
@Service
public class SimpleLogCollector implements ILogCollector {

    @Autowired
    private ILogDao logDao;

    @Autowired
    private IIndexerService indexerService;

    @Override
    public void saveLog(LogData logData) {
        logDao.saveLog(logData);
    }

    public void changeHasSnortLogs(Long logId, boolean hasSnortLogs) {
        logDao.changeHasSnortLogs(logId, hasSnortLogs ? 1 : 0);
    }
    
    public void setLogDao(ILogDao logDao) {
        this.logDao = logDao;
    }

    @Override
    public List<LogData> loadLatest(int numberOfLogs, Long hostId) {
        return logDao.loadLatest(numberOfLogs, hostId);
    }

    @Override
    public void saveHost(HostData hostData) {
        logDao.saveHost(hostData);
    }

    @Override
    public void saveHosts(List<HostData> hostDataList) {
        for (HostData hostData : hostDataList) {
             logDao.saveHost(hostData);
        }
    }

    @Override
    public List<HostData> loadHosts() {
        return logDao.loadHosts();
    }

    @Override
    public List<LogData> search(Date from , Date to, List<Integer> facilities, List<Integer> severities, List<Integer> hosts, String pattern, int offset, int limit) throws IndexerServiceException {
        List<Long> ids = indexerService.search(from, to, facilities, severities, hosts, pattern, offset, limit);
        String stringIds = StringUtils.collectionToCommaDelimitedString(ids);
        if(StringUtils.hasText(stringIds)){
            return logDao.getLogsByIds(stringIds);
        }else {
            return Collections.emptyList();
        }
    }

    @Override
    public void saveUnprocessedLogs() {
         logDao.saveUnprocessedLogs();
    }

    @Override
    public boolean hasUnprocessedLogs() {
        return (logDao.loadUnprocessed(1).size() > 0);
    }

    @Override
    public List<String> getUnprocessedHostsName() {
        return logDao.getUnprocessedHostsName();
    }

    @Override
    public List<FirewallAlertData> getFirewallAlertData(Date date) {
        return logDao.getFirewallAlertData(date);
    }

    @Override
    public List<FireWallDropData> getFirewallDropData(Date date) {
        return logDao.getFirewallDropData(date);
    }

    @Override
    public List<OssecAlertData> getOssecAlertData(Date data) {
        return logDao.getOssecAlertData(data);
    }

    public void setIndexerService(IIndexerService indexerService) {
        this.indexerService = indexerService;
    }

    @Override
    public void saveUnprocessedSnortLog(RawSnortMessage rawSnortMessage) {
        logDao.saveUnprocessedSnortLog(rawSnortMessage.toUnprocessedSnortLogData());
    }

    @Override
    public void saveSnortLog(SnortLogData snortLogData) {
        logDao.saveSnortLog(snortLogData);
    }

    @Override
    public void saveOssecLog(OssecLogData ossecLogData) {
        logDao.saveOssecLog(ossecLogData);
    }

    @Override
    public List<UnprocessedSnortLogData> getUnprocessedSnortLogs(OssecSnortMessage ossecSnortMessage) {
        return logDao.getUnprocessedSnortLogs(
            ossecSnortMessage.getIdentifier(),
            ossecSnortMessage.getDateFrom(),
            ossecSnortMessage.getDateTo()
        );
    }

    @Override
    public List<OssecLogData> getOssecLogs(RawSnortMessage rawSnortMessage) {
        return logDao.getOssecLogs(
            rawSnortMessage.getIdentifier(),
            rawSnortMessage.getDateFrom(),
            rawSnortMessage.getDateTo()
        );
    }

    @Override
    public List<LogData> getLogsByHash(String hash) {
        return logDao.getLogsByHash(hash);
    }

    @Override
    public List<SnortLogData> getSnortLogsByHash(String hash) {
        return logDao.getSnortLogsByHash(hash);
    }
}
