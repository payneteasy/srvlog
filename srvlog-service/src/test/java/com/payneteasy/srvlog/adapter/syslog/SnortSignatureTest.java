package com.payneteasy.srvlog.adapter.syslog;

import static com.payneteasy.srvlog.adapter.syslog.SnortSignature.createSnortSignature;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * @author imenem
 */
public class SnortSignatureTest {

    private static final String CLASSIFICATION = "[1:1310:5] Snort Alert [1:1310:5]";

    @Test
    public void testCreateSnortSignature() {
        String signature = createSnortSignature(CLASSIFICATION).toString();
        assertEquals("[1:1310:5]", signature);
    }

}
