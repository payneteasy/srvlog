package com.payneteasy.srvlog.config;

import com.payneteasy.startup.parameters.AStartupParameter;

public interface IStartupConfig {

    @AStartupParameter(name = "JETTY_CONTEXT", value = "/srvlog")
    String getJettyContext();

    @AStartupParameter(name = "JETTY_PORT", value = "8080")
    int getJettyPort();

    @AStartupParameter(name = "JETTY_ENV_CONFIG_PATH", value = "")
    String getJettyEnvConfigPath();

    @AStartupParameter(name = "WEB_DESCRIPTOR_PATH", value = "")
    String webDescriptorPath();

    @AStartupParameter(name = "WEB_SOCKET_ENDPOINT_PATH", value = "/ws-log")
    String webSocketEndpointPath();

    @AStartupParameter(name = "WEB_SOCKET_MAX_MESSAGE_SIZE", value = "65535")
    long webSocketMaxMessageSize();

    @AStartupParameter(name = "WEB_SOCKET_IDLE_TIMEOUT_SECONDS", value = "300")
    int webSocketIdleTimeoutSeconds();
}
