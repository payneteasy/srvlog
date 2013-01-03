package com.payneteasy.service;

import com.nesscomputing.syslog4j.server.SyslogServerIF;

/**
 * Date: 03.01.13 Time: 15:04
 */
public interface ILogManager {
    boolean isStarted();
    String getSyslog4jProtocol();
    int getSyslog4jPort();
}
