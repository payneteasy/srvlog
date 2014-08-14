package com.payneteasy.srvlog.adapter.log4j;

import com.payneteasy.srvlog.adapter.utils.AdapterHelper;
import com.payneteasy.srvlog.data.LogData;
import com.payneteasy.srvlog.data.LogFacility;
import org.apache.log4j.spi.LoggingEvent;

import java.util.Date;

/**
 * @author rpuch
 */
class Log4jAdapterUtils {
    static LogData buildLogData(ServerLog4JEvent logEvent, String program) {
        LogData logData = new LogData();
        String address = logEvent.getHost().getHostAddress();
        logData.setHost(AdapterHelper.extractHostname(logEvent.getHost()));
        logData.setSeverity(logEvent.getLogEvent().getLevel().getSyslogEquivalent());
        logData.setFacility(LogFacility.user.getValue());
        logData.setDate(new Date(logEvent.getLogEvent().getTimeStamp()));
        logData.setMessage(getLog4Message(logEvent.getLogEvent()));
        logData.setProgram(program);
        return logData;
    }

    static String getLog4Message(LoggingEvent logEvent) {
        StringBuilder builder = new StringBuilder();
        builder.append(logEvent.getMessage());
        if (logEvent.getThrowableStrRep() != null) {
            builder.append("\n");
            for (String s: logEvent.getThrowableStrRep()) {
                builder.append("\t").append(s).append("\n");
            }
        }
        return builder.toString();
    }
}
