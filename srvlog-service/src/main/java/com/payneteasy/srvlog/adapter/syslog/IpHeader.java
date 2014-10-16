package com.payneteasy.srvlog.adapter.syslog;

import static com.payneteasy.srvlog.adapter.syslog.ProtocolRegistry.getAlias;
import com.payneteasy.srvlog.data.SnortLogData;
import static java.lang.Integer.parseInt;
import jregex.Matcher;
import jregex.Pattern;
import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * Object representation for ip header from barnyard2 syslog_full output plugin message.
 *
 * @author imenem
 */
public class IpHeader {

    /**
     * Regular expression for ip header parsing.
     */
    private final static Pattern IP_HEADER_REGEX = new Pattern(
        "(?<=\\|\\| )({PROTOCOL}\\d{1,3}) " +
        "({SOURCE_IP}\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}) " +
        "({DESTINATION_IP}\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}) " +
        "({PROTOCOL_VERSION}\\d{1}) " +
        "({HEADER_LENGTH}\\d+) " +
        "({SERVICE_TYPE}\\d+) " +
        "({DATAGRAM_LENGTH}\\d+) " +
        "({IDENTIFICATION}\\d+) " +
        "({FLAGS}\\d+) " +
        "({FRAGMENT_OFFSET}\\d+) " +
        "({TIME_TO_LIVE}\\d+) " +
        "({CHECKSUM}\\d+)(?= \\|\\|)"
    );

    /**
     * Returns true, if message from from barnyard2 syslog_full output plugin contains IP header.
     *
     * @param       rawSnortMessage     Message from barnyard2 syslog_full output plugin
     * .
     * @return      True, if message contains IP header.
     */
    public static boolean isContainsIpHeader(String rawSnortMessage) {
        return IP_HEADER_REGEX.matcher(rawSnortMessage).find();
    }

    /**
     * Returns true, if data transfer object contains IP header.
     *
     * @param       snortLogData        Data transfer object to check.
     *
     * @return      True, if data transfer object contains IP header.
     */
    public static boolean isContainsIpHeader(SnortLogData snortLogData) {
        return !isEmpty(snortLogData.getProtocolAlias());
    }

    /**
     * Parses message from from barnyard2 syslog_full output plugin.
     *
     * @param       rawSnortMessage     Message from from barnyard2 syslog_full output plugin.
     *
     * @return      Object representation of IP header.
     */
    public static IpHeader createIpHeader(String rawSnortMessage) {
        IpHeader ipHeader = new IpHeader();

        Matcher matcher = IP_HEADER_REGEX.matcher(rawSnortMessage);

        if (!matcher.find()) {
            throw new RuntimeException(
                "Snort message does not contains ip header. " +
                "Check message with method isMessageContainsIPHeader() first"
            );
        }

        ipHeader.protocolNumber = parseInt(matcher.group("PROTOCOL"));
        ipHeader.protocolAlias = getAlias(ipHeader.protocolNumber);
        ipHeader.sourceIp = matcher.group("SOURCE_IP");
        ipHeader.destinationIp = matcher.group("DESTINATION_IP");
        ipHeader.protocolVersion = parseInt(matcher.group("PROTOCOL_VERSION"));
        ipHeader.headerLength = parseInt(matcher.group("HEADER_LENGTH"));
        ipHeader.serviceType = parseInt(matcher.group("SERVICE_TYPE"));
        ipHeader.datagramLength = parseInt(matcher.group("DATAGRAM_LENGTH"));
        ipHeader.identification = parseInt(matcher.group("IDENTIFICATION"));
        ipHeader.flags = parseInt(matcher.group("FLAGS"));
        ipHeader.fragmentOffset = parseInt(matcher.group("FRAGMENT_OFFSET"));
        ipHeader.timeToLive = parseInt(matcher.group("TIME_TO_LIVE"));
        ipHeader.checksum = parseInt(matcher.group("CHECKSUM"));

        return ipHeader;
    }

