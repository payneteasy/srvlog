package com.payneteasy.srvlog.service.impl;

public class LogBroadcastingRequest {

    private String host;
    private String program;
    private String subscriptionState;

    public LogBroadcastingRequest() {
    }

    public LogBroadcastingRequest(String host, String program, String subscriptionState) {
        this.host = host;
        this.program = program;
        this.subscriptionState = subscriptionState;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getSubscriptionState() {
        return subscriptionState;
    }

    public void setSubscriptionState(String subscriptionState) {
        this.subscriptionState = subscriptionState;
    }
}