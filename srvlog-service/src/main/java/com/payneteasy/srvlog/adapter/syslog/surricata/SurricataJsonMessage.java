package com.payneteasy.srvlog.adapter.syslog.surricata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SurricataJsonMessage {

    private Date           timestamp;
    private String         in_iface;
    private String         src_ip;
    private int            src_port;
    private String         dest_ip;
    private int            dest_port;
    private String         port;
    private String         payload;
    private String         packet;
    private String         proto;

    private SurricataAlert alert;
    private SurricataHttp  http;

    public Date getTimestamp() {
        return timestamp;
    }

    public String getIn_iface() {
        return in_iface;
    }

    public String getSrc_ip() {
        return src_ip;
    }

    public int getSrc_port() {
        return src_port;
    }

    public SurricataAlert getAlert() {
        return alert != null ? alert : new SurricataAlert();
    }

    public String getDest_ip() {
        return dest_ip;
    }

    public int getDest_port() {
        return dest_port;
    }

    public String getPort() {
        return port;
    }

    public String getPayload() {
        return payload;
    }

    public String getPacket() {
        return packet;
    }

    @Override
    public String toString() {
        return "SurricataJsonMessage{" +
                "timestamp=" + timestamp +
                ", in_iface='" + in_iface + '\'' +
                ", src_ip='" + src_ip + '\'' +
                ", src_port=" + src_port +
                ", dest_ip='" + dest_ip + '\'' +
                ", dest_port=" + dest_port +
                ", port='" + port + '\'' +
                ", payload='" + payload + '\'' +
                ", packet='" + packet + '\'' +
                ", proto='" + proto + '\'' +
                ", alert=" + alert +
                ", http=" + http +
                '}';
    }

    public String getProto() {
        return proto;
    }

    public SurricataHttp getHttp() {
        return http != null ? http : new SurricataHttp();
    }
}
