package com.payneteasy.srvlog.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Date: 12.01.13
 * Time: 20:28
 */
public enum LogFacility implements LogEnum{

    kern(0, "kern"),
    user(1, "user"),
    mail(2, "mail"),
    daemon(3, "daemon"),
    auth(4, "auth"),
    syslog(5, "syslog"),
    lpr(6, "lpr"),
    news(7, "news"),
    uucp(8, "uucp"),
    cron(9, "cron"),
    authpriv(10, "authpriv"),
    ftp(11, "ftp"),
    ntp(12, "ntp"),
    audit(13, "audit"),
    alert(14, "alert"),
    clock(15, "clock"),
    local0(16, "local0"),
    local1(17, "local1"),
    local2(18, "local2"),
    local3(19, "local3"),
    local4(20, "local4"),
    local5(21, "local5"),
    local6(22, "local6"),
    local7(23, "local7");

    private static Map<Integer, String> valueToName = new HashMap<Integer, String>();
    private static List<LogFacility> logFacilities = new ArrayList<LogFacility>();

    static {
        for (LogFacility lf: LogFacility.values()) {
            valueToName.put(lf.getValue(), lf.name());
            logFacilities.add(lf);
        }
    }

    private final Integer value;

    private String facilityDisplayName;

    private LogFacility(final Integer value, String facilityDisplayName)
    {
        this.value = value;
        this.facilityDisplayName = facilityDisplayName;
    }

    public Integer getValue()
    {
        return value;
    }

    public String getFacilityDisplayName() {
        return facilityDisplayName;
    }

    public void setFacilityDisplayName(String facilityDisplayName) {
        this.facilityDisplayName = facilityDisplayName;
    }

    public static List<LogFacility> getLogEnumList() {
        return logFacilities;
    }


    public static String forValue(Integer value) {
        return valueToName.get(value);
    }
}
