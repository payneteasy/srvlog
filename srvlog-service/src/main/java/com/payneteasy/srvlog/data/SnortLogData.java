package com.payneteasy.srvlog.data;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Id;

/**
 * Data transfer object.
 * This class uses to save multilevel object structure to plain database table.
 * Jdbc-proc doesn't save multilevel object structure.
 *
 * @author imenem
 */
public class SnortLogData {

    private Long id;
    private Long logId;
    private String program;
    private String sensorName;
    private Date date;
    private int priority;
    private String classification;
    private String alertCause;
    private String payload;
    private int protocolNumber;
    private String protocolAlias;
    private int protocolVersion;
    private String sourceIp;
    private String destinationIp;
    private int headerLength;
    private int serviceType;
    private int datagramLength;
    private int identification;
    private int flags;
    private int fragmentOffset;
    private int timeToLive;
    private int checksum;
    private int sourcePort;
    private int destinationPort;

    @Id
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "log_id")
    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    @Column(name = "program")
    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    @Column(name = "sensor_name")
    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    @Column(name = "date")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Column(name = "priority")
    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Column(name = "classification")
    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    @Column(name = "alert_cause")
    public String getAlertCause() {
        return alertCause;
    }

    public void setAlertCause(String alertCause) {
        this.alertCause = alertCause;
    }

    @Column(name = "payload")
    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    @Column(name = "protocol_number")
    public int getProtocolNumber() {
        return protocolNumber;
    }

    public void setProtocolNumber(int protocolNumber) {
        this.protocolNumber = protocolNumber;
    }

    @Column(name = "protocol_alias")
    public String getProtocolAlias() {
        return protocolAlias;
    }

    public void setProtocolAlias(String protocolAlias) {
        this.protocolAlias = protocolAlias;
    }

    @Column(name = "protocol_version")
    public int getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(int protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    @Column(name = "source_ip")
    public String getSourceIp() {
        return sourceIp;
    }

    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }

    @Column(name = "destination_ip")
    public String getDestinationIp() {
        return destinationIp;
    }

    public void setDestinationIp(String destinationIp) {
        this.destinationIp = destinationIp;
    }

    @Column(name = "header_length")
    public int getHeaderLength() {
        return headerLength;
    }

    public void setHeaderLength(int headerLength) {
        this.headerLength = headerLength;
    }

    @Column(name = "service_type")
    public int getServiceType() {
        return serviceType;
    }

    public void setServiceType(int serviceType) {
        this.serviceType = serviceType;
    }

    @Column(name = "datagram_length")
    public int getDatagramLength() {
        return datagramLength;
    }

    public void setDatagramLength(int datagramLength) {
        this.datagramLength = datagramLength;
    }

    @Column(name = "identification")
    public int getIdentification() {
        return identification;
    }

    public void setIdentification(int identification) {
        this.identification = identification;
    }

    @Column(name = "flags")
    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    @Column(name = "fragment_offset")
    public int getFragmentOffset() {
        return fragmentOffset;
    }

    public void setFragmentOffset(int fragmentOffset) {
        this.fragmentOffset = fragmentOffset;
    }

    @Column(name = "time_to_live")
    public int getTimeToLive() {
        return timeToLive;
    }

    public void setTimeToLive(int timeToLive) {
        this.timeToLive = timeToLive;
    }

    @Column(name = "checksum")
    public int getChecksum() {
        return checksum;
    }

    public void setChecksum(int checksum) {
        this.checksum = checksum;
    }

    @Column(name = "source_port")
    public int getSourcePort() {
        return sourcePort;
    }

    public void setSourcePort(int sourcePort) {
        this.sourcePort = sourcePort;
    }

    @Column(name = "destination_port")
    public int getDestinationPort() {
        return destinationPort;
    }

    public void setDestinationPort(int destinationPort) {
        this.destinationPort = destinationPort;
    }

}
