package com.payneteasy.srvlog.adapter.syslog;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Date: 04.01.13
 */
@Service
public class SpringLogCollectorConfig implements SyslogAdapterConfig {
    @Override
    public String getSyslogProtocol() {
        return syslog4jProtocol;
    }

    @Override
    public int getSyslogPort() {
        return syslog4jPort;
    }

    @Value( "${port}" )
    private int syslog4jPort;

    @Value( "${protocol}" )
    private String syslog4jProtocol;
}
