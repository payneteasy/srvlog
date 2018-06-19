package com.payneteasy.srvlog.adapter.syslog;

import com.nesscomputing.syslog4j.server.impl.event.structured.StructuredSyslogServerEvent;
import org.junit.Assert;
import org.junit.Test;

import java.net.InetAddress;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StructuredSyslogServerEventTest {

    // https://tools.ietf.org/html/rfc5424
    String ValidStructuredMessage             = "<165>1 2012-12-25T22:14:15.003Z mymachine.example.com evntslog - ID47 [exampleSDID@32473 iut=\"3\" eventSource=\"Application\" eventID=\"1011\"] BOMAn application event log entry";
    String ValidStructuredMultiMessage        = "<165>1 2012-12-25T22:14:15.003Z mymachine.example.com evntslog - ID47 [exampleSDID@32473 iut=\"3\" eventSource=\"Application\" eventID=\"1011\"][meta sequenceId=\"1\"] BOMAn application event log entry";
    String ValidStructuredMultiMessageSameKey = "<165>1 2012-12-25T22:14:15.003Z mymachine.example.com evntslog - ID47 [exampleSDID@32473 iut=\"3\" eventID=\"1011\"][meta iut=\"10\"] BOMAn application event log entry";
    String ValidStructuredNoStructValues      = "<165>1 2012-12-25T22:14:15.003Z mymachine.example.com evntslog - ID47 - BOMAn application event log entry";

    private static final String MessageLookingLikeStructured = "<133>NOMA101FW01A: NetScreen device_id=NOMA101FW01A [Root]system-notification-00257(traffic): start_time=\"2011-12 -23 17:33:43\" duration=0 reason=Creation";

    // https://tools.ietf.org/search/rfc3164
    private String ValidNonStructuredMessage = "<86>Dec 24 17:05:01 nb-lkoopmann CRON[10049]: pam_unix(cron:session): session closed for user root";

    private InetAddress address = InetAddress.getLoopbackAddress();

    @Test
    public void testApplicationName() {

        // we need to add milliseconds to timestamp
        // for syslog-ng add frac-digits(3) to to config
        check("fw-ipmi-1", "<86>1 2018-03-14T02:30:42+03:00 fw-ipmi-1 snoopy 28368 - [meta sequenceId=\"5\"] [uid:0 sid:24656 tty:/dev/pts/1 cwd:/etc/syslog-ng filename:/usr/bin/curl]: curl -A BlackSun --header X-MyHeader: 123 www.google.com");
        check("syslog-ng", "<45>1 2018-03-14T03:26:01.000+03:00 fw-ipmi-1 syslog-ng 747  Syslog connection established; fd='8', server='AF_INET(10.0.0.115:2514)', local='AF_INET(0.0.0.0:0)'");

        Assert.assertEquals("evntslog",  new StructuredSyslogServerEvent(ValidStructuredMessage, address).getApplicationName());
        Assert.assertEquals("evntslog",  new StructuredSyslogServerEvent(ValidStructuredMessage, address).getApplicationName());
        Assert.assertEquals("evntslog",  new StructuredSyslogServerEvent(ValidStructuredMultiMessage, address).getApplicationName());
        Assert.assertEquals("evntslog",  new StructuredSyslogServerEvent(ValidStructuredMultiMessageSameKey, address).getApplicationName());
        Assert.assertEquals("evntslog",  new StructuredSyslogServerEvent(ValidStructuredNoStructValues, address).getApplicationName());
    }

    private void check(String app, String message) {
        Assert.assertEquals(app,  new StructuredSyslogServerEvent(message, address).getApplicationName());
    }


    @Test
    public void testIsStructuredMessage() {
        assertTrue(isStructuredMessage(ValidStructuredMessage));
        assertTrue(isStructuredMessage(ValidStructuredMultiMessage));
        assertTrue(isStructuredMessage(ValidStructuredMultiMessageSameKey));
        assertTrue(isStructuredMessage(ValidStructuredNoStructValues));
        assertFalse(isStructuredMessage("<13>Dec 22 05:31:55 update monit[1612]: 'rootfs' space usage 80.6% matches resource limit [space usage>80.0%]"));
        assertFalse(isStructuredMessage(ValidNonStructuredMessage));
    }

    private static boolean isStructuredMessage(String receiveData) {
        int idx = receiveData.indexOf('>');

        if (idx != -1 && ((receiveData.length() > idx + 1 && Character.isDigit(receiveData.charAt(idx + 1))))) {
            return true;
        }

        return false;
    }

}
