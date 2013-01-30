package com.payneteasy.srvlog.service.impl;

import com.payneteasy.srvlog.dao.ILogDao;
import com.payneteasy.srvlog.data.HostData;
import com.payneteasy.srvlog.data.LogData;
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

    public void setLogDao(ILogDao logDao) {
        this.logDao = logDao;
    }

    @Override
    public List<LogData> loadLatest(int numberOfLogs) {
        return logDao.loadLatest(numberOfLogs);
    }

    @Override
    public void saveHost(HostData hostData) {
        logDao.saveHost(hostData);
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
            return Collections.<LogData>emptyList();
        }
    }

    @Override
    public Long getNumberUnprocessedHosts() {
//        return logDao.getNumberUnprocessedHosts();
        return 10L;
    }

    @Override
    public void saveUnprocessedHosts() {
//        logDao.saveUnprocessedLogs();
    }

    @Override
    public void addHosts(String hosts) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    public void setIndexerService(IIndexerService indexerService) {
        this.indexerService = indexerService;
    }
}
