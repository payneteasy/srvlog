package com.payneteasy.service.impl;

import com.nesscomputing.syslog4j.server.SyslogServerEventIF;
import com.nesscomputing.syslog4j.server.SyslogServerIF;
import com.payneteasy.dao.ILogManagerDao;
import com.payneteasy.service.ILogManagerEventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.SocketAddress;

/**
 * Date: 03.01.13 Time: 16:32
 */
@Service
public class LogManagerEventHandlerImpl implements ILogManagerEventHandler {

    @Override
    public void event(SyslogServerIF syslogServer, SocketAddress socketAddress, SyslogServerEventIF event) {
        logManagerDao.saveLogMessage(event.getMessage());
    }

    @Override
    public void exception(SyslogServerIF syslogServer, SocketAddress socketAddress, Exception exception) {
    }

    @Override
    public void initialize(SyslogServerIF syslogServer) {
    }

    @Override
    public void destroy(SyslogServerIF syslogServer) {
    }

    @Autowired
    private ILogManagerDao logManagerDao;
}
