package com.payneteasy.srvlog.dao;

import com.payneteasy.srvlog.CommonIntegrationTest;
import com.payneteasy.srvlog.data.LogData;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Date: 04.01.13
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"classpath:spring/spring-test-datasource.xml","classpath:spring/spring-dao.xml", "classpath:spring/spring-service.xml"})
public class LogDaoTest extends CommonIntegrationTest {


    private ILogDao logDao;

    @Before
    public void setUp() {
        logDao = context.getBean(ILogDao.class);
    }

    @Test
    public void testSaveLog(){
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

    @Test
    public void testLoadLatest() {
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
