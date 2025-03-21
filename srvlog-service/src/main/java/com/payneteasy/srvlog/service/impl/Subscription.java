package com.payneteasy.srvlog.service.impl;

import java.util.Collections;
import java.util.List;

public class Subscription {

    private final List<String> subscriptionHosts;
    private final List<String> subscriptionPrograms;
    private final State subscriptionState;

    public Subscription(List<String> subscriptionHosts, List<String> subscriptionPrograms, State subscriptionState) {
        this.subscriptionHosts = subscriptionHosts;
        this.subscriptionPrograms = subscriptionPrograms;
        this.subscriptionState = subscriptionState;
    }

    public enum State {
        INITIAL, ONLINE_BROADCASTING
    }

    public static Subscription initialState() {
        return new Subscription(Collections.emptyList(), Collections.emptyList(), State.INITIAL);
    }

    public List<String> getSubscriptionHosts() {
        return subscriptionHosts;
    }

    public List<String> getSubscriptionPrograms() {
        return subscriptionPrograms;
    }

    public State getSubscriptionState() {
        return subscriptionState;
    }

    public synchronized boolean isBroadcastCandidateFor(String host, String program) {
        return Subscription.State.ONLINE_BROADCASTING.equals(this.getSubscriptionState())
                && subscriptionHosts.contains(host)
                && subscriptionPrograms.contains(program);
    }
}
