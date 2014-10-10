package com.payneteasy.srvlog.util;

/**
 * @author rpuch
 */
public class ComparisonUtils {
    public static boolean hostAddressesAreEqual(String host1, String host2) {
        // is it the same reference?
        if (host1 == host2) {
            return true;
        }

        // if one of strings is null, hosts are not equal, because
        // references are not the same, hence other host is non-null
        if (host1 == null || host2 == null) {
            return false;
        }

        // are hosts equals in the sense of strings?
        if (host1.equals(host2)) {
            return true;
        }

        // checking for different localhost representations
        boolean localhost1 = isLocalhost(host1);
        boolean localhost2 = isLocalhost(host2);
        if (localhost1 && localhost2) {
            return true;
        }

        // host names are different and do not both represent a localhost:
        // this means that they are totally different
        return false;
    }

    private static boolean isLocalhost(String host) {
        return "127.0.0.1".equalsIgnoreCase(host)
                || "localhost".equalsIgnoreCase(host);
    }
}
