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
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Id;
import static org.springframework.util.StringUtils.isEmpty;

/**
 * Data transfer object.
 * This class uses to save multilevel object structure to plain database table.
 * Jdbc-proc doesn't save multilevel object structure.
 *
 * @author imenem
 */
public class SnortLogData implements Serializable {

    private Long id;
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

    @Override
    public int hashCode() {
        int hashCode = 3;
        hashCode = 17 * hashCode + Objects.hashCode(this.program);
        hashCode = 17 * hashCode + Objects.hashCode(this.sensorName);
        hashCode = 17 * hashCode + Objects.hashCode(this.date);
        hashCode = 17 * hashCode + this.priority;
        hashCode = 17 * hashCode + Objects.hashCode(this.classification);
        hashCode = 17 * hashCode + Objects.hashCode(this.alertCause);
        hashCode = 17 * hashCode + this.generatorId;
        hashCode = 17 * hashCode + this.signatureId;
        hashCode = 17 * hashCode + this.signatureRevision;
        hashCode = 17 * hashCode + this.protocolNumber;
        hashCode = 17 * hashCode + Objects.hashCode(this.protocolAlias);
        hashCode = 17 * hashCode + this.protocolVersion;
        hashCode = 17 * hashCode + Objects.hashCode(this.sourceIp);
        hashCode = 17 * hashCode + Objects.hashCode(this.destinationIp);
        hashCode = 17 * hashCode + this.headerLength;
        hashCode = 17 * hashCode + this.serviceType;
        hashCode = 17 * hashCode + this.datagramLength;
        hashCode = 17 * hashCode + this.identification;
        hashCode = 17 * hashCode + this.flags;
        hashCode = 17 * hashCode + this.fragmentOffset;
        hashCode = 17 * hashCode + this.timeToLive;
        hashCode = 17 * hashCode + this.checksum;
        hashCode = 17 * hashCode + this.sourcePort;
        hashCode = 17 * hashCode + this.destinationPort;
        hashCode = 17 * hashCode + Objects.hashCode(this.host);
        hashCode = 17 * hashCode + Objects.hashCode(this.xForwardedFor);
        hashCode = 17 * hashCode + Objects.hashCode(this.xRealIp);
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SnortLogData other = (SnortLogData) obj;
        if (!Objects.equals(this.program, other.program)) {
            return false;
        }
        if (!Objects.equals(this.sensorName, other.sensorName)) {
            return false;
        }
        if (!Objects.equals(this.date, other.date)) {
            return false;
        }
        if (this.priority != other.priority) {
            return false;
        }
        if (!Objects.equals(this.classification, other.classification)) {
            return false;
        }
        if (!Objects.equals(this.alertCause, other.alertCause)) {
            return false;
        }
        if (this.generatorId != other.generatorId) {
            return false;
        }
        if (this.signatureId != other.signatureId) {
            return false;
        }
        if (this.signatureRevision != other.signatureRevision) {
            return false;
        }
        if (this.protocolNumber != other.protocolNumber) {
            return false;
        }
        if (!Objects.equals(this.protocolAlias, other.protocolAlias)) {
            return false;
        }
        if (this.protocolVersion != other.protocolVersion) {
            return false;
        }
        if (!Objects.equals(this.sourceIp, other.sourceIp)) {
            return false;
        }
        if (!Objects.equals(this.destinationIp, other.destinationIp)) {
            return false;
        }
        if (this.headerLength != other.headerLength) {
            return false;
        }
        if (this.serviceType != other.serviceType) {
            return false;
        }
        if (this.datagramLength != other.datagramLength) {
            return false;
        }
        if (this.identification != other.identification) {
            return false;
        }
        if (this.flags != other.flags) {
            return false;
        }
        if (this.fragmentOffset != other.fragmentOffset) {
            return false;
        }
        if (this.timeToLive != other.timeToLive) {
            return false;
        }
        if (this.checksum != other.checksum) {
            return false;
        }
        if (this.sourcePort != other.sourcePort) {
            return false;
        }
        if (this.destinationPort != other.destinationPort) {
            return false;
        }
        if (!Objects.equals(this.host, other.host)) {
            return false;
        }
        if (!Objects.equals(this.xForwardedFor, other.xForwardedFor)) {
            return false;
        }
        if (!Objects.equals(this.xRealIp, other.xRealIp)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String string = "SnortLogData{";

        if (!isEmpty(program)) {
            string += "program=" + program + "; ";
        }
        if (!isEmpty(sensorName)) {
            string += "sensorName=" + sensorName + "; ";
        }
        if (date != null) {
            string += "date=" + date + "; ";
        }
        if (priority != 0) {
            string += "priority=" + priority + "; ";
        }
        if (!isEmpty(classification)) {
            string += "classification=" + classification + "; ";
        }
        if (!isEmpty(alertCause)) {
            string += "alertCause=" + alertCause + "; ";
        }
        if (generatorId != 0) {
            string += "generatorId=" + generatorId + "; ";
        }
        if (signatureId != 0) {
            string += "signatureId=" + signatureId + "; ";
        }
        if (signatureRevision != 0) {
            string += "signatureRevision=" + signatureRevision + "; ";
        }
        if (protocolNumber != 0) {
            string += "protocolNumber=" + protocolNumber + "; ";
        }
        if (!isEmpty(protocolAlias)) {
            string += "protocolAlias=" + protocolAlias + "; ";
        }
        if (protocolVersion != 0) {
            string += "protocolVersion=" + protocolVersion + "; ";
        }
        if (!isEmpty(sourceIp)) {
            string += "sourceIp=" + sourceIp + "; ";
        }
        if (!isEmpty(destinationIp)) {
            string += "destinationIp=" + destinationIp + "; ";
        }
        if (headerLength != 0) {
            string += "headerLength=" + headerLength + "; ";
        }
        if (serviceType != 0) {
            string += "serviceType=" + serviceType + "; ";
        }
        if (datagramLength != 0) {
            string += "datagramLength=" + datagramLength + "; ";
        }
        if (identification != 0) {
            string += "identification=" + identification + "; ";
        }
        if (flags != 0) {
            string += "flags=" + flags + "; ";
        }
        if (fragmentOffset != 0) {
            string += "fragmentOffset=" + fragmentOffset + "; ";
        }
        if (timeToLive != 0) {
            string += "timeToLive=" + timeToLive + "; ";
        }
        if (checksum != 0) {
            string += "checksum=" + checksum + "; ";
        }
        if (sourcePort != 0) {
            string += "sourcePort=" + sourcePort + "; ";
        }
        if (destinationPort != 0) {
            string += "destinationPort=" + destinationPort + "; ";
        }
        if (!isEmpty(host)) {
            string += "host=" + host + "; ";
        }
        if (!isEmpty(xForwardedFor)) {
            string += "xForwardedFor=" + xForwardedFor + "; ";
        }
        if (!isEmpty(xRealIp)) {
            string += "xRealIp=" + xRealIp + "; ";
        }

        return string + "}";
    }

}
