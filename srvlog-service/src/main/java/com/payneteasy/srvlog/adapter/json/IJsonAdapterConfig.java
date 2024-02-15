package com.payneteasy.srvlog.adapter.json;

import com.payneteasy.startup.parameters.AStartupParameter;

public interface IJsonAdapterConfig {

    @AStartupParameter(name = "JSON_ADAPTER_PORT", value = "28080")
    int getWebServerPort();

    @AStartupParameter(name = "JSON_ADAPTER_BIND_ADDRESS", value = "127.0.0.1")
    String getWebServerBindAddress();

    @AStartupParameter(name = "JSON_ADAPTER_TOKEN", value = "test-token-197dc68c-34e5-11eb-9fbd-7bb165d4566c", maskVariable = true)
    String getToken();

    @AStartupParameter(name = "JSON_ADAPTER_PATH", value = "/save-logs")
    String getPath();

}
