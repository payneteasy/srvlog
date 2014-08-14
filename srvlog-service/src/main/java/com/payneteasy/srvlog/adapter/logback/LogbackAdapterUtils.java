package com.payneteasy.srvlog.adapter.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import ch.qos.logback.classic.util.LevelToSyslogSeverity;
import com.payneteasy.srvlog.adapter.utils.AdapterHelper;
import com.payneteasy.srvlog.data.LogData;
import com.payneteasy.srvlog.data.LogFacility;

import java.util.Date;

/**
 * @author rpuch
 */
class LogbackAdapterUtils {
    public static LogData buildLogData(ServerLogbackEvent logEvent, String program) {
        LogData logData = new LogData();
        String address = logEvent.getHost().getHostAddress();
        logData.setHost(AdapterHelper.extractHostname(logEvent.getHost()));
        logData.setSeverity(LevelToSyslogSeverity.convert(logEvent.getLogEvent()));
        logData.setFacility(LogFacility.user.getValue());
        logData.setDate(new Date(logEvent.getLogEvent().getTimeStamp()));
        logData.setMessage(getLogbackMessage(logEvent.getLogEvent()));
        logData.setProgram(program);
        return logData;
    }

    static String getLogbackMessage(ILoggingEvent logEvent) {
        StringBuilder builder = new StringBuilder();
        builder.append(logEvent.getMessage());
        if (logEvent.getThrowableProxy() != null) {
            builder.append("\n");
            builder.append(ThrowableProxyUtil.asString(logEvent.getThrowableProxy()));
        }
        return builder.toString();
    }
}
