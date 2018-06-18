package com.payneteasy.srvlog.adapter.syslog.surricata;

import com.payneteasy.srvlog.data.SnortLogData;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SuricataJsonMessageTest {

    @Test
    public void udp() throws IOException {
        SuricataMessageManager manager = new SuricataMessageManager(null);
        SnortLogData snort = manager.createSnortLogData(read("surricata-01-broadcast.json"));
        assertNotNull( snort               );
        assertNotNull( snort.getDate()     );
        assertNotNull( snort.getPayload () );
        assertEquals ( "suricata"                       , snort.getProgram          () );
        assertEquals ( "eth2"                            , snort.getSensorName       () );
        assertEquals ( 2                                 , snort.getPriority         () );
        assertEquals ( "ET SCAN NMAP OS Detection Probe" , snort.getClassification   () );
        assertEquals ( "Attempted Information Leak"      , snort.getAlertCause       () );
        assertEquals ( 1                                 , snort.getGeneratorId      () );
        assertEquals ( 2018489                           , snort.getSignatureId      () );
        assertEquals ( 3                                 , snort.getSignatureRevision() );
        assertEquals ( "UDP"                             , snort.getProtocolAlias    () );
        assertEquals ( "10.0.2.5"                        , snort.getSourceIp         () );
        assertEquals ( "10.0.2.8"                        , snort.getDestinationIp    () );
        assertEquals ( 48008                             , snort.getSourcePort       () );
        assertEquals ( 36391                             , snort.getDestinationPort  () );
        assertEquals ( null                              , snort.getHost             () );

        assertTrue(snort.getPayload().contains("00000000| 00 b5 6d 04 7a 30 6c 3b 6b c2 8e 38 08 00 45 00 |..m.z0l;k..8..E."));
    }

    @Test
    public void http() throws IOException {
        SuricataMessageManager manager = new SuricataMessageManager(null);
        SnortLogData snort = manager.createSnortLogData(read("surricata-02-http.json"));
        assertNotNull( snort               );
        assertNotNull( snort.getDate()     );
        assertNotNull( snort.getPayload () );
        assertEquals ( "suricata"                       , snort.getProgram          () );
        assertEquals ( "eth2"                            , snort.getSensorName       () );
        assertEquals ( 1                                 , snort.getPriority         () );
        assertEquals ( "ET USER_AGENTS Suspicious User Agent (BlackSun)" , snort.getClassification());
        assertEquals ( "A Network Trojan was detected"   , snort.getAlertCause       () );
        assertEquals ( 1                                 , snort.getGeneratorId      () );
        assertEquals ( 2008983                           , snort.getSignatureId      () );
        assertEquals ( 6                                 , snort.getSignatureRevision() );
        assertEquals ( "TCP"                             , snort.getProtocolAlias    () );
        assertEquals ( "10.0.2.8"                        , snort.getSourceIp         () );
        assertEquals ( "173.194.73.103"                  , snort.getDestinationIp    () );
        assertEquals ( 38210                             , snort.getSourcePort       () );
        assertEquals ( 80                                , snort.getDestinationPort  () );
        assertEquals ( "www.google.com"                  , snort.getHost             () );

        assertTrue(snort.getPayload().contains("<TITLE>302 Moved</TITLE></HEAD><BODY>"));
        assertTrue(snort.getPayload().contains("User-Agent: BlackSun"));
        assertTrue(snort.getPayload().contains("00000000| d4 ca 6d 04 87 61 00 b5 6d 04 7a 30 08 00 45 00 |..m..a..m.z0..E.|"));

    }

    private String read(String aFilename) throws IOException {
        return FileUtils.readFileToString(new File("src/test/resources/surricata", aFilename));
    }
}
