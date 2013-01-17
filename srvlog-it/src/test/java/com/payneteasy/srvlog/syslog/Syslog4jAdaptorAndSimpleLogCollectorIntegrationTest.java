package com.payneteasy.srvlog.syslog;

import com.nesscomputing.syslog4j.Syslog;
import com.nesscomputing.syslog4j.SyslogFacility;
import com.nesscomputing.syslog4j.SyslogIF;
import com.payneteasy.srvlog.CommonIntegrationTest;
import com.payneteasy.srvlog.DatabaseUtil;
import com.payneteasy.srvlog.adapter.syslog.ISyslogAdapterConfig;
import com.payneteasy.srvlog.dao.ILogDao;
import com.payneteasy.srvlog.data.LogData;
import com.payneteasy.srvlog.service.ILogCollector;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
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

public class Syslog4jAdaptorAndSimpleLogCollectorIntegrationTest extends CommonIntegrationTest {

    private SyslogIF syslogClient;
    private ILogCollector logCollector;

    @Override
    protected void createSpringContext() {
        context =  new ClassPathXmlApplicationContext(
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
        syslogClient = createSyslog4jClient(context.getBean(ISyslogAdapterConfig.class));
        logCollector = context.getBean(ILogCollector.class);
        DatabaseUtil.addLocalhostToHostList(context.getBean(ILogDao.class));
    }

    @After
    public void tearDown() {
        syslogClient.shutdown();
        super.tearDown();
    }

    @Test
    public void testRetrieveAndSaveSyslogMessage() throws InterruptedException {
        syslogClient.getConfig().setHost("localhost");
        syslogClient.info("A test info message");
        syslogClient.flush();
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


    public static SyslogIF createSyslog4jClient(ISyslogAdapterConfig syslogAdapterConfig) {
        SyslogIF udpSyslogClient = Syslog.getInstance(syslogAdapterConfig.getSyslogProtocol());

        udpSyslogClient.getConfig().setSendLocalName(false);
        udpSyslogClient.getConfig().setPort(syslogAdapterConfig.getSyslogPort());
        udpSyslogClient.getConfig().setFacility(SyslogFacility.local0);
        return udpSyslogClient;
    }

    public static void main(String[] args) {
        SyslogIF syslog4jClient = createSyslog4jClient(new ISyslogAdapterConfig() {
            @Override
            public String getSyslogProtocol() {
                return "tcp";
            }

            @Override
            public int getSyslogPort() {
                return 1514;
            }
        });
        syslog4jClient.getConfig().setHost("localhost");
        try {
            DatabaseUtil.generateTestLogsThroughSyslogClient(syslog4jClient);
            syslog4jClient.flush();
        }finally {
            if (syslog4jClient !=null) {

                syslog4jClient.shutdown();
            }
        }

    }



}
