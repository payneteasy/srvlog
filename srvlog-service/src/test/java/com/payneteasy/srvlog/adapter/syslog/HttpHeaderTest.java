package com.payneteasy.srvlog.adapter.syslog;

import static com.payneteasy.srvlog.adapter.syslog.HttpHeader.createHttpHeader;
import static com.payneteasy.srvlog.adapter.syslog.HttpHeader.isContainsHttpHeader;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * @author imenem
 */
public class HttpHeaderTest {

    private final String FULL_QUERY =
        "Host: www.test.org\n" +
        "X-Geo: US\n" +
        "X-Real-IP: 54.83.132.159\n" +
        "X-Timer: S1398278909.637000322,VS0,VS3\n" +
        "X-Forwarded-For: 54.83.132.159, 199.27.72.25, 50.19.19.94\n";

    private final String IGNORE_CASE_QUERY =
        "host: www.test.org\n" +
        "x-geo: US\n" +
        "x-real-ip: 54.83.132.159\n" +
        "x-timer: S1398278909.637000322,VS0,VS3\n" +
        "x-forwarded-for: 54.83.132.159, 199.27.72.25, 50.19.19.94\n";

    private final String WINDOWS_QUERY =
        "Host: www.test.org\r\n" +
        "X-Geo: US\r\n" +
        "X-Real-IP: 54.83.132.159\r\n" +
        "X-Timer: S1398278909.637000322,VS0,VS3\r\n" +
        "X-Forwarded-For: 54.83.132.159, 199.27.72.25, 50.19.19.94\r\n";

    private final String HOST_QUERY =
        "Host: www.test.org\n" +
        "X-Geo: US\n" +
        "X-Timer: S1398278909.637000322,VS0,VS3\n";

    private final String FORWARDED_QUERY =
        "X-Geo: US\n" +
        "X-Timer: S1398278909.637000322,VS0,VS3\n" +
        "X-Forwarded-For: 54.83.132.159, 199.27.72.25, 50.19.19.94\n";

    @Test
    public void testIsContainsHttpHeader() {
        assertTrue(isContainsHttpHeader(FULL_QUERY));
        assertTrue(isContainsHttpHeader(WINDOWS_QUERY));
        assertTrue(isContainsHttpHeader(IGNORE_CASE_QUERY));
        assertTrue(isContainsHttpHeader(HOST_QUERY));
        assertTrue(isContainsHttpHeader(FORWARDED_QUERY));
    }

    @Test
    public void testCreateHttpHeader() {
        assertEquals(
            "Host: www.test.org; X-Forwarded-For: 54.83.132.159, 199.27.72.25, 50.19.19.94; X-Real-IP: 54.83.132.159; ",
            createHttpHeader(FULL_QUERY).toString()
        );

        assertEquals(
            "Host: www.test.org; X-Forwarded-For: 54.83.132.159, 199.27.72.25, 50.19.19.94; X-Real-IP: 54.83.132.159; ",
            createHttpHeader(WINDOWS_QUERY).toString()
        );

        assertEquals(
            "Host: www.test.org; X-Forwarded-For: 54.83.132.159, 199.27.72.25, 50.19.19.94; X-Real-IP: 54.83.132.159; ",
            createHttpHeader(IGNORE_CASE_QUERY).toString()
        );

        assertEquals(
            "Host: www.test.org; ",
            createHttpHeader(HOST_QUERY).toString()
        );

        assertEquals(
            "X-Forwarded-For: 54.83.132.159, 199.27.72.25, 50.19.19.94; ",
            createHttpHeader(FORWARDED_QUERY).toString()
        );
    }

}