    /**
     * Creates object representation of IP header from data transfer object.
     *
     * @param       snortLogData        Data transfer object
     *
     * @return      Object representation of IP header.
     */
    public static IpHeader createIpHeader(SnortLogData snortLogData) {
        IpHeader ipHeader = new IpHeader();

        if (!isContainsIpHeader(snortLogData)) {
            throw new RuntimeException(
                "Snort message does not contains ip header. " +
                "Check message with method isContainsIPHeader() first"
            );
        }

        ipHeader.protocolNumber = snortLogData.getProtocolNumber();
        ipHeader.protocolAlias = snortLogData.getProtocolAlias();
        ipHeader.sourceIp = snortLogData.getSourceIp();
        ipHeader.destinationIp = snortLogData.getDestinationIp();
        ipHeader.protocolVersion = snortLogData.getProtocolVersion();
        ipHeader.headerLength = snortLogData.getHeaderLength();
        ipHeader.serviceType = snortLogData.getServiceType();
        ipHeader.datagramLength = snortLogData.getDatagramLength();
        ipHeader.identification = snortLogData.getIdentification();
        ipHeader.flags = snortLogData.getFlags();
        ipHeader.fragmentOffset = snortLogData.getFragmentOffset();
        ipHeader.timeToLive = snortLogData.getTimeToLive();
        ipHeader.checksum = snortLogData.getChecksum();

        return ipHeader;
    }

    /**
     * Protocol number (see {@link ProtocolRegistry} for more information).
     */
    private int protocolNumber;

    /**
     * Protocol alias (see {@link ProtocolRegistry} for more information).
     */
    private String protocolAlias;

    /**
     * Protocol version.
     */
    private int protocolVersion;

    /**
     * Source IP.
     */
    private String sourceIp;

    /**
     * Destination IP.
     */
    private String destinationIp;

    /**
     * Header length.
     */
    private int headerLength;

    /**
     * Type of service.
     */
    private int serviceType;

    /**
     * Datagram length.
     */
    private int datagramLength;

    /**
     * Identification.
     */
    private int identification;

    /**
     * Flags
     */
    private int flags;

    /**
     * Fragment offset.
     */
    private int fragmentOffset;

    /**
     * Time to live.
     */
    private int timeToLive;

    /**
     * Checksum.
     */
    private int checksum;

    /**
     * Returns protocol number (see {@link ProtocolRegistry}).
     *
     * @return      Protocol number.
     */
    public int getProtocolNumber() {
        return protocolNumber;
    }

    @Override
    public String toString() {
        return
            field("proto", protocolAlias) +
            field("sip", sourceIp) +
            field("dip", destinationIp) +
            field("ver", protocolVersion) +
            field("hlen", headerLength) +
            field("tos", serviceType) +
            field("len", datagramLength) +
            field("id", identification) +
            field("flags", flags) +
            field("off", fragmentOffset) +
            field("ttl", timeToLive) +
            field("csum", checksum)
        ;
    }

    /**
     * Fills data transfer object by IP header data.
     *
     * @param       snortLogData        Transfer object to fill.
     */
    public void fillSnortLogData(SnortLogData snortLogData) {
        snortLogData.setProtocolNumber(protocolNumber);
        snortLogData.setProtocolAlias(protocolAlias);
        snortLogData.setProtocolVersion(protocolVersion);
        snortLogData.setSourceIp(sourceIp);
        snortLogData.setDestinationIp(destinationIp);
        snortLogData.setHeaderLength(headerLength);
        snortLogData.setServiceType(serviceType);
        snortLogData.setDatagramLength(datagramLength);
        snortLogData.setIdentification(identification);
        snortLogData.setFlags(flags);
        snortLogData.setFragmentOffset(fragmentOffset);
        snortLogData.setTimeToLive(timeToLive);
        snortLogData.setChecksum(checksum);
    }

    /**
     * Converts pair of key and value to string.
     *
     * @param       key         Key.
     * @param       value       Value for key.
     *
     * @return      Key and value pair representation.
     */
    private String field(String key, Object value) {
        return key + ": " + value + "; ";
    }

}
