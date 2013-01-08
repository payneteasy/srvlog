package com.payneteasy.srvlog.service;

import com.nesscomputing.syslog4j.*;
import com.payneteasy.srvlog.adapter.syslog.ISyslogAdapterConfig;
import com.payneteasy.srvlog.dao.ILogDao;
import com.payneteasy.srvlog.data.LogData;
import com.payneteasy.srvlog.service.impl.SimpleLogCollector;
import com.payneteasy.srvlog.adapter.syslog.SyslogAdapter;
import org.junit.Ignore;
import org.junit.Test;

import java.text.MessageFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertTrue;

/**
 * Date: 04.01.13
 */
@Ignore
public class SimpleLogCollectorTest {

    @Test
    public void testSendLog() throws InterruptedException {

        SyslogAdapter syslogAdapter = new SyslogAdapter();
        ISyslogAdapterConfig testSyslogConfig = getTestSyslogConfig();
        syslogAdapter.setLogAdapterConfig(testSyslogConfig);

        SimpleLogCollector logCollector = new SimpleLogCollector();
        ILogDao mockLogDao = createMock(ILogDao.class);
        logCollector.setLogDao(mockLogDao);

        syslogAdapter.setLogCollector(logCollector);

        mockLogDao.saveLog(getTestLogData());
        replay(mockLogDao);
        boolean messageRetrieved = false;
        AssertionError verificationError = null;
        try {
            syslogAdapter.init();
            SyslogIF udpSyslogClient = createSyslog4jClient(testSyslogConfig);
            udpSyslogClient.info("message");
            for (int i = 1; i <= 5; i++) {
                try {
                   verify(mockLogDao);
                   messageRetrieved = true;
                   break;
                } catch (AssertionError e) {
                    System.out.printf("The message has not been retrieved. %s seconds passed.\n", i);
                    verificationError = e;
                    TimeUnit.SECONDS.sleep(1);
                }
            }


        } finally {
            syslogAdapter.destroy();
        }
        assertTrue(MessageFormat.format("Message has not been retrieved: {0}", verificationError.getMessage()), messageRetrieved);


    }

    private LogData getTestLogData() {
        LogData logData = new LogData();
        logData.setDate(new Date());
        logData.setSeverity(1);
        logData.setFacility(1);
        logData.setMessage("message");
        return logData;
    }

    private SyslogIF createSyslog4jClient(ISyslogAdapterConfig mockLogAdapterConfig) {
        SyslogIF udpSyslogClient = Syslog.getInstance(mockLogAdapterConfig.getSyslogProtocol());

        udpSyslogClient.getConfig().setSendLocalName(false);
        udpSyslogClient.getConfig().setPort(mockLogAdapterConfig.getSyslogPort());

        udpSyslogClient.getConfig().setFacility(SyslogFacility.forValue(1));

        return udpSyslogClient;
    }


    private ISyslogAdapterConfig getTestSyslogConfig() {
        return new ISyslogAdapterConfig() {

            @Override
            public String getSyslogProtocol() {
                return SyslogConstants.UDP;
            }

            @Override
            public int getSyslogPort() {
                return 1514;
            }
        };
    }


}
