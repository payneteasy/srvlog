package com.payneteasy.srvlog.adapter.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;

import java.net.InetAddress;

/**
 * Date: 19.06.13
 * Time: 20:49
 */
public class ServerLogbackEvent {
    private final ILoggingEvent logEvent;
    private final InetAddress host;

    public ServerLogbackEvent(ILoggingEvent logEvent, InetAddress host) {
        this.logEvent = logEvent;
        this.host = host;
    }

    public ILoggingEvent getLogEvent() {
        return logEvent;
    }

    public InetAddress getHost() {
        return host;
    }
}
