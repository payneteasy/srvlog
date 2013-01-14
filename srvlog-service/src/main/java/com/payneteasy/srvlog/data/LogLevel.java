package com.payneteasy.srvlog.data;

import java.util.HashMap;
import java.util.Map;

/**
 * Date: 12.01.13
 * Time: 20:15
 */
public enum LogLevel {

    EMERGENCY(0), ALERT(1), CRITICAL(2), ERROR(3), WARN(4),
    NOTICE(5), INFO(6), DEBUG(7);

    private static Map<Integer, String> valueToName = new HashMap<Integer, String>();

    static {
        for (LogLevel ll: LogLevel.values()) {
            valueToName.put(ll.getValue(), ll.name());
        }
    }

    private final Integer value;

    private LogLevel(final Integer value)
    {
        this.value = value;
    }

    public Integer getValue() {
        return this.value;
    }

    public static String forValue(Integer value) {
        return valueToName.get(value);
    }
}