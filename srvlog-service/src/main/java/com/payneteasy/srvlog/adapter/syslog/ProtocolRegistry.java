package com.payneteasy.srvlog.adapter.syslog;

import static java.lang.String.valueOf;
import java.util.HashMap;
import java.util.Map;

/**
 * Registry for different transport protocols metadata.
 *
 * This registry provides ability to find protocol official name
 * or protocol alias by protocol number.
 * Registry data took from /etc/protocols.
 *
 *      official name         number            alias                        description
 *
 *          ip                  0           IP                  internet protocol, pseudo protocol number
 *          icmp                1           ICMP                internet control message protocol
 *          igmp                2           IGMP                Internet Group Management
 *          ggp                 3           GGP                 gateway-gateway protocol
 *          ipencap             4           IP-ENCAP            IP encapsulated in IP (officially "IP")
 *          st                  5           ST                  ST datagram mode
 *          tcp                 6           TCP                 transmission control protocol
 *          egp                 8           EGP                 exterior gateway protocol
 *          igp                 9           IGP                 any private interior gateway (Cisco)
 *          pup                 12          PUP                 PARC universal packet protocol
 *          udp                 17          UDP                 user datagram protocol
 *          hmp                 20          HMP                 host monitoring protocol
 *          xns-idp             22          XNS-IDP             Xerox NS IDP
 *          rdp                 27          RDP                 "reliable datagram" protocol
 *          iso-tp4             29          ISO-TP4             ISO Transport Protocol class 4 [RFC905]
 *          dccp                33          DCCP                Datagram Congestion Control Prot. [RFC4340]
 *          xtp                 36          XTP                 Xpress Transfer Protocol
 *          ddp                 37          DDP                 Datagram Delivery Protocol
 *          idpr-cmtp           38          IDPR-CMTP           IDPR Control Message Transport
 *          ipv6                41          IPv6                Internet Protocol, version 6
 *          ipv6-route          43          IPv6-Route          Routing Header for IPv6
 *          ipv6-frag           44          IPv6-Frag           Fragment Header for IPv6
 *          idrp                45          IDRP                Inter-Domain Routing Protocol
 *          rsvp                46          RSVP                Reservation Protocol
 *          gre                 47          GRE                 General Routing Encapsulation
 *          esp                 50          IPSEC-ESP           Encap Security Payload [RFC2406]
 *          ah                  51          IPSEC-AH            Authentication Header [RFC2402]
 *          skip                57          SKIP                SKIP
 *          ipv6-icmp           58          IPv6-ICMP           ICMP for IPv6
 *          ipv6-nonxt          59          IPv6-NoNxt          No Next Header for IPv6
 *          ipv6-opts           60          IPv6-Opts           Destination Options for IPv6
 *          rspf                73          RSPF CPHB           Radio Shortest Path First (officially CPHB)
 *          vmtp                81          VMTP                Versatile Message Transport
 *          eigrp               88          EIGRP               Enhanced Interior Routing Protocol (Cisco)
 *          ospf                89          OSPFIGP             Open Shortest Path First IGP
 *          mtp                 92          MTP                 Multicast Transport Protocol
 *          ax.25               93          AX.25               AX.25 frames
 *          ipip                94          IPIP                IP-within-IP Encapsulation Protocol
 *          etherip             97          ETHERIP             Ethernet-within-IP Encapsulation [RFC3378]
 *          encap               98          ENCAP               Yet Another IP encapsulation [RFC1241]
 *          #                   99                              any private encryption scheme
 *          pim                 103         PIM                 Protocol Independent Multicast
 *          ipcomp              108         IPCOMP              IP Payload Compression Protocol
 *          vrrp                112         VRRP                Virtual Router Redundancy Protocol [RFC5798]
 *          l2tp                115         L2TP                Layer Two Tunneling Protocol [RFC2661]
 *          isis                124         ISIS                IS-IS over IPv4
 *          sctp                132         SCTP                Stream Control Transmission Protocol
 *          fc                  133         FC                  Fibre Channel
 *          mobility-header     135         Mobility-Header     Mobility Support for IPv6 [RFC3775]
 *          udplite             136         UDPLite             UDP-Lite [RFC3828]
 *          mpls-in-ip          137         MPLS-in-IP          MPLS-in-IP [RFC4023]
 *          manet               138                             MANET Protocols [RFC5498]
 *          hip                 139         HIP                 Host Identity Protocol
 *          shim6               140         Shim6               Shim6 Protocol [RFC5533]
 *          wesp                141         WESP                Wrapped Encapsulating Security Payload
 *          rohc                142         ROHC                Robust Header Compression
 *
 * @author imenem
 */
