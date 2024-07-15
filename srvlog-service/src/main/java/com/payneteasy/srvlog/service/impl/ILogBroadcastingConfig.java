package com.payneteasy.srvlog.service.impl;

import com.payneteasy.startup.parameters.AStartupParameter;

public interface ILogBroadcastingConfig {

    @AStartupParameter(name = "LOG_STORAGE_CAPACITY", value = "1000")
    int getProgramLogStorageCapacity();
}
