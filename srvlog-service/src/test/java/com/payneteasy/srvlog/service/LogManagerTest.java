package com.payneteasy.srvlog.service;

import com.nesscomputing.syslog4j.*;
import com.nesscomputing.syslog4j.impl.message.processor.SyslogMessageProcessor;
import com.payneteasy.srvlog.dao.ILogManagerDao;
import com.payneteasy.srvlog.data.LogData;
import com.payneteasy.srvlog.service.impl.LogManagerImpl;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.easymock.EasyMock.*;

/**
 * Date: 04.01.13
 */
@Ignore
public class LogManagerTest {

    @Test
    public void testSendLog() throws InterruptedException {

        LogManagerImpl logManager = new LogManagerImpl();
        ILogManagerConfig mockLogManagerConfig = getMockLogManagerConfig();
        logManager.setLogManagerConfig(mockLogManagerConfig);
        ILogManagerDao mockLogManagerDao = createMock(ILogManagerDao.class);
        logManager.setLogManagerDao(mockLogManagerDao);

        mockLogManagerDao.saveLog(getTestLogData());
        replay(mockLogManagerDao);

        try {
            logManager.init();
            SyslogIF udpSyslogClient = createSyslog4jClient(mockLogManagerConfig);
            udpSyslogClient.info("message");
            for (int i = 1; i <= 5; i++) {
                try {
                   verify(mockLogManagerDao);
                   break;
                } catch (AssertionError e) {
                    System.out.printf("The message has not been retrieved. %s seconds passed.\n", i);
                    TimeUnit.SECONDS.sleep(1);
                }
            }


        } finally {
            logManager.destroy();
        }

    }

    private LogData getTestLogData() {
        LogData logData = new LogData();
        logData.setDate(new Date());
        logData.setSeverity(1);
        logData.setFacility(1);
        logData.setMessage("message");
        return logData;
    }

    private SyslogIF createSyslog4jClient(ILogManagerConfig mockLogManagerConfig) {
        SyslogIF udpSyslogClient = Syslog.getInstance(mockLogManagerConfig.getSyslog4jProtocol());

        udpSyslogClient.getConfig().setSendLocalName(false);
        udpSyslogClient.getConfig().setPort(mockLogManagerConfig.getSyslog4jPort());

        udpSyslogClient.getConfig().setFacility(SyslogFacility.forValue(1));

        return udpSyslogClient;
    }


    private ILogManagerConfig getMockLogManagerConfig() {
        return new ILogManagerConfig() {

            @Override
            public String getSyslog4jProtocol() {
                return SyslogConstants.UDP;
            }

            @Override
            public int getSyslog4jPort() {
                return 1514;
            }
        };
    }

}
