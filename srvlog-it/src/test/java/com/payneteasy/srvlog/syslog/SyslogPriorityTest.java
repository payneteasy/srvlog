package com.payneteasy.srvlog.syslog;

import com.nesscomputing.syslog4j.SyslogFacility;
import com.nesscomputing.syslog4j.SyslogLevel;
import com.nesscomputing.syslog4j.server.impl.event.SyslogServerEvent;
import com.payneteasy.srvlog.data.LogFacility;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static junit.framework.Assert.assertEquals;

/**
 * Date: 22.01.13 Time: 12:48
 */
public class SyslogPriorityTest {

    @Test
    public void testPriority() throws UnknownHostException {
        int priority = 26;
        String message = "<26>Jan 12 00:00:00 ";
        SyslogServerEvent event = new SyslogServerEvent(message, InetAddress.getLocalHost());
        assertEquals(SyslogFacility.daemon, event.getFacility());
        assertEquals(SyslogLevel.CRITICAL, event.getLevel());
        System.out.println(" Facility: " + (SyslogFacility.daemon.getValue() >> 3));
        System.out.println(" Level: " + SyslogLevel.CRITICAL.getValue());
        int i1 = (13 % 8);
        int i2 = 13 & ~7;
        System.out.println(i1);
        System.out.println(i2);
        System.out.println(SyslogFacility.user.getValue());
//        System.out.println((1 << 3) == (13 >> 3));

    }
}
