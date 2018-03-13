package com.payneteasy.srvlog.adapter.syslog.surricata;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.base.Utf8;
import com.payneteasy.srvlog.data.SnortLogData;
import com.payneteasy.srvlog.service.ILogCollector;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Base64;

@Service
public class SuricataMessageManager {

    private static final Logger LOG = LoggerFactory.getLogger(SuricataMessageManager.class);

    private final ILogCollector collector;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();

    @Autowired
    public SuricataMessageManager(ILogCollector aCollector) {
        collector = aCollector;
    }

    public boolean isMessageFromSurricata(String aRawMessage) {
        return aRawMessage.contains("suricata") && aRawMessage.contains("{");
    }

    public void processRawMessage(String aRawMessage) {
        try {
            collector.saveSnortLog(createSnortLogData(aRawMessage));
        } catch (IOException e) {
            LOG.error("Cannot process {}", aRawMessage, e);
        }
    }

    protected SnortLogData createSnortLogData(String aRawMessage) throws IOException {
        String               jsonText         = extractJson(aRawMessage);
        JsonNode             jsonRoot         = objectMapper.readTree(jsonText);
        SuricataJsonMessage surricataMessage = objectMapper.convertValue(jsonRoot, SuricataJsonMessage.class);

        replaceBase64ToUtf8(jsonRoot);

        String               payloadJson      = objectWriter.writeValueAsString(jsonRoot)
                                                     .replace("\\n", "\n")
                                                     .replace("\\r", "");

        return convertToSnort(payloadJson, surricataMessage);
    }

    private void replaceBase64ToUtf8(JsonNode aRoot) {
        replaceNode(aRoot, "payload");
        replaceNode(aRoot, "packet");
        JsonNode httpNode = aRoot.get("http");
        if(httpNode != null) {
            replaceNode(httpNode, "http_response_body");
        }
    }

    private void replaceNode(JsonNode aRoot, String aName) {
        JsonNode payloadNode = aRoot.get(aName);
        if(payloadNode == null) {
            LOG.warn("No node '{}' in {}", aName, aRoot);
            return;
        }

        String base64 = payloadNode.asText();
        if(Strings.isNullOrEmpty(base64)) {
            LOG.warn("Node {} has empty value ({})", aName, payloadNode);
            return;
        }

        byte[] bytes = Base64.getDecoder().decode(base64);

        String text;
        if(Utf8.isWellFormed(bytes)) {
            text = new String(bytes, Charsets.UTF_8);
        } else {
            text = ByteBufUtil.prettyHexDump(Unpooled.wrappedBuffer(bytes));
        }

        ObjectNode node = (ObjectNode) aRoot;
        node.set(aName, new TextNode(text));
    }

    private SnortLogData convertToSnort(String aJson, SuricataJsonMessage aEvent) {
        SnortLogData   snort = new SnortLogData();
        SuricataAlert alert = aEvent.getAlert();
        SuricataHttp http  = aEvent.getHttp();

        snort.setProgram           ( "surricata"                  );
        snort.setSensorName        ( aEvent.getIn_iface()         );
        snort.setDate              ( aEvent.getTimestamp()        );
        snort.setPriority          ( alert.getSeverity()          ); // [Priority: 2 ]
        snort.setClassification    ( alert.getSignature()         ); // [Classification: Attempted Information Leak ]
        snort.setAlertCause        ( alert.getCategory()          );
        snort.setPayload           ( aJson                        );

        snort.setGeneratorId       ( alert.getGid()               );
        snort.setSignatureId       ( alert.getSignatureId()       );
        snort.setSignatureRevision ( alert.setSignatureRevision() );

        snort.setProtocolAlias     ( aEvent.getProto()            );
        snort.setSourceIp          ( aEvent.getSrc_ip()           );
        snort.setDestinationIp     ( aEvent.getDest_ip()          );

        snort.setSourcePort        ( aEvent.getSrc_port()         );
        snort.setDestinationPort   ( aEvent.getDest_port()        );

        snort.setHost              ( http.getHostname()           );

        // todo parse headers
//        snort.setxForwardedFor(xForwardedFor);
//        snort.setxRealIp(xRealIp);

//        snort.setProtocolNumber(protocolNumber); // ex. 6
//        snort.setProtocolVersion(protocolVersion); // ex. 4
//        snort.setHeaderLength(headerLength);
//        snort.setServiceType(serviceType);
//        snort.setDatagramLength(datagramLength);
//        snort.setIdentification(identification);
//        snort.setFlags(flags);
//        snort.setFragmentOffset(fragmentOffset);
//        snort.setTimeToLive(timeToLive);
//        snort.setChecksum(checksum);

        return snort;
    }

    private String extractJson(String aRawMessage) {
        int pos = aRawMessage.indexOf('{');
        if(pos < 0) {
            throw new IllegalStateException("Cannot find char '{' in the message " + aRawMessage);
        }

        return aRawMessage.substring(pos);

    }
}
