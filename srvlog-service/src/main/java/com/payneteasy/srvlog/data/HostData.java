package com.payneteasy.srvlog.data;

import javax.persistence.Column;
import javax.persistence.Id;

/**
 * Date: 12.01.13
 * Time: 22:39
 */
public class HostData {


    private Long id;


    private String hostname;


    private String ipAddress;

    @Id
    @Column(name = "host_id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }


    @Column(name = "hostname")
    public String getHostname() {
        return hostname;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Column(name = "ip")
    public String getIpAddress() {
        return ipAddress;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HostData)) return false;

        HostData hostData = (HostData) o;

        if (!hostname.equals(hostData.hostname)) return false;
        if (!id.equals(hostData.id)) return false;
        if (ipAddress != null ? !ipAddress.equals(hostData.ipAddress) : hostData.ipAddress != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + hostname.hashCode();
        result = 31 * result + (ipAddress != null ? ipAddress.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "HostData{" +
                "id=" + id +
                ", hostname='" + hostname + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                '}';
    }
}
