package com.payneteasy.srvlog.syslog;

import com.nesscomputing.syslog4j.Syslog;
import com.nesscomputing.syslog4j.SyslogFacility;
import com.nesscomputing.syslog4j.SyslogIF;
import com.payneteasy.srvlog.CommonIntegrationTest;
import com.payneteasy.srvlog.adapter.syslog.ISyslogAdapterConfig;
import com.payneteasy.srvlog.data.LogData;
import com.payneteasy.srvlog.service.ILogCollector;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

/**
 * Date: 08.01.13
 * Time: 16:53
 */

public class Syslog4jAdaptor$SimpleLogCollectorIntegrationTest extends CommonIntegrationTest {

    private SyslogIF syslogClient;
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

    @Before
    public void setUp() throws IOException, InterruptedException {
        super.setUp();
        syslogClient = createSyslog4jClient(context.getBean(ISyslogAdapterConfig.class));
        logCollector = context.getBean(ILogCollector.class);
    }

    @After
    public void tearDown() {
        super.tearDown();
        syslogClient.shutdown();
    }

    @Test
    public void testRetrieveAndSaveSyslogMessage() throws InterruptedException {
        syslogClient.info("A test info message");
        int numberOfLogs = 25;
        int numOfTries = 3;
        List<LogData> logDataList = null;
        for (int i = 0; i < numOfTries ; i++) {
            TimeUnit.SECONDS.sleep(2);
            logDataList = logCollector.loadLatest(numberOfLogs);
            if (logDataList.size() > 0) {
                 break;
            }
        }

        assertTrue("Logs should exist in defined time interval", logDataList.size() > 0);
    }

    private SyslogIF createSyslog4jClient(ISyslogAdapterConfig syslogAdapterConfig) {
        SyslogIF udpSyslogClient = Syslog.getInstance(syslogAdapterConfig.getSyslogProtocol());

        udpSyslogClient.getConfig().setSendLocalName(false);
        udpSyslogClient.getConfig().setPort(syslogAdapterConfig.getSyslogPort());

        udpSyslogClient.getConfig().setFacility(SyslogFacility.forValue(1));

        return udpSyslogClient;
    }



}
