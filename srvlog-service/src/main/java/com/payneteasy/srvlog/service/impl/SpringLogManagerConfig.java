package com.payneteasy.srvlog.service.impl;

import com.payneteasy.srvlog.service.ILogManagerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Date: 04.01.13
 */
@Service
public class SpringLogManagerConfig implements ILogManagerConfig {
    @Override
    public String getSyslog4jProtocol() {
        return syslog4jProtocol;
    }

    @Override
    public int getSyslog4jPort() {
        return syslog4jPort;
    }

    @Value( "${port}" )
    private int syslog4jPort;

    @Value( "${protocol}" )
    private String syslog4jProtocol;
}
