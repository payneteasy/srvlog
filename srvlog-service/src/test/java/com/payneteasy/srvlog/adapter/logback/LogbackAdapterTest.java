package com.payneteasy.srvlog.adapter.logback;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.net.SocketAppender;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import com.payneteasy.srvlog.adapter.utils.AdapterHelper;
import com.payneteasy.srvlog.data.LogData;
import com.payneteasy.srvlog.data.LogFacility;
import com.payneteasy.srvlog.data.LogLevel;
import com.payneteasy.srvlog.service.ILogCollector;
import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Date: 16.06.13
 * Time: 19:43
 */
public class LogbackAdapterTest {

    private static final Logger log = (Logger) LoggerFactory.getLogger(LogbackAdapterTest.class);


    @Test
    public void testSendLogbackLogDirectly() throws IOException {
        ILogCollector mockLogCollector = EasyMock.createMock(ILogCollector.class);

        LogbackAdapter logbackAdapter = new LogbackAdapter(mockLogCollector, "paynet", 4000);

        Calendar c = buildReferenceCalendar();
        LogData logData = buildReferenceLogData(c);

        mockLogCollector.saveLog(logData);
        EasyMock.expectLastCall();
        EasyMock.replay(mockLogCollector);

        ILoggingEvent logEvent = new LoggingEvent(log.getClass().getName(), log,
                Level.WARN, "This is test logging", null, null);
        logbackAdapter.processEvent(new ServerLogbackEvent(logEvent, InetAddress.getLoopbackAddress()));

        EasyMock.verify(mockLogCollector);
    }

    @Test
    public void testSendLog4jLogUsingTCP() throws IOException, InterruptedException {

        final CountDownLatch savedLatch = new CountDownLatch(1);

        ILogCollector mockLogCollector = EasyMock.createMock(ILogCollector.class);

        LogbackAdapter logbackAdapter = new LogbackAdapter(mockLogCollector, "paynet", 4713);

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
        logbackAdapter.init();

        SocketAppender appender = new SocketAppender();
        appender.setRemoteHost("localhost");
        appender.setPort(4713);
        appender.setContext(log.getLoggerContext());
        appender.start();

        ILoggingEvent logEvent = new LoggingEvent(log.getClass().getName(), log,
                /*c.getTime().getTime(), */Level.WARN, "This is test logging", null, null);
        appender.doAppend(logEvent);

        // waiting for 5 seconds at maximum
        Assert.assertTrue(savedLatch.await(5, TimeUnit.SECONDS));

        appender.stop();
        logbackAdapter.destroy();

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
