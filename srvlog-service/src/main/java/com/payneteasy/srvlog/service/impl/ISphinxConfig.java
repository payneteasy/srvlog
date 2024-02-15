package com.payneteasy.srvlog.service.impl;

import com.payneteasy.startup.parameters.AStartupParameter;

public interface ISphinxConfig {

    @AStartupParameter(name = "SPHINX_HOST", value = "localhost")
    String getHost();

    @AStartupParameter(name = "SPHINX_PORT", value = "9312")
    int getPort();

    @AStartupParameter(name = "SPHINX_CONNECT_TIMEOUT", value = "30000")
    int getConnectTimeout();

    @AStartupParameter(name = "SPHINX_QUERY_INDEXES", value = "srvlog_index_main,srvlog_index_delta")
    String getQueryIndexes();
}
