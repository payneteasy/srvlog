package com.payneteasy.srvlog.adapter.syslog;

import com.nesscomputing.syslog4j.server.impl.event.SyslogServerEvent;
import static com.payneteasy.srvlog.adapter.syslog.OssecSnortMessage.createOssecSnortMessage;
import static com.payneteasy.srvlog.adapter.syslog.OssecSnortMessage.getRawMessage;
import static java.net.InetAddress.getByName;
import java.net.UnknownHostException;
import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author imenem
 */
public class OssecSnortMessageTest {

    private static final String MESSAGE =
        "Jun  9 16:08:10 log-1 ossec: Alert Level: 10; Rule: 20121 - MIDSE on nginx-1; " +
        "Location: (firewall) 10.0.1.1->/var/log/messages; srcip: 185.56.80.125; dstip: 10.0.2.4; " +
        "Jun  9 16:08:10 firewall snort[20441]: [1:2016979:3] ET WEB_SERVER suhosin.simulation PHP config option in uri " +
        "[Classification: A Network Trojan was detected] [Priority: 1] <eth3> {TCP} 185.56.80.125:59841 -> 10.0.2.4:4027";

    @Test
    public void testCreateOssecSnortMessage() {
        OssecSnortMessage snortMessage = createOssecSnortMessage(MESSAGE);

        assertEquals("[1:2016979:3]", snortMessage.getIdentifier());
        assertEquals("87a00f1e64a75d5b357d8ee3b4d71126", snortMessage.getHash());
    }

    @Test
    public void testGetRawMessage() throws UnknownHostException {
        String rawMessage = getRawMessage(new SyslogServerEvent("<132>" + MESSAGE, getByName("127.0.0.1")));

        assertEquals(MESSAGE, rawMessage);
    }

    @Test
    public void isSnortMessageFromOssec() {
        Assert.assertTrue(OssecSnortMessage.isSnortMessageFromOssec("Jun  9 16:08:10 log-1 ossec: Alert Level: 3; Rule: 20110 - (null); Location: (fw-1) 10.2.24.11->/var/log/messages; classification:  ids,; srcip: 86.178.90.83; dstip: 185.15.175.18; Jun 21 17:39:37 fw-1 snort[9492]: [137:1:2] (spp_ssl) Invalid Client HELLO after Server HELLO Detected [Classification: Potentially Bad Traffic] [Priority: 2] <eth0.100> {TCP} 86.178.90.83:57005 -> 185.15.175.18:443"));
        Assert.assertTrue(OssecSnortMessage.isSnortMessageFromOssec("Jun  9 16:08:10 log-1 ossec: Alert Level: 3; Rule: 20150 - (null); Location: (fw-ipmi-1) 10.6.0.1->/var/log/messages; classification:  ids,; srcip: 10.6.0.1; dstip: 10.6.0.6; Jun 21 17:38:49 fw-ipmi-1 suricata[694]: [1:2008983:6] ET USER_AGENTS Suspicious User Agent (BlackSun) [Classification: A Network Trojan was detected] [Priority: 1] <eth3> {TCP} 10.6.0.1:55324 -> 10.6.0.6:80"));
        Assert.assertFalse(OssecSnortMessage.isSnortMessageFromOssec("Jun  9 16:08:10 log-1 ossec: Alert Level: 3; Rule: 20150 - (null); Location: (fw-ipmi-1) 10.6.0.1->/var/log/messages; classification:  ids,; srcip: 10.6.0.1; dstip: 10.6.0.6; Jun 21 17:38:49 fw-ipmi-1 hello[694]: [1:2008983:6] ET USER_AGENTS Suspicious User Agent (BlackSun) [Classification: A Network Trojan was detected] [Priority: 1] <eth3> {TCP} 10.6.0.1:55324 -> 10.6.0.6:80"));
    }
}
