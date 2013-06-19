package com.payneteasy.srvlog.adapter.syslog;

import com.nesscomputing.syslog4j.server.impl.event.SyslogServerEvent;
import com.payneteasy.srvlog.adapter.utils.AdapterHelper;
import com.payneteasy.srvlog.data.LogData;
import com.payneteasy.srvlog.data.LogFacility;
import com.payneteasy.srvlog.data.LogLevel;
import com.payneteasy.srvlog.service.ILogCollector;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Locale;

/**
 * Date: 22.01.13 Time: 13:42
 */
public class SyslogAdapterTest {

    private SyslogAdapter adapter;

    @Before
    public void setUp () {
        Locale.setDefault(Locale.US);
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

        AdapterHelper.setHostName(inetAddress, logData);

        logData.setProgram("monit");
        logData.setMessage("[1612]: 'rootfs' space usage 80.6% matches resource limit [space usage>80.0%]");
        mockLogCollector.saveLog(logData);
        EasyMock.expectLastCall();
        EasyMock.replay(mockLogCollector);

        adapter.event(null, null, new SyslogServerEvent(syslogMessage, InetAddress.getLocalHost()));

        EasyMock.verify(mockLogCollector);

    }

    @Test
    public void testParseMessageWithHostnameWithoutPID() throws UnknownHostException {
        InetAddress inetAddress = InetAddress.getLocalHost();

        String syslogMessage = "<132>Feb 17 13:50:21 log-1 ossec: Alert Level: 2; Rule: 1002 - Unknown problem somewhere in the system.; Location: (sso) 10.0.1.7->/var/log/messages; Feb 17 13:50:19 sso syslog-ng[8332]: Connection failed; server='AF_INET(10.0.1.4:2514)', error='Connection refused (111)', time_reopen='10'";
        adapter = new SyslogAdapter();
        ILogCollector mockLogCollector = EasyMock.createMock(ILogCollector.class);
        adapter.setLogCollector(mockLogCollector);
        LogData logData = new LogData();
        Calendar c = Calendar.getInstance();
        c.set(2013, 01, 17, 13, 50, 21);
        c.set(Calendar.MILLISECOND, 0);

        logData.setDate(c.getTime());
        logData.setFacility(LogFacility.local0.getValue());
        logData.setSeverity(LogLevel.WARN.getValue());

        AdapterHelper.setHostName(inetAddress, logData);

        logData.setProgram("ossec");
        logData.setMessage(": Alert Level: 2; Rule: 1002 - Unknown problem somewhere in the system.; Location: (sso) 10.0.1.7->/var/log/messages; Feb 17 13:50:19 sso syslog-ng[8332]: Connection failed; server='AF_INET(10.0.1.4:2514)', error='Connection refused (111)', time_reopen='10'");
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

        AdapterHelper.setHostName(inetAddress, logData);

        logData.setProgram("monit");
        logData.setMessage("[1612]: 'rootfs' space usage 80.6% matches resource limit [space usage>80.0%]");
        mockLogCollector.saveLog(logData);
        EasyMock.expectLastCall();
        EasyMock.replay(mockLogCollector);

        adapter.event(null, null, new SyslogServerEvent(syslogMessage, InetAddress.getLocalHost()));

        EasyMock.verify(mockLogCollector);
    }

    @Test
    public void parseLogTagMore32() throws UnknownHostException {
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

        AdapterHelper.setHostName(inetAddress, logData);

        logData.setMessage("123456789012345678901234567890123456789monit[1612]: 'rootfs' space usage 80.6% matches resource limit [space usage>80.0%]");
        mockLogCollector.saveLog(logData);
        EasyMock.expectLastCall();
        EasyMock.replay(mockLogCollector);

        adapter.event(null, null, new SyslogServerEvent(syslogMessage, InetAddress.getLocalHost()));

        EasyMock.verify(mockLogCollector);
    }

//      PRIVATE METHODS

}

