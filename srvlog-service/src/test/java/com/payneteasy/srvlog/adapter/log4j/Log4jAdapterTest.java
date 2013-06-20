package com.payneteasy.srvlog.adapter.log4j;

import com.payneteasy.srvlog.adapter.utils.AdapterHelper;
import com.payneteasy.srvlog.data.LogData;
import com.payneteasy.srvlog.data.LogFacility;
import com.payneteasy.srvlog.data.LogLevel;
import com.payneteasy.srvlog.service.ILogCollector;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.InetAddress;
import java.util.Calendar;

/**
 * Date: 16.06.13
 * Time: 19:43
 */
public class Log4jAdapterTest {

    private static final Logger log = Logger.getLogger(Log4jAdapterTest.class);


    @Test
    public void testSendLog4jLog() throws IOException {

        Log4jAdapter log4jAdapter = new Log4jAdapter();
        log4jAdapter.setProgram("paynet");
        ILogCollector mockLogCollector = EasyMock.createMock(ILogCollector.class);
        log4jAdapter.setLogCollector(mockLogCollector);

        LogData logData = new LogData();
        Calendar c = Calendar.getInstance();
        c.set(2013, 01, 17, 13, 50, 21);
        c.set(Calendar.MILLISECOND, 0);




        logData.setDate(c.getTime());
        logData.setFacility(LogFacility.user.getValue());
        logData.setSeverity(LogLevel.WARN.getValue());

        AdapterHelper.setHostName(InetAddress.getLocalHost(), logData);

        logData.setProgram("paynet");
        logData.setMessage("This is test logging");
        mockLogCollector.saveLog(logData);
        EasyMock.expectLastCall();
        EasyMock.replay(mockLogCollector);

        LoggingEvent logEvent = new LoggingEvent(log.getClass().getName(), log, c.getTime().getTime(), Level.WARN, "This is test logging", null);
        log4jAdapter.processEvent(new ServerLog4jEvent(logEvent, InetAddress.getLocalHost()));

        EasyMock.verify(mockLogCollector);




    }

}
