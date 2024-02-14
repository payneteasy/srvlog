package com.payneteasy.srvlog.adapter.syslog;

import com.payneteasy.startup.parameters.AStartupParameter;

/**
 * Date: 04.01.13
 */
public interface ISyslogAdapterConfig {

    @AStartupParameter(name = "SYSLOG_PROTOCOL", value = "tcp")
    String getSyslogProtocol();

    @AStartupParameter(name = "SYSLOG_HOST", value = "localhost")
    String getSyslogHost();

    @AStartupParameter(name = "SYSLOG_PORT", value = "2514")
    int getSyslogPort();
}
