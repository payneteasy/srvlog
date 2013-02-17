package com.payneteasy.srvlog.dao;

import com.payneteasy.srvlog.CommonIntegrationTest;
import com.payneteasy.srvlog.data.HostData;
import com.payneteasy.srvlog.data.LogData;
import com.payneteasy.srvlog.util.DateRange;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Calendar;
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
        logData.setProgram("program");

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
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MILLISECOND,0);
        Date date = calendar.getTime();

        LogData logData = saveTestLog(1, date);

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

        DateRange dateRange = DateRange.lastMonth();

        Calendar start = Calendar.getInstance();
        start.setTime(dateRange.getFromDate());
        Calendar end = Calendar.getInstance();
        end.setTime(dateRange.getToDate());
        end.add(Calendar.DATE, -3);

        for (; !start.after(end); start.add(Calendar.DATE, 1)) {
            Date current = start.getTime();
            saveTestLog(1, current);
        }

        List<LogData> logDataList = logDao.loadLatest(5, null);

        assertEquals("loadLatest should return the numOfLogs log entries.", 5, logDataList.size());

        logDataList = logDao.loadLatest(7, null);

        assertEquals("loadLatest should return the numOfLogs log entries.", 7, logDataList.size());
    }

    @Test
    public void testGetLogsDataByIds(){
        addLocalhostToHostList();

        for (int i = 0; i < 11; i++) {
            Date date = new Date();
            saveTestLog(i, date);
        }

        List<LogData> logDataList = logDao.getLogsByIds("1,2,3,4,5");
        assertEquals("getLogsByIds should return 5 log entries.", 5, logDataList.size());

    }

    private LogData saveTestLog(int i, Date date) {
        LogData logData = new LogData();
        logData.setDate(date);
        logData.setFacility(1);
        logData.setSeverity(1);
        logData.setHost("localhost");
        logData.setMessage("Log message " + i);
        logDao.saveLog(logData);
        return logData;
    }

    @Test
    public void testGetUnprocessedLogs() {
        saveTestLog(1, new Date());

        List<LogData> logDataList = logDao.loadUnprocessed(1);

        assertTrue("Should return one unprocessed log", logDataList.size() == 1);
    }

    @Test
    public void testSaveUnprocessedLogs() {
        saveTestLog(1, new Date());

        List<LogData> unprocessedLogs = logDao.loadUnprocessed(1);

        assertTrue("Should return one unprocessed log", unprocessedLogs.size() == 1);

        addLocalhostToHostList();

        logDao.saveUnprocessedLogs();

        unprocessedLogs = logDao.loadUnprocessed(1);

        assertTrue("Should return zero unprocessed logs when saveUnprocessedLogs has been invoked", unprocessedLogs.size() == 0);


        List<LogData> processedLogs = logDao.loadLatest(1, null);

        assertTrue("Should returne one processed logs when saveUnprocessedLogs has been invoked", processedLogs.size() == 1);


    }

    public static void main(String[] args) {
        DateRange dateRange = DateRange.thisMonth();

        Calendar start = Calendar.getInstance();
        start.setTime(dateRange.getFromDate());
        Calendar end = Calendar.getInstance();
        end.setTime(dateRange.getToDate());

        for (; !start.after(end); start.add(Calendar.DATE, 1)) {
            Date current = start.getTime();
            System.out.println(current);
        }
    }

}
