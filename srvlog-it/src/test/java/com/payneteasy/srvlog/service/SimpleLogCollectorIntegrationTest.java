package com.payneteasy.srvlog.service;

import com.payneteasy.srvlog.CommonIntegrationTest;
import com.payneteasy.srvlog.data.LogData;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * Date: 08.01.13
 * Time: 17:32
 */
public class SimpleLogCollectorIntegrationTest extends CommonIntegrationTest {

    private ILogCollector logCollector;

    @Override
    protected ClassPathXmlApplicationContext createSpringContext() {
        return new ClassPathXmlApplicationContext(
                "classpath:spring/spring-test-datasource.xml",
                "classpath:spring/spring-dao.xml",
                "classpath:spring/spring-service.xml",
                "classpath:spring/spring-log-adapter.xml"
        );
    }

    @Override
    @Before
    public void setUp() throws IOException, InterruptedException {
        super.setUp();
        logCollector = context.getBean(ILogCollector.class);
    }

    @Test
    public void testLoadLatest() {
        for (int i = 0; i < 12; i++) {
            LogData logData = new LogData();
            logData.setDate(new Date());
            logData.setFacility(1);
            logData.setSeverity(1);
            logData.setHost("localhost");
            logData.setMessage("Log message " + i);
            logCollector.saveLog(logData);
        }

        List<LogData> logDataList = logCollector.loadLatest(10);
        assertEquals("loadLatest(numOfLogs) should load logs not more than specified numOfLogs", 10, logDataList.size());
    }


}
