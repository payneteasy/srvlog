package com.payneteasy.srvlog.data;

import com.payneteasy.srvlog.adapter.syslog.HttpHeader;
import static com.payneteasy.srvlog.adapter.syslog.HttpHeader.createHttpHeader;
import com.payneteasy.srvlog.adapter.syslog.IpHeader;
import static com.payneteasy.srvlog.adapter.syslog.IpHeader.createIpHeader;
import com.payneteasy.srvlog.adapter.syslog.ProtocolHeader;
import static com.payneteasy.srvlog.adapter.syslog.ProtocolHeader.createProtocolHeader;
import com.payneteasy.srvlog.adapter.syslog.SnortSignature;
import static com.payneteasy.srvlog.adapter.syslog.SnortSignature.createSnortSignature;
import java.io.Serializable;
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
public class SnortLogData implements Serializable {

    private Long id;
    private String hash;
    private String program;
    private String sensorName;
    private Date date;
    private int priority;
    private String classification;
    private String alertCause;
    private int generatorId;
    private int signatureId;
    private int signatureRevision;
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
    private String host;
    private String xForwardedFor;
    private String xRealIp;

    @Id
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "hash")
    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
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
        return (Date) date.clone();
    }

    public void setDate(Date date) {
        this.date = (Date) date.clone();
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

    @Column(name = "generator_id")
    public int getGeneratorId() {
        return generatorId;
    }

    public void setGeneratorId(int generatorId) {
        this.generatorId = generatorId;
    }

    @Column(name = "signature_id")
    public int getSignatureId() {
        return signatureId;
    }

    public void setSignatureId(int signatureId) {
        this.signatureId = signatureId;
    }

    @Column(name = "signature_revision")
    public int getSignatureRevision() {
        return signatureRevision;
    }

    public void setSignatureRevision(int signatureRevision) {
        this.signatureRevision = signatureRevision;
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

    @Column(name = "host")
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Column(name = "x_forwarded_for")
    public String getxForwardedFor() {
        return xForwardedFor;
    }

    public void setxForwardedFor(String xForwardedFor) {
        this.xForwardedFor = xForwardedFor;
    }

    @Column(name = "x_real_ip")
    public String getxRealIp() {
        return xRealIp;
    }

    public void setxRealIp(String xRealIp) {
        this.xRealIp = xRealIp;
    }

    public SnortSignature getSignature() {
        return createSnortSignature(this);
    }

    public IpHeader getIpHeader() {
        return createIpHeader(this);
    }

    public ProtocolHeader getProtocolHeader() {
        return createProtocolHeader(this);
    }

    public HttpHeader getHttpHeader() {
        return createHttpHeader(this);
    }
}
