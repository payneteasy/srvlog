package com.payneteasy.srvlog.adapter.syslog;

import com.nesscomputing.syslog4j.server.impl.event.SyslogServerEvent;
import com.payneteasy.srvlog.data.LogData;
import com.payneteasy.srvlog.data.LogFacility;
import com.payneteasy.srvlog.data.LogLevel;
import com.payneteasy.srvlog.service.ILogCollector;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import sun.util.resources.CalendarData_ar;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;

/**
 * Date: 22.01.13 Time: 13:42
 */
public class SyslogAdapterTest {

    private SyslogAdapter adapter;

    @Before
    public void setUp () {

    }


    @Test
    public void testParseMessageWithHostname() throws Exception {
        InetAddress inetAddress = InetAddress.getLocalHost();

        String syslogMessage = "<13>Dec 22 05:31:55 update monit[1612]: 'rootfs' space usage 80.6% matches resource limit [space usage>80.0%]";
        adapter = new SyslogAdapter();
        ILogCollector mockLogCollector = EasyMock.createMock(ILogCollector.class);
        adapter.setLogCollector(mockLogCollector);
        LogData logData = new LogData();
        Calendar c = Calendar.getInstance();
        c.set(2013, 11, 22, 05, 31, 55);
        c.set(Calendar.MILLISECOND, 0);

        logData.setDate(c.getTime());
        logData.setFacility(LogFacility.user.getValue());
        logData.setSeverity(LogLevel.NOTICE.getValue());

        String hostName = inetAddress.getHostName();
        int j = hostName.indexOf('.');

        if (j > -1) {
            hostName = hostName.substring(0,j);
        }
        logData.setHost(hostName);

        logData.setProgram("monit");
        logData.setMessage("[1612]: 'rootfs' space usage 80.6% matches resource limit [space usage>80.0%]");
        mockLogCollector.saveLog(logData);
        EasyMock.expectLastCall();
        EasyMock.replay(mockLogCollector);

        adapter.event(null, null, new SyslogServerEvent(syslogMessage, InetAddress.getLocalHost()));

        EasyMock.verify(mockLogCollector);

    }

    @Test
    public void parseLogMessageWithoutHostName() throws UnknownHostException {
        InetAddress inetAddress = InetAddress.getLocalHost();
        String syslogMessage = "<13>Dec 22 05:31:55 monit[1612]: 'rootfs' space usage 80.6% matches resource limit [space usage>80.0%]";
        adapter = new SyslogAdapter();
        ILogCollector mockLogCollector = EasyMock.createMock(ILogCollector.class);
        adapter.setLogCollector(mockLogCollector);
        LogData logData = new LogData();
        Calendar c = Calendar.getInstance();
        c.set(2013, 11, 22, 05, 31, 55);
        c.set(Calendar.MILLISECOND, 0);

        logData.setDate(c.getTime());
        logData.setFacility(LogFacility.user.getValue());
        logData.setSeverity(LogLevel.NOTICE.getValue());

        String hostName = inetAddress.getHostName();
        int j = hostName.indexOf('.');

        if (j > -1) {
            hostName = hostName.substring(0,j);
        }
        logData.setHost(hostName);

        logData.setProgram("monit");
        logData.setMessage("[1612]: 'rootfs' space usage 80.6% matches resource limit [space usage>80.0%]");
        mockLogCollector.saveLog(logData);
        EasyMock.expectLastCall();
        EasyMock.replay(mockLogCollector);

        adapter.event(null, null, new SyslogServerEvent(syslogMessage, InetAddress.getLocalHost()));

        EasyMock.verify(mockLogCollector);
    }

    @Test
    public void parseLongTag() throws UnknownHostException {
        InetAddress inetAddress = InetAddress.getLocalHost();
        String syslogMessage = "<13>Dec 22 05:31:55 123456789012345678901234567890123456789monit[1612]: 'rootfs' space usage 80.6% matches resource limit [space usage>80.0%]";
        adapter = new SyslogAdapter();
        ILogCollector mockLogCollector = EasyMock.createMock(ILogCollector.class);
        adapter.setLogCollector(mockLogCollector);
        LogData logData = new LogData();
        Calendar c = Calendar.getInstance();
        c.set(2013, 11, 22, 05, 31, 55);
        c.set(Calendar.MILLISECOND, 0);

        logData.setDate(c.getTime());
        logData.setFacility(LogFacility.user.getValue());
        logData.setSeverity(LogLevel.NOTICE.getValue());

        String hostName = inetAddress.getHostName();
        int j = hostName.indexOf('.');

        if (j > -1) {
            hostName = hostName.substring(0,j);
        }
        logData.setHost(hostName);

        logData.setMessage("[1612]: 'rootfs' space usage 80.6% matches resource limit [space usage>80.0%]");
        mockLogCollector.saveLog(logData);
        EasyMock.expectLastCall();
        EasyMock.replay(mockLogCollector);

        adapter.event(null, null, new SyslogServerEvent(syslogMessage, InetAddress.getLocalHost()));

        EasyMock.verify(mockLogCollector);
    }
}

