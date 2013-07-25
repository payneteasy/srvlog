package com.payneteasy.srvlog.adapter.log4j;

import org.apache.log4j.spi.LoggingEvent;

import java.net.InetAddress;

/**
 * Date: 19.06.13
 * Time: 20:49
 */
public class ServerLog4jEvent {
    private final LoggingEvent logEvent;
    private final InetAddress host;

    public ServerLog4jEvent(LoggingEvent logEvent, InetAddress host) {
        this.logEvent = logEvent;
        this.host = host;
    }

    public LoggingEvent getLogEvent() {
        return logEvent;
    }

    public InetAddress getHost() {
        return host;
    }
}
