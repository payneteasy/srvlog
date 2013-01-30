package com.payneteasy.srvlog.service;

import com.payneteasy.srvlog.CommonIntegrationTest;
import com.payneteasy.srvlog.DatabaseUtil;
import com.payneteasy.srvlog.dao.ILogDao;
import com.payneteasy.srvlog.data.HostData;
import com.payneteasy.srvlog.data.LogData;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DuplicateKeyException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Date: 08.01.13
 * Time: 17:32
 */
public class SimpleLogCollectorIntegrationTest extends CommonIntegrationTest {

    private ILogCollector logCollector;

    @Override
    protected void createSpringContext() {
        context = new ClassPathXmlApplicationContext(
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
        DatabaseUtil.addLocalhostToHostList(context.getBean(ILogDao.class));
    }

    @Test
    public void testLoadLatest() {
        for (int i = 0; i < 12; i++) {
            LogData logData = new LogData();
            logData.setDate(new Date());
            logData.setFacility(1);
            logData.setSeverity(1);
            logData.setHost("127.0.0.1");
            logData.setMessage("Log message " + i);
            logData.setProgram("program");
            logCollector.saveLog(logData);
        }

        List<LogData> logDataList = logCollector.loadLatest(10);
        assertEquals("loadLatest(numOfLogs) should load logs not more than specified numOfLogs", 10, logDataList.size());
    }

    @Test
    public void testSaveHosts(){
        List<HostData> newHostDataList = getHostDataList();

        List<HostData> beforeHostDataList = logCollector.loadHosts();

        logCollector.saveHosts(newHostDataList);

        List<HostData> afterHostDataList = logCollector.loadHosts();

        assertEquals("loadHosts() should load hosts size equal before host size + new host size",beforeHostDataList.size()+newHostDataList.size(), afterHostDataList.size());
    }

    private static List<HostData> getHostDataList(){
        String hosts = "testhost1,12.12.12.13;testhost2,12.12.12.13";
        String[] arrayHosts = hosts.split(";");
        List<HostData> hostDataList = new ArrayList<HostData>(arrayHosts.length-1);
        for (String arrayHost : arrayHosts) {
            String[] currentHost = arrayHost.split(",");
            HostData hostData = new HostData();
            hostData.setHostname(currentHost[0]);
            hostData.setIpAddress(currentHost[1]);
            hostDataList.add(hostData);
        }
        return hostDataList;
    }

}
