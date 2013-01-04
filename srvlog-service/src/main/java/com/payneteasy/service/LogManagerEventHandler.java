package com.payneteasy.service;

import com.nesscomputing.syslog4j.server.SyslogServerIF;
import com.nesscomputing.syslog4j.server.SyslogServerSessionlessEventHandlerIF;

import java.net.SocketAddress;

/**
 * Date: 04.01.13
 */
public abstract class LogManagerEventHandler implements SyslogServerSessionlessEventHandlerIF {
    @Override
    public void exception(SyslogServerIF syslogServer, SocketAddress socketAddress, Exception exception) {
        //TODO
    }

    @Override
    public void initialize(SyslogServerIF syslogServer) {
        //TODO
    }

    @Override
    public void destroy(SyslogServerIF syslogServer) {
        //TODO
    }
}
