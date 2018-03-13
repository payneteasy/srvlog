package com.payneteasy.srvlog.adapter.syslog.surricata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SurricataAlert {

    private String category;
    private int    severity;
    private String signature;
    private int    gid;

    @JsonProperty("signature_id")
    private int    signature_id;

    @JsonProperty("rev")
    private int    rev;

    public int getGid() {
        return gid;
    }

    public String getCategory() {
        return category;
    }

    public int getSignatureId() {
        return signature_id;
    }

    public int setSignatureRevision() {
        return rev;
    }

    public int getSeverity() {
        return severity;
    }

    public String getSignature() {
        return signature;
    }
}
