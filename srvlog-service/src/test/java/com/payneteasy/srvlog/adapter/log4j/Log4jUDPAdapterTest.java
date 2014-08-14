package com.payneteasy.srvlog.adapter.log4j;

import com.payneteasy.loggingextensions.log4j.UDPLog4jAppender;
import com.payneteasy.srvlog.adapter.utils.AdapterHelper;
import com.payneteasy.srvlog.data.LogData;
import com.payneteasy.srvlog.data.LogFacility;
import com.payneteasy.srvlog.data.LogLevel;
import com.payneteasy.srvlog.service.ILogCollector;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Log4jUDPAdapterTest {

    private static final Logger log = Logger.getLogger(Log4jUDPAdapterTest.class);


    @Test
    public void testSendLog4jLogDirectly() throws IOException {
        ILogCollector mockLogCollector = EasyMock.createMock(ILogCollector.class);

        Log4jUDPAdapter log4jAdapter = new Log4jUDPAdapter(mockLogCollector, "paynet", 4000);

        Calendar c = buildReferenceCalendar();
        LogData logData = buildReferenceLogData(c);

        mockLogCollector.saveLog(logData);
        EasyMock.expectLastCall();
        EasyMock.replay(mockLogCollector);

        LoggingEvent logEvent = new LoggingEvent(log.getClass().getName(), log,
                Level.WARN, "This is test logging", null);
        log4jAdapter.processEvent(new ServerLog4JEvent(logEvent, InetAddress.getLoopbackAddress()));

        EasyMock.verify(mockLogCollector);
    }

    @Test
    public void testSendLog4jLogUsingTCP() throws IOException, InterruptedException {

        final CountDownLatch savedLatch = new CountDownLatch(1);

        ILogCollector mockLogCollector = EasyMock.createMock(ILogCollector.class);

        Log4jUDPAdapter log4jAdapter = new Log4jUDPAdapter(mockLogCollector, "paynet", 4713);

        Calendar c = buildReferenceCalendar();
        LogData logData = buildReferenceLogData(c);

        mockLogCollector.saveLog(logData);
        EasyMock.expectLastCall().andAnswer(new IAnswer<Object>() {
            @Override
            public Object answer() throws Throwable {
                savedLatch.countDown();
                return null;
            }
        });
        EasyMock.replay(mockLogCollector);

        // initializing, binding to the server socket...
        log4jAdapter.init();

        UDPLog4jAppender appender = new UDPLog4jAppender();
        appender.setRemoteHost("localhost");
        appender.setPort(4713);
        appender.activateOptions();

        LoggingEvent logEvent = new LoggingEvent(log.getClass().getName(), log,
                Level.WARN, "This is test logging", null);
        appender.doAppend(logEvent);

        // waiting for 5 seconds at maximum
        Assert.assertTrue(savedLatch.await(5, TimeUnit.SECONDS));

        appender.close();
        log4jAdapter.destroy();

        EasyMock.verify(mockLogCollector);
    }

    private LogData buildReferenceLogData(Calendar c) throws UnknownHostException {
        LogData logData = new LogData();


        logData.setDate(c.getTime());
        logData.setFacility(LogFacility.user.getValue());
        logData.setSeverity(LogLevel.WARN.getValue());

        AdapterHelper.setHostName(InetAddress.getLoopbackAddress(), logData);

        logData.setProgram("paynet");
        logData.setMessage("This is test logging");
        return logData;
    }

    private Calendar buildReferenceCalendar() {
        Calendar c = Calendar.getInstance();
        c.set(2013, 01, 17, 13, 50, 21);
        c.set(Calendar.MILLISECOND, 0);
        return c;
    }

}
