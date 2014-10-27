package com.payneteasy.srvlog.adapter.syslog;

import com.nesscomputing.syslog4j.server.impl.event.SyslogServerEvent;
import static com.payneteasy.srvlog.adapter.syslog.OssecSnortMessage.createOssecSnortMessage;
import static com.payneteasy.srvlog.adapter.syslog.OssecSnortMessage.getRawMessage;
import static java.net.InetAddress.getByName;
import java.net.UnknownHostException;
import static org.junit.Assert.assertEquals;
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
        assertEquals("43121a82906a94c006faac0b9877af7d", snortMessage.getHash());
    }

    @Test
    public void testGetRawMessage() throws UnknownHostException {
        String rawMessage = getRawMessage(new SyslogServerEvent("<132>" + MESSAGE, getByName("127.0.0.1")));

        assertEquals(MESSAGE, rawMessage);
    }

}
