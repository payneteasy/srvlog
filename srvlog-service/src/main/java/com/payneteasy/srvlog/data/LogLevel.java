package com.payneteasy.srvlog.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Date: 12.01.13
 * Time: 20:15
 */
public enum LogLevel implements LogEnum{

    EMERGENCY(0, "EMERGENCY"),
    ALERT(1, "ALERT"),
    CRITICAL(2, "CRITICAL"),
    ERROR(3, "ERROR"),
    WARN(4, "WARN"),
    NOTICE(5, "NOTICE"),
    INFO(6, "INFO"),
    DEBUG(7, "DEBUG");

    private static Map<Integer, String> valueToName = new HashMap<>();
    private static List<LogLevel> logLevelList = new ArrayList<>();
    private static Map<Integer, LogLevel> valueToLevel = new HashMap<>();

    static {
        for (LogLevel ll: LogLevel.values()) {
            valueToName.put(ll.getValue(), ll.name());
            valueToLevel.put(ll.getValue(), ll);
            logLevelList.add(ll);
        }
    }

    private final Integer value;
    private String levelDisplayName;

    LogLevel(final Integer value, String levelDisplayName)
    {
        this.value = value;
        this.levelDisplayName = levelDisplayName;
    }

    public Integer getValue() {
        return this.value;
    }

    public String getLevelDisplayName() {
        return levelDisplayName;
    }

    public void setLevelDisplayName(String levelDisplayName) {
        this.levelDisplayName = levelDisplayName;
    }

    public static List<LogLevel> getLogEnumList() {
        return logLevelList;
    }

    public static String forValue(Integer value) {
        return valueToName.get(value);
    }

    public static LogLevel levelForValue(Integer value) {
        return valueToLevel.get(value);
    }
}
