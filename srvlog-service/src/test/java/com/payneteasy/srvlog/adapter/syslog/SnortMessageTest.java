package com.payneteasy.srvlog.adapter.syslog;

import static com.payneteasy.srvlog.adapter.syslog.SnortMessage.parseSnortMessage;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * @author imenem
 */
public class SnortMessageTest {
    private final static String PAYLOAD =
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

    private final static String FULL_MESSAGE =
        "| [SNORTIDS[LOG]: [snortIds1-eth1] ] |" +
        "| 2014-09-11 16:23:01.479 1 [1:1310:5] Snort Alert [1:1310:5] |" +
        "| policy-violation |" +
        "| 6 192.168.1.137 173.194.39.114 4 20 0 1113 40840 2 0 65456 0 |" +
        "| 55536 80 3885499888 1996581709 8 0 24 229 41947 0 |" +
        PAYLOAD;

    private final static String MESSAGE_WITHOUT_HEADERS =
        "| [SNORTIDS[LOG]: [snortIds1-eth1] ] |" +
        "| 2014-09-11 16:23:01.479 1 [1:1310:5] Snort Alert [1:1310:5] |" +
        "| policy-violation |" +
        PAYLOAD;

    private final static String MESSAGE_WITH_TCP_HEADER =
        "| [SNORTIDS[LOG]: [snortIds1-eth1] ] |" +
        "| 2014-09-11 16:23:01.479 1 [1:1310:5] Snort Alert [1:1310:5] |" +
        "| policy-violation |" +
        "| 6 192.168.1.137 173.194.39.114 4 20 0 1113 40840 2 0 65456 0 |" +
        "| 55536 80 3885499888 1996581709 8 0 24 229 41947 0 |" +
        PAYLOAD;

    private final static String MESSAGE_WITH_UDP_HEADER =
        "| [SNORTIDS[LOG]: [snortIds1-eth1] ] |" +
        "| 2014-09-11 16:23:01.479 1 [1:1310:5] Snort Alert [1:1310:5] |" +
        "| policy-violation |" +
        "| 17 192.168.1.137 173.194.39.114 4 20 0 1113 40840 2 0 65456 0 |" +
        "| 55536 80 512 41947 |" +
        PAYLOAD;

    private final static String MESSAGE_WITH_ICMP_HEADER =
        "| [SNORTIDS[LOG]: [snortIds1-eth1] ] |" +
        "| 2014-09-11 16:23:01.479 1 [1:1310:5] Snort Alert [1:1310:5] |" +
        "| policy-violation |" +
        "| 1 192.168.1.137 173.194.39.114 4 20 0 1113 40840 2 0 65456 0 |" +
        "| 536 18 512 1947 45 |" +
        PAYLOAD;

    @Test
    public void testParseSnortMessageWithFullMEssage() {
        SnortMessage snortMessage = parseSnortMessage(FULL_MESSAGE);
        String stringMessage = snortMessage.toString();

        assertRequiredMessagePartsExisted(stringMessage);
        assertMessagePartExisted(stringMessage, "IP header: proto: TCP; sip: 192.168.1.137; dip: 173.194.39.114; ver: 4; hlen: 20; tos: 0; len: 1113; id: 40840; flags: 2; off: 0; ttl: 65456; csum: 0;");
        assertMessagePartExisted(stringMessage, "Protocol header: sp: 55536; dp: 80;");
    }

    @Test
    public void testParseSnortMessageWithMessageWithoutHeaders() {
        SnortMessage snortMessage = parseSnortMessage(MESSAGE_WITHOUT_HEADERS);
        String stringMessage = snortMessage.toString();

        assertRequiredMessagePartsExisted(stringMessage);
        assertMessagePartNotExisted(stringMessage, "IP header:");
        assertMessagePartNotExisted(stringMessage, "Protocol header:");
    }

    @Test
    public void testParseSnortMessageWithMessageWithTcpHeader() {
        SnortMessage snortMessage = parseSnortMessage(MESSAGE_WITH_TCP_HEADER);
        String stringMessage = snortMessage.toString();

        assertRequiredMessagePartsExisted(stringMessage);
        assertMessagePartExisted(stringMessage, "IP header: proto: TCP;");
        assertMessagePartExisted(stringMessage, "Protocol header: sp: 55536; dp: 80;");
    }

    @Test
    public void testParseSnortMessageWithMessageWithUdpHeader() {
        SnortMessage snortMessage = parseSnortMessage(MESSAGE_WITH_UDP_HEADER);
        String stringMessage = snortMessage.toString();

        assertRequiredMessagePartsExisted(stringMessage);
        assertMessagePartExisted(stringMessage, "IP header: proto: UDP;");
        assertMessagePartExisted(stringMessage, "Protocol header: sp: 55536; dp: 80;");
    }

    @Test
    public void testParseSnortMessageWithMessageWithIcmpHeader() {
        SnortMessage snortMessage = parseSnortMessage(MESSAGE_WITH_ICMP_HEADER);
        String stringMessage = snortMessage.toString();

        assertRequiredMessagePartsExisted(stringMessage);
        assertMessagePartExisted(stringMessage, "IP header: proto: ICMP;");
        assertMessagePartNotExisted(stringMessage, "Protocol header:");
    }

    private void assertMessagePartExisted(String snortMessage, String messagePart) {
        assertTrue(
            "Message part must be existed " + messagePart + " in message " + snortMessage,
            snortMessage.contains(messagePart)
        );
    }

    private void assertMessagePartNotExisted(String snortMessage, String messagePart) {
        assertFalse(
            "Message part must not be existed " + messagePart + " in message " + snortMessage,
            snortMessage.contains(messagePart)
        );
    }

    private void assertRequiredMessagePartsExisted(String snortMessage) {
        assertMessagePartExisted(snortMessage, "Date: 2014-09-11 16:23:01.479");
        assertMessagePartExisted(snortMessage, "Priority: 1");
        assertMessagePartExisted(snortMessage, "Classification: [1:1310:5] Snort Alert [1:1310:5]");
        assertMessagePartExisted(snortMessage, "Alert cause: policy-violation");
        assertMessagePartExisted(snortMessage, "GET /search?q=fetish HTTP/1.1");   // check payload
    }
}
