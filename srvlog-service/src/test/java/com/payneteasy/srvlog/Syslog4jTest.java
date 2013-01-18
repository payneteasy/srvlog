package com.payneteasy.srvlog;

import com.google.common.base.Charsets;
import com.nesscomputing.syslog4j.Syslog;
import com.nesscomputing.syslog4j.SyslogIF;
import com.nesscomputing.syslog4j.SyslogLevel;
import com.nesscomputing.syslog4j.server.SyslogServer;
import com.nesscomputing.syslog4j.server.SyslogServerEventIF;
import com.nesscomputing.syslog4j.server.SyslogServerIF;
import com.nesscomputing.syslog4j.server.SyslogServerSessionlessEventHandlerIF;
import com.nesscomputing.syslog4j.server.impl.event.printstream.FileSyslogServerEventHandler;
import com.nesscomputing.syslog4j.server.impl.event.printstream.PrintStreamSyslogServerEventHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.*;
import java.net.SocketAddress;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Date: 03.01.13 Time: 13:11
 */

@Ignore
public class Syslog4jTest {

    private static final String PROTOCOL = "udp";
    private static final int PORT = 1514;
    public static final String HELLO_SYSLOG = "Hello Syslog!!";
    private SyslogServerIF udpSyslogInstance;
    private SyslogIF udpSyslogClient;

    @Before
    public void setUp() {
        udpSyslogInstance = SyslogServer.getInstance(PROTOCOL);
        udpSyslogInstance.getConfig().setPort(PORT);

        udpSyslogClient = Syslog.getInstance(PROTOCOL);
        udpSyslogClient.getConfig().setSendLocalName(false);
        udpSyslogClient.getConfig().setPort(PORT);
    }

    @After
    public void tearDown() {
        udpSyslogInstance.shutdown();
        udpSyslogClient.shutdown();
    }

    @Test
    @Ignore
    public void testServerStart() {
        final String[] message = new String[1];

        final SyslogLevel[] level = new SyslogLevel[1];

        SyslogEventHandler eventHandler = new SyslogEventHandler();
        udpSyslogInstance.getConfig().addEventHandler(eventHandler);

        SyslogServer.getThreadedInstance(PROTOCOL);

        udpSyslogClient.info(HELLO_SYSLOG);

        for (int i = 0; i < 5; i++) {
            try {
                if(eventHandler.message == null){
                    Thread.sleep(10);
                }else {
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        assertNotNull("Message was not obtained", eventHandler.message);
        assertEquals("Syslog Adapter has to properly handle messages", HELLO_SYSLOG, eventHandler.message);
    }

    @Test
    @Ignore
    public void sendFromFile()  {

    }

    public class SyslogEventHandler implements SyslogServerSessionlessEventHandlerIF {

        public String message;
        public String host;

        @Override
        public void event(SyslogServerIF syslogServer, SocketAddress socketAddress, SyslogServerEventIF event) {
            System.out.println("message = " + event.getMessage());
            message = new String(event.getMessage());
            host = event.getHost();
        }

        @Override
        public void exception(SyslogServerIF syslogServer, SocketAddress socketAddress, Exception exception) { }

        @Override
        public void initialize(SyslogServerIF syslogServer) { }

        @Override
        public void destroy(SyslogServerIF syslogServer) { }
    }

}
