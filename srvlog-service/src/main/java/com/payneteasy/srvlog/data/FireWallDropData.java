package com.payneteasy.srvlog.data;

import java.io.Serializable;

/**
 * Date: 17.02.13 Time: 17:16
 */
public class FirewallDropData implements Serializable{
    private String sourceIp;
    private String destinationIp;
    private String sourcePort;
    private String destinationPort;
    private String protocol;
    private Long dropCount;

    public String getSourceIp() {
        return sourceIp;
    }

    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }

    public String getDestinationIp() {
        return destinationIp;
    }

    public void setDestinationIp(String destinationIp) {
        this.destinationIp = destinationIp;
    }

    public String getSourcePort() {
        return sourcePort;
    }

    public void setSourcePort(String sourcePort) {
        this.sourcePort = sourcePort;
    }

    public String getDestinationPort() {
        return destinationPort;
    }

    public void setDestinationPort(String destinationPort) {
        this.destinationPort = destinationPort;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public Long getDropCount() {
        return dropCount;
    }

    public void setDropCount(Long dropCount) {
        this.dropCount = dropCount;
    }

    @Override
    public String toString() {
        return "FirewallDropData{" +
                "sourceIp='" + sourceIp + '\'' +
                ", destinationIp='" + destinationIp + '\'' +
                ", sourcePort='" + sourcePort + '\'' +
                ", destinationPort='" + destinationPort + '\'' +
                ", protocol='" + protocol + '\'' +
                ", dropCount=" + dropCount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FirewallDropData)) return false;

        FirewallDropData that = (FirewallDropData) o;

        if (destinationIp != null ? !destinationIp.equals(that.destinationIp) : that.destinationIp != null)
            return false;
        if (destinationPort != null ? !destinationPort.equals(that.destinationPort) : that.destinationPort != null)
            return false;
        if (dropCount != null ? !dropCount.equals(that.dropCount) : that.dropCount != null) return false;
        if (protocol != null ? !protocol.equals(that.protocol) : that.protocol != null) return false;
        if (sourceIp != null ? !sourceIp.equals(that.sourceIp) : that.sourceIp != null) return false;
        if (sourcePort != null ? !sourcePort.equals(that.sourcePort) : that.sourcePort != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = sourceIp != null ? sourceIp.hashCode() : 0;
        result = 31 * result + (destinationIp != null ? destinationIp.hashCode() : 0);
        result = 31 * result + (sourcePort != null ? sourcePort.hashCode() : 0);
        result = 31 * result + (destinationPort != null ? destinationPort.hashCode() : 0);
        result = 31 * result + (protocol != null ? protocol.hashCode() : 0);
        result = 31 * result + (dropCount != null ? dropCount.hashCode() : 0);
        return result;
    }
}
