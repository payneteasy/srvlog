package com.payneteasy.srvlog.dao;

import com.payneteasy.srvlog.CommonIntegrationTest;
import com.payneteasy.srvlog.data.HostData;
import com.payneteasy.srvlog.data.LogData;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Date: 04.01.13
 */
public class LogDaoTest extends CommonIntegrationTest {


    private ILogDao logDao;

    @Before
    public void setUp() throws IOException, InterruptedException {
        super.setUp();
        logDao = context.getBean(ILogDao.class);
    }

    @Test
    public void testSaveHost() {
        addLocalhostToHostList();

        List<HostData> hostDataList = logDao.loadHosts();
        assertTrue(hostDataList.size() > 0);
    }

    @Test
    public void testSaveLogWithUnknownHost() {
        LogData logData = new LogData();
        logData.setSeverity(1);
        logData.setDate(new Date());
        logData.setFacility(1);
        logData.setMessage("message");
        logData.setHost("unknown.host");

        logDao.saveLog(logData);

        LogData persistedLogData = logDao.load(logData.getId());
        assertNull("Should be save in unprocessed_logs table", persistedLogData);

        // check presence in the unprocessed_logs table
        LogData unprocessedLogData = logDao.finInUnporocessed(logData.getId());
        assertNotNull("Should be present in unprocessed_logs table", unprocessedLogData);
    }

    @Test
    public void testSaveLog(){
        addLocalhostToHostList();


        LogData logData = new LogData();
        logData.setSeverity(1);
        logData.setDate(new Date());
        logData.setFacility(1);
        logData.setMessage("message");
        logData.setHost("localhost");

        logDao.saveLog(logData);
        assertNotNull("ID must be assigned to logData entity", logData.getId());

        LogData persistedLogData = logDao.load(logData.getId());
        assertEquals("Persisted and original objects have to be equal", logData, persistedLogData);
    }

    private void addLocalhostToHostList() {
        HostData hostData = new HostData();
        hostData.setHostname("localhost");
        hostData.setIpAddress("127.0.0.1");
        logDao.saveHost(hostData);
    }

    @Test
    public void testLoadLatest() {
        addLocalhostToHostList();

        for (int i = 0; i < 11; i++) {
            LogData logData = new LogData();
            logData.setDate(new Date());
            logData.setFacility(1);
            logData.setSeverity(1);
            logData.setHost("localhost");
            logData.setMessage("Log message " + i);
            logDao.saveLog(logData);
        }

        List<LogData> logDataList = logDao.loadLatest(5);

        assertEquals("loadLatest should return the numOfLogs log entries.", 5, logDataList.size());

        logDataList = logDao.loadLatest(7);

        assertEquals("loadLatest should return the numOfLogs log entries.", 7, logDataList.size());
    }



}