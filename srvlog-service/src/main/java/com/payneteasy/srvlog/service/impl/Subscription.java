package com.payneteasy.srvlog.service.impl;

public class Subscription {

    private final String subscriptionHost;
    private final String subscriptionProgram;
    private final State subscriptionState;

    public Subscription(String subscriptionHost, String subscriptionProgram, State subscriptionState) {
        this.subscriptionHost = subscriptionHost;
        this.subscriptionProgram = subscriptionProgram;
        this.subscriptionState = subscriptionState;
    }

    public enum State {
        INITIAL, ONLINE_BROADCASTING
    }

    public static Subscription initialState() {
        return new Subscription(null, null, State.INITIAL);
    }

    public String getSubscriptionHost() {
        return subscriptionHost;
    }

    public String getSubscriptionProgram() {
        return subscriptionProgram;
    }

    public State getSubscriptionState() {
        return subscriptionState;
    }

    public synchronized boolean isBroadcastCandidateFor(String host, String program) {
        return Subscription.State.ONLINE_BROADCASTING.equals(this.getSubscriptionState())
                && host.equalsIgnoreCase(this.getSubscriptionHost())
                && program.equalsIgnoreCase(this.getSubscriptionProgram());
    }
}
