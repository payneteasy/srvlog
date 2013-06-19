package com.payneteasy.srvlog.adapter.utils;

import com.payneteasy.srvlog.data.LogData;

import java.net.InetAddress;

/**
 * Date: 19.06.13
 * Time: 20:11
 */
public class AdapterHelper {

    public static void setHostName(InetAddress inetAddress, LogData logData) {
        String hostName = extractHostname(inetAddress);
        logData.setHost(hostName);
    }

    public static String extractHostname(InetAddress inetAddress) {
        String hostAddress = inetAddress.getHostAddress();

        String hostName = inetAddress.getHostName();
        if (!hostName.equalsIgnoreCase(hostAddress)) {
            int j = hostName.indexOf('.');

            if (j > -1) {
                hostName = hostName.substring(0,j);
            }
        }

        return hostName;
    }


}
