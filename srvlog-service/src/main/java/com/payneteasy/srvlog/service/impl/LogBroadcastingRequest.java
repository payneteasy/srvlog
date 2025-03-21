package com.payneteasy.srvlog.service.impl;

import java.util.List;

public class LogBroadcastingRequest {

    private List<String> hosts;
    private List<String> programs;
    private String subscriptionState;

    public LogBroadcastingRequest() {
    }

    public LogBroadcastingRequest(List<String> hosts, List<String> programs, String subscriptionState) {
        this.hosts = hosts;
        this.programs = programs;
        this.subscriptionState = subscriptionState;
    }

    public List<String> getHosts() {
        return hosts;
    }

    public void setHosts(List<String> hosts) {
        this.hosts = hosts;
    }

    public List<String> getPrograms() {
        return programs;
    }

    public void setPrograms(List<String> programs) {
        this.programs = programs;
    }

    public String getSubscriptionState() {
        return subscriptionState;
    }

    public void setSubscriptionState(String subscriptionState) {
        this.subscriptionState = subscriptionState;
    }
}