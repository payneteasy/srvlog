package com.payneteasy.srvlog.adapter.syslog;

import static com.payneteasy.srvlog.adapter.syslog.ProtocolRegistry.getAlias;
import static java.lang.Integer.parseInt;
import jregex.Matcher;
import jregex.Pattern;

/**
 * Object representation for ip header from barnyard2 syslog_full output plugin message.
 *
 * @author imenem
 */
public class IpHeader {

    /**
     * Regular expression for ip header parsing.
     */
    private final static Pattern ipHeaderRegex = new Pattern(
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
    public static boolean isSnortMessageContainsIpHeader(String rawSnortMessage) {
        return ipHeaderRegex.matcher(rawSnortMessage).find();
    }

    /**
     * Parses message from from barnyard2 syslog_full output plugin.
     *
     * @param       rawSnortMessage     Message from from barnyard2 syslog_full output plugin.
     *
     * @return      Object representation of IP header.
     */
    public static IpHeader parseIpHeader(String rawSnortMessage) {
        IpHeader ipHeader = new IpHeader();

        Matcher matcher = ipHeaderRegex.matcher(rawSnortMessage);

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
