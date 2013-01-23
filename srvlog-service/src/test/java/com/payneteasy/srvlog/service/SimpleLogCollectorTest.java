package com.payneteasy.srvlog.service;

import com.nesscomputing.syslog4j.Syslog;
import com.nesscomputing.syslog4j.SyslogConstants;
import com.nesscomputing.syslog4j.SyslogFacility;
import com.nesscomputing.syslog4j.SyslogIF;
import com.payneteasy.srvlog.adapter.syslog.ISyslogAdapterConfig;
import com.payneteasy.srvlog.adapter.syslog.SyslogAdapter;
import com.payneteasy.srvlog.dao.ILogDao;
import com.payneteasy.srvlog.data.LogData;
import com.payneteasy.srvlog.data.LogFacility;
import com.payneteasy.srvlog.service.impl.SimpleLogCollector;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertTrue;

/**
 * Date: 04.01.13
 */
//@Ignore
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
            udpSyslogClient.info("program[1234]:message"); //log level 6
            for (int i = 1; i <= 3; i++) {
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
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MILLISECOND, 0);
        logData.setDate(new Date());
        logData.setSeverity(6);
        logData.setFacility(LogFacility.alert.getValue());
        logData.setMessage("[1234]:message");
        logData.setProgram("program");
        return logData;
    }

    private SyslogIF createSyslog4jClient(ISyslogAdapterConfig mockLogAdapterConfig) {
        SyslogIF udpSyslogClient = Syslog.getInstance(mockLogAdapterConfig.getSyslogProtocol());

        udpSyslogClient.getConfig().setSendLocalName(false);
        udpSyslogClient.getConfig().setPort(mockLogAdapterConfig.getSyslogPort());

        udpSyslogClient.getConfig().setFacility(SyslogFacility.alert);

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

    @Test
    public void searchLogs() throws IndexerServiceException {
        SimpleLogCollector logCollector = new SimpleLogCollector();

//        INIT MOCK SERVICES
        ILogDao mockLogDao = createMock(ILogDao.class);
        logCollector.setLogDao(mockLogDao);
        IIndexerService mockIndexerService = createMock(IIndexerService.class);
        logCollector.setIndexerService(mockIndexerService);

        List<Integer> facilities = Arrays.asList(1, 2, 3, 4);
        List<Integer> severities = Arrays.asList(1, 2, 3, 4);
        List<Integer> host = Arrays.asList(1, 2, 3, 4);
        ;
        String pattern = "";
        int offset = 0;
        int limit = 25;

        Date from = new Date();
        Date to = new Date();

        expect(mockIndexerService.search(from, to, facilities, severities, host, pattern, offset, limit)).andReturn(getIds());

        expect(mockLogDao.getLogsByIds(StringUtils.collectionToCommaDelimitedString(getIds()))).andReturn(getLogs());

        replay(mockLogDao, mockIndexerService);

        logCollector.search(from, to, facilities, severities, host, pattern, offset, limit);

        verify(mockLogDao, mockIndexerService);

    }



    private List<Long> getIds() {
        return Arrays.asList(1L, 2L, 3L);
    }


    public List<LogData> getLogs() {
        return new ArrayList<LogData>();
    }


}
