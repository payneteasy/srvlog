package com.payneteasy.srvlog.adapter.syslog;

import com.nesscomputing.syslog4j.server.impl.event.structured.StructuredSyslogServerEvent;
import org.junit.Assert;
import org.junit.Test;

import java.net.InetAddress;

public class StructuredSyslogServerEventTest {

    //                                                     <45>1 2018-03-14T03:26:01+03:00 fw-ipmi-1 syslog-ng 747  Syslog connection established; fd='8', server='AF_INET(10.0.0.115:2514)', local='AF_INET(0.0.0.0:0)'
    private static final String ValidStructuredMessage = "<165>1 2012-12-25T22:14:15.003Z mymachine.example.com evntslog - ID47 [exampleSDID@32473 iut=\"3\" eventSource=\"Application\" eventID=\"1011\"] BOMAn application event log entry";
    private static final String ValidStructuredMultiMessage = "<165>1 2012-12-25T22:14:15.003Z mymachine.example.com evntslog - ID47 [exampleSDID@32473 iut=\"3\" eventSource=\"Application\" eventID=\"1011\"][meta sequenceId=\"1\"] BOMAn application event log entry";
    private static final String ValidStructuredMultiMessageSameKey = "<165>1 2012-12-25T22:14:15.003Z mymachine.example.com evntslog - ID47 [exampleSDID@32473 iut=\"3\" eventID=\"1011\"][meta iut=\"10\"] BOMAn application event log entry";
    private static final String ValidStructuredNoStructValues = "<165>1 2012-12-25T22:14:15.003Z mymachine.example.com evntslog - ID47 - BOMAn application event log entry";
//    private static final String MessageLookingLikeStructured = "<133>NOMA101FW01A: NetScreen device_id=NOMA101FW01A [Root]system-notification-00257(traffic): start_time=\"2011-12 -23 17:33:43\" duration=0 reason=Creation";
    private static final String ValidNonStructuredMessage = "<86>Dec 24 17:05:01 nb-lkoopmann CRON[10049]: pam_unix(cron:session): session closed for user root";

    private InetAddress address = InetAddress.getLoopbackAddress();

    @Test
    public void testApplicationName() {
        check("snoopy", "<86>1 2018-03-14T02:30:42+03:00 fw-ipmi-1 snoopy 28368 - [meta sequenceId=\"5\"] [uid:0 sid:24656 tty:/dev/pts/1 cwd:/etc/syslog-ng filename:/usr/bin/curl]: curl -A BlackSun --header X-MyHeader: 123 www.google.com");
        Assert.assertEquals("evntslog",  new StructuredSyslogServerEvent(ValidStructuredMessage, address).getApplicationName());
        Assert.assertEquals("evntslog",  new StructuredSyslogServerEvent(ValidStructuredMessage, address).getApplicationName());
        Assert.assertEquals("evntslog",  new StructuredSyslogServerEvent(ValidStructuredMultiMessage, address).getApplicationName());
        Assert.assertEquals("evntslog",  new StructuredSyslogServerEvent(ValidStructuredMultiMessageSameKey, address).getApplicationName());
        Assert.assertEquals("evntslog",  new StructuredSyslogServerEvent(ValidStructuredNoStructValues, address).getApplicationName());
//        Assert.assertEquals("evntslog",  new StructuredSyslogServerEvent(MessageLookingLikeStructured, address).getApplicationName());
        Assert.assertEquals("evntslog",  new StructuredSyslogServerEvent(ValidNonStructuredMessage, address).getApplicationName());
    }

    private void check(String app, String message) {
        Assert.assertEquals(app,  new StructuredSyslogServerEvent(message, address).getApplicationName());

    }
}
