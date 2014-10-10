package com.payneteasy.srvlog.adapter.syslog;

import static java.lang.Integer.parseInt;
import jregex.Matcher;
import jregex.Pattern;

/**
 * Object representation for protocol header from barnyard2 syslog_full output plugin message.
 *
 * @author imenem
 */
public class ProtocolHeader {

    /**
     * Regular expression for TCP header.
     */
    private final static Pattern TCP_HEADER_REGEX = new Pattern(
        "({SOURCE_PORT}\\d+) " +
        "({DESTINATION_PORT}\\d+) " +
        "({SEQUENCE_NUMBER}\\d+) " +
        "({ACKNOWLEDGEMENT_NUMBER}\\d+) " +
        "({OFFSET}\\d+) " +
        "({RESERVED}\\d+) " +
        "({FLAGS}\\d+) " +
        "({WINDOW_SIZE}\\d+) " +
        "({CHECKSUM}\\d+) " +
        "({URGENT_POINTER}\\d+)"
    );

    /**
     * Regular expression for UDP header.
     */
    private final static Pattern UDP_HEADER_REGEX = new Pattern(
        "({SOURCE_PORT}\\d+) " +
        "({DESTINATION_PORT}\\d+) " +
        "({DATAGRAM_LENGTH}\\d+) " +
        "({CHECKSUM}\\d+)"
    );

    /**
     * Parses raw TCP protocol header from barnyard2 syslog_full output plugin message.
     *
     * @param       rawProtocolHeader       Raw protocol header.
     *
     * @return      Object representation of protocol header.
     */
    public static ProtocolHeader parseTcpHeader(String rawProtocolHeader) {
        Matcher matcher = TCP_HEADER_REGEX.matcher(rawProtocolHeader);

        if (!matcher.find()) {
            throw new RuntimeException("Given protocol header is not UDP header.");
        }

        ProtocolHeader protocolHeader = new ProtocolHeader();

        protocolHeader.sourcePort = parseInt(matcher.group("SOURCE_PORT"));
        protocolHeader.destinationPort = parseInt(matcher.group("DESTINATION_PORT"));

        return protocolHeader;
    }

    /**
     * Parses raw UDP protocol header from barnyard2 syslog_full output plugin message.
     *
     * @param       rawProtocolHeader       Raw protocol header.
     *
     * @return      Object representation of protocol header.
     */
    public static ProtocolHeader parseUdpHeader(String rawProtocolHeader) {
        Matcher matcher = UDP_HEADER_REGEX.matcher(rawProtocolHeader);

        if (!matcher.find()) {
            throw new RuntimeException("Given protocol header is not UDP header.");
        }

        ProtocolHeader protocolHeader = new ProtocolHeader();

        protocolHeader.sourcePort = parseInt(matcher.group("SOURCE_PORT"));
        protocolHeader.destinationPort = parseInt(matcher.group("DESTINATION_PORT"));

        return protocolHeader;
    }

    /**
     * Source port.
     */
    private int sourcePort;

    /**
     * Destination protocol.
     */
    private int destinationPort;

    @Override
    public String toString() {
        return "sp: " + sourcePort + "; dp: " + destinationPort + ";";
    }

}
