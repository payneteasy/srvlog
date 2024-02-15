package com.payneteasy.srvlog.adapter.logback;

import com.payneteasy.startup.parameters.AStartupParameter;

public interface ILogbackAdapterConfig {

    @AStartupParameter(name = "LOGBACK_PROGRAM", value = "paynet")
    String getProgram();

    @AStartupParameter(name = "LOGBACK_TCP_PORT", value = "4713")
    int getTcpPort();

    @AStartupParameter(name = "LOGBACK_UDP_PORT", value = "4713")
    int getUdpPort();
}
