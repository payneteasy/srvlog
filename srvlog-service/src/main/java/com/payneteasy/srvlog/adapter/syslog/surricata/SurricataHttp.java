package com.payneteasy.srvlog.adapter.syslog.surricata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SurricataHttp {
    private String hostname;

    public String getHostname() {
        return hostname;
    }
}
