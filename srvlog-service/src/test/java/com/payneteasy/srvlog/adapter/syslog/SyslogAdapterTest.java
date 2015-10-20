package com.payneteasy.srvlog.adapter.syslog;

import com.nesscomputing.syslog4j.server.impl.event.SyslogServerEvent;
import com.payneteasy.srvlog.adapter.utils.AdapterHelper;
import com.payneteasy.srvlog.data.LogData;
import com.payneteasy.srvlog.data.LogFacility;
import com.payneteasy.srvlog.data.LogLevel;
import com.payneteasy.srvlog.data.OssecLogData;
import com.payneteasy.srvlog.data.SnortLogData;
import com.payneteasy.srvlog.service.ILogCollector;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Locale;
import org.easymock.EasyMock;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

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

    @Test
    public void testSaveOssecAlert() throws Exception {
        InetAddress inetAddress = InetAddress.getLocalHost();

        String syslogMessage
                = "Jun  9 16:08:10 log-1 ossec: Alert Level: 10; Rule: 20121 - MIDSE on nginx-1; "
                + "Location: (firewall) 10.0.1.1->/var/log/messages; srcip: 185.56.80.125; dstip: 10.0.2.4; "
                + "Jun  9 16:08:10 firewall snort[20441]: [1:2016979:3] ET WEB_SERVER suhosin.simulation PHP config option in uri "
                + "[Classification: A Network Trojan was detected] [Priority: 1] <eth3> {TCP} 185.56.80.125:59841 -> 10.0.2.4:4027";

        adapter = new SyslogAdapter();
        ILogCollector mockLogCollector = EasyMock.createMock(ILogCollector.class);
        adapter.setLogCollector(mockLogCollector);

        LogData logData = new LogData();

        logData.setDate(new DateTime(new DateTime().getYear(), 6, 9, 16, 8, 10).toDate());
        logData.setFacility(LogFacility.user.getValue());
        logData.setSeverity(LogLevel.INFO.getValue());

        AdapterHelper.setHostName(inetAddress, logData);

        logData.setProgram("ossec");
        logData.setMessage(
            ": Alert Level: 10; Rule: 20121 - MIDSE on nginx-1; "
            + "Location: (firewall) 10.0.1.1->/var/log/messages; srcip: 185.56.80.125; dstip: 10.0.2.4; "
            + "Jun  9 16:08:10 firewall snort[20441]: [1:2016979:3] ET WEB_SERVER suhosin.simulation PHP config option in uri "
            + "[Classification: A Network Trojan was detected] [Priority: 1] <eth3> {TCP} 185.56.80.125:59841 -> 10.0.2.4:4027"
        );

        mockLogCollector.saveLog(logData);
        EasyMock.expectLastCall().andAnswer(() -> {
            LogData logData1 = (LogData) EasyMock.getCurrentArguments()[0];
            logData1.setId(1L);
            return null;
        });

        OssecLogData ossecLogData = new OssecLogData();

        ossecLogData.setLogId(1L);
        ossecLogData.setDate(logData.getDate());
        ossecLogData.setIdentifier("[1:2016979:3]");
        ossecLogData.setHash("43121a82906a94c006faac0b9877af7d");

        mockLogCollector.saveOssecLog(ossecLogData);
        EasyMock.expectLastCall();

        EasyMock.replay(mockLogCollector);

        adapter.event(null, null, new SyslogServerEvent(syslogMessage, InetAddress.getLocalHost()));

        EasyMock.verify(mockLogCollector);
    }

    @Test
    public void testSaveSnortMessage()  throws Exception {
        InetAddress inetAddress = InetAddress.getLocalHost();

        String syslogMessage =
            "| [SNORTIDS[LOG]: [snortIds1-eth1] ] |" +
            "| 2014-09-11 16:23:01.479 1 [1:1310:5] Snort Alert [1:1310:5] |" +
            "| policy-violation |" +
            "| 6 192.168.1.137 173.194.39.114 4 20 0 1113 40840 2 0 65456 0 |" +
            "| 55536 80 3885499888 1996581709 8 0 24 229 41947 0 |" +
            "| 1127 10BF48C888A0AC220BC9529B0800450004599F8840004006FFB0C0A80189ADC22772D8F00050E79805F077016B4D8" +
            "01800E5A3DB00000101080A02779A7599D090E8474554202F7365617263683F713D66657469736820485454502F312E310D0A" +
            "557365722D4167656E743A204F706572612F392E383020285831313B204C696E7578207838365F3634292050726573746F2F322" +
            "E31322E3338382056657273696F6E2F31322E31360D0A486F73743A207777772E676F6F676C652E636F6D0D0A4163636570743" +
            "A20746578742F68746D6C2C206170706C69636174696F6E2F786D6C3B713D302E392C206170706C69636174696F6E2F7868746" +
            "D6C2B786D6C2C20696D6167652F706E672C20696D6167652F776562702C20696D6167652F6A7065672C20696D6167652F67696" +
            "62C20696D6167652F782D786269746D61702C202A2F2A3B713D302E310D0A4163636570742D4C616E67756167653A2072752D5" +
            "2552C72753B713D302E392C656E3B713D302E380D0A4163636570742D456E636F64696E673A20677A69702C206465666C61746" +
            "50D0A436F6F6B69653A20485349443D41624A475573673434413867617042585F3B204150495349443D4C615F5A5264614B626" +
            "B34356A696B742F414C366B4A664C534A51765378356B464E3B204E49443D36373D726C76562D4B6A7644586B6E6F4E6736455" +
            "F696F544969766C70716E467734617752314B705857674D497767477367612D6E30437365716C4156395144554F7A66752D623" +
            "7733058694C6738586E3147647649486A613447324A7641765F6A4C6353597950387971683641363373613657364D313178796" +
            "A373341677948392D6C3657556B64553433743244474648597A336C6632386F5749764D546A74754C736C38435974416F6D563" +
            "7766B6C6F51563161706371625A6C647448766E72676276676A494847555572444C58657730313457453B205349443D4451414" +
            "1414C4141414143765755456B4D3142686C6E692D63624E57516C31704C757561537042563079495148544158646F443154666" +
            "27173325A3641676330547A5A48385F6D3857797A77574A42555055704D4E486B2D6758786D5658787A664D7A42454A7338497" +
            "6756B655442777036644131684877374B456D6B614F6647326C4F4C7A713041596D36676F5339375F485F484A3266613671313" +
            "04D43584533736F3734672D347853476C33637631317458626A50395836726B6E78344F4A496575706E68694A56656C5361566" +
            "750373447615F61545A375F5A5F50614A6D552D485578746B66333573465A4276744F34465A513B204F4750433D34303631313" +
            "5352D32333A3B204F47503D2D343036313135353A3B20505245463D49443D616639656538316235343936363363613A553D333" +
            "665613666653664353032666631663A46463D303A4C443D72753A4E523D31303A544D3D313430353430383935303A4C4D3D313" +
            "431303434313737373A53473D333A533D6457513855344B6F69352D59766E366F0D0A436F6E6E656374696F6E3A204B6565702D416C6976650D0A0D0A|";

        adapter = new SyslogAdapter();
        ILogCollector mockLogCollector = EasyMock.createMock(ILogCollector.class);
        adapter.setLogCollector(mockLogCollector);

        SnortLogData snortLogData = new SnortLogData();

        snortLogData.setProgram("snort");
        snortLogData.setSensorName("snortIds1-eth1");
        snortLogData.setDate(new DateTime("2014-09-11T16:23:01.479Z").toDate());
        snortLogData.setPriority(1);
        snortLogData.setClassification("[1:1310:5] Snort Alert [1:1310:5]");
        snortLogData.setAlertCause("policy-violation");
        snortLogData.setGeneratorId(1);
        snortLogData.setSignatureId(1310);
        snortLogData.setSignatureRevision(5);
        snortLogData.setProtocolNumber(6);
        snortLogData.setProtocolAlias("TCP");
        snortLogData.setProtocolVersion(4);
        snortLogData.setSourceIp("192.168.1.137");
        snortLogData.setDestinationIp("173.194.39.114");
        snortLogData.setHeaderLength(20);
        snortLogData.setDatagramLength(1113);
        snortLogData.setIdentification(40840);
        snortLogData.setFlags(2);
        snortLogData.setTimeToLive(65456);
        snortLogData.setSourcePort(55536);
        snortLogData.setDestinationPort(80);
        snortLogData.setHost("www.google.com");

        mockLogCollector.saveSnortLog(snortLogData);

        EasyMock.replay(mockLogCollector);

        adapter.event(null, null, new SyslogServerEvent(syslogMessage, InetAddress.getLocalHost()));

        EasyMock.verify(mockLogCollector);
    }
}
