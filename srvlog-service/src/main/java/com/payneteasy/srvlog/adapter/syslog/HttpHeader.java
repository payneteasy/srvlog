package com.payneteasy.srvlog.adapter.syslog;

import com.payneteasy.srvlog.data.SnortLogData;
import jregex.Matcher;
import jregex.Pattern;
import static jregex.REFlags.IGNORE_CASE;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.trim;

/**
 * Object representation for HTTP headers from payload from barnyard2 syslog_full output plugin message.
 *
 * @author imenem
 */
public class HttpHeader {

    /**
     * Regular expression for Host header.
     */
    private static final Pattern HOST_REGEX = new Pattern("Host:({HOST}.+)", IGNORE_CASE);

    /**
     * Regular expression for X-Forwarded-For header.
     */
    private static final Pattern X_FORWARDED_FOR_REGEX = new Pattern("X-Forwarded-For:({X_FORWARDED_FOR}.+)", IGNORE_CASE);

    /**
     * Regular expression for X-Real-IP header.
     */
    private static final Pattern X_REAL_IP_REGEX = new Pattern("X-Real-IP:({X_REAL_IP}.+)", IGNORE_CASE);

    /**
     * Returns true, if payload contains HTTP headers.
     *
     * @param       payload     Payload from barnyard2 syslog_full output plugin message.
     * .
     * @return      True, if payload contains HTTP headers.
     */
    public static boolean isContainsHttpHeader(String payload) {
        return HOST_REGEX.matcher(payload).find()
            || X_FORWARDED_FOR_REGEX.matcher(payload).find()
            || X_REAL_IP_REGEX.matcher(payload).find();
    }

    /**
     * Returns true, if data transfer object contains HTTP headers.
     *
     * @param       data        Data transfer object to check.
     *
     * @return      True, if data transfer object contains HTTP headers.
     */
    public static boolean isContainsHttpHeader(SnortLogData data) {
        return !isEmpty(data.getHost())
            || !isEmpty(data.getxForwardedFor())
            || !isEmpty(data.getxRealIp());
    }

    /**
     * Parses payload from from barnyard2 syslog_full output plugin message.
     *
     * @param       payload     Payload from from barnyard2 syslog_full output plugin message.
     *
     * @return      Object representation of HTTP headers.
     */
    public static HttpHeader createHttpHeader(String payload) {
        HttpHeader httpHeader = new HttpHeader();

        Matcher hostMatcher = HOST_REGEX.matcher(payload);
        Matcher xForwardedForMatcher = X_FORWARDED_FOR_REGEX.matcher(payload);
        Matcher xRealIpMatcher = X_REAL_IP_REGEX.matcher(payload);

        boolean hasHost = hostMatcher.find();
        boolean hasXForwardedFor = xForwardedForMatcher.find();
        boolean hasXRealIp = xRealIpMatcher.find();

        if (!hasHost && !hasXForwardedFor && !hasXRealIp) {
            throw new RuntimeException(
                "Message does not look like http query. " +
                "Check message with method isContainsHttpHeader() first"
            );
        }

        httpHeader.host = trim(hostMatcher.group("HOST"));
        httpHeader.xForwardedFor = trim(xForwardedForMatcher.group("X_FORWARDED_FOR"));
        httpHeader.xRealIp = trim(xRealIpMatcher.group("X_REAL_IP"));

        return httpHeader;
    }

    /**
     * Creates object representation of HTTP headers from data transfer object.
     *
     * @param       data        Data transfer object
     *
     * @return      Object representation of HTTP headers.
     */
    public static HttpHeader createHttpHeader(SnortLogData data) {
        HttpHeader httpHeader = new HttpHeader();

        httpHeader.host = data.getHost();
        httpHeader.xForwardedFor = data.getxForwardedFor();
        httpHeader.xRealIp = data.getxRealIp();

        return httpHeader;
    }

    /**
     * HTTP host.
     */
    private String host;

    /**
     * HTTP header x-fowarded-for.
     */
    private String xForwardedFor;

    /**
     * HTTP header x-real-ip.
     */
    private String xRealIp;

    /**
     * Fills data transfer object by HTTP headers data.
     *
     * @param       data        Transfer object to fill.
     */
    public void fillSnortLogData(SnortLogData data) {
        data.setHost(host);
        data.setxForwardedFor(xForwardedFor);
        data.setxRealIp(xRealIp);
    }

    @Override
    public String toString() {
        String string = "";

        if (!isEmpty(host)) {
            string += "Host: " + host + "; ";
        }

        if (!isEmpty(xForwardedFor)) {
            string += "X-Forwarded-For: " + xForwardedFor + "; ";
        }

        if (!isEmpty(xRealIp)) {
            string += "X-Real-IP: " + xRealIp + "; ";
        }

        return string;
    }

}
