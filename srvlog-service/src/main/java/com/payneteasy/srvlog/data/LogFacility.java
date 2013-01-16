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

    kern(0), user(1), mail(2), daemon(3), auth(4), syslog(5),
    lpr(6), news(7), uucp(8), cron(9), authpriv(10), ftp(11),
    ntp(12), audit(13), alert(14), clock(15),
    local0(16), local1(17), local2(18), local3(19), local4(20),
    local5(21), local6(22), local7(23);

    private static Map<Integer, String> valueToName = new HashMap<Integer, String>();
    private static List<LogFacility> logFacilities = new ArrayList<LogFacility>();

    static {
        for (LogFacility lf: LogFacility.values()) {
            valueToName.put(lf.getValue(), lf.name());
            logFacilities.add(lf);
        }
    }

    private final Integer value;

    private LogFacility(final Integer value)
    {
        this.value = value;
    }

    public Integer getValue()
    {
        return value;
    }

    public static List<LogFacility> getLogEnumList() {
        return logFacilities;
    }


    public static String forValue(Integer value) {
        return valueToName.get(value);
    }
}