public class ProtocolRegistry {

    /**
     * ICMP protocol number.
     */
    public static final int ICMP = 1;

    /**
     * TCP protocol number.
     */
    public static final int TCP = 6;

    /**
     * UDP protocol number.
     */
    public static final int UDP = 17;

    /**
     * Map with protocol official names.
     */
    private static final Map<Integer, String> PROTOCOL_OFFICIAL_NAME_REGISTRY = new HashMap<>();

    /**
     * Map with protocol aliases.
     */
    private static final Map<Integer, String> PROTOCOL_ALIAS_REGISTRY = new HashMap<>();

    /**
     * Fills registry maps with protocols metadata.
     */
    static {
        add(0,   "ip", "IP");
        add(ICMP,"icmp", "ICMP");
        add(2,   "igmp", "IGMP");
        add(3,   "ggp", "GGP");
        add(4,   "ipencap", "IP-ENCAP");
        add(5,   "st", "ST");
        add(TCP, "tcp", "TCP");
        add(8,   "egp", "EGP");
        add(9,   "igp", "IGP");
        add(12,  "pup", "PUP");
        add(UDP, "udp", "UDP");
        add(20,  "hmp", "HMP");
        add(22,  "xns-idp", "XNS-IDP");
        add(27,  "rdp", "RDP");
        add(29,  "iso-tp4", "ISO-TP4");
        add(33,  "dcpp", "DCPP");
        add(36,  "xtp", "XTP");
        add(37,  "ddp", "DDP");
        add(38,  "idpr-cmtp", "IDPR-CMTP");
        add(41,  "ipv6", "IPv6");
        add(43,  "ipv6-route", "IPv6-Route");
        add(43,  "ipv6-frag", "IPv6-Frag");
        add(45,  "idrp", "IDRP");
        add(46,  "rsvp", "RSVP");
        add(47,  "gre", "GRE");
        add(50,  "esp", "IPSEC-ESP");
        add(51,  "ah", "IPSEC-AH");
        add(57,  "skip", "SKIP");
        add(58,  "ipv6-icmp", "IPv6-ICMP");
        add(59,  "ipv6-nonxt", "IPv6-NoNxt");
        add(60,  "ipv6-opts", "IPv6-Opts");
        add(73,  "rspf", "RSPF CPHB");
        add(81,  "vmtp", "VMTP");
        add(88,  "eigrp", "EIGRP");
        add(89,  "ospf", "OSPFIGP");
        add(93,  "ex.25", "AX.25");
        add(94,  "ipip", "IPIP");
        add(97,  "etherip", "ETHERIP");
        add(98,  "encap", "ENCAP");
        add(99,  "#", "private encryption scheme");
        add(103, "pim", "PIM");
        add(108, "ipcomp", "IPCOMP");
        add(112, "vrrp", "VRRP");
        add(115, "isis", "ISIS");
        add(132, "sctp", "SCTP");
        add(133, "fc", "FC");
        add(135, "mobility-header", "Mobility-Header");
        add(136, "udplite", "UPD-Lite");
        add(137, "mpls-in-ip", "MPLS-in-IP");
        add(138, "manet", "manet");
        add(139, "hip", "HIP");
        add(140, "shim6", "Shim6");
        add(141, "wesp", "WESP");
        add(142, "rohc", "ROHC");
    }

    /**
     * Returns official protocol name by protocol number.
     * If registry does not contain protocol with given number,
     * protocol number will be returned.
     *
     * @param       protocolNumber      Protocol number.
     *
     * @return      Protocol official name.
     */
    public static String getOfficialName(int protocolNumber) {
        return PROTOCOL_OFFICIAL_NAME_REGISTRY.getOrDefault(protocolNumber, valueOf(protocolNumber));
    }

    /**
     * Returns protocol alias by protocol number.
     * If registry does not contain protocol with given number,
     * protocol number will be returned.
     *
     * @param       protocolNumber      Protocol number.
     *
     * @return      Protocol alias.
     */
    public static String getAlias(int protocolNumber) {
        return PROTOCOL_ALIAS_REGISTRY.getOrDefault(protocolNumber, valueOf(protocolNumber));
    }

    /**
     * Adds protocol metadata to registry.
     *
     * @param       protocolNumber      Protocol number.
     * @param       officialName        Protocol official name.
     * @param       protocolAlias       Protocol alias.
     */
    private static void add(int protocolNumber, String officialName, String protocolAlias) {
        PROTOCOL_OFFICIAL_NAME_REGISTRY.put(protocolNumber, officialName);
        PROTOCOL_ALIAS_REGISTRY.put(protocolNumber, protocolAlias);
    }
}

