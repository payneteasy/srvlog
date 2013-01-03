package com.payneteasy.service;

import com.nesscomputing.syslog4j.server.SyslogServerEventHandlerIF;
import com.nesscomputing.syslog4j.server.SyslogServerEventIF;
import com.nesscomputing.syslog4j.server.SyslogServerIF;
import com.nesscomputing.syslog4j.server.SyslogServerSessionlessEventHandlerIF;
import org.springframework.stereotype.Service;

import java.net.SocketAddress;

/**
 * Date: 03.01.13 Time: 16:29
 */
@Service
public interface ILogManagerEventHandler extends SyslogServerSessionlessEventHandlerIF {

}
