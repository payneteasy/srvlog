package com.payneteasy.srvlog.adapter.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import ch.qos.logback.classic.util.LevelToSyslogSeverity;
import com.payneteasy.srvlog.adapter.AbstractLoggerAdapter;
import com.payneteasy.srvlog.adapter.LoggerAcceptorNode;
import com.payneteasy.srvlog.adapter.utils.AdapterHelper;
import com.payneteasy.srvlog.adapter.utils.IRunnableFactory;
import com.payneteasy.srvlog.data.LogData;
import com.payneteasy.srvlog.data.LogFacility;
import com.payneteasy.srvlog.service.ILogCollector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.Socket;
import java.util.Date;

@Service
public class LogbackTCPAdapter extends AbstractLoggerAdapter {
    @Autowired
    public LogbackTCPAdapter(
            ILogCollector logCollector,
            @Value("${logbackProgram}") String program,
            @Value("${logbackTCPPort}") int serverPort) {
        super(logCollector, program, serverPort);
    }

    @Override
    protected String getLoggerTypeName() {
        return "logback";
    }

    @Override
    protected IRunnableFactory createWorkerFactory() {
        return new IRunnableFactory() {
            @Override
            public Runnable createWorker(Socket clientSocket) {
                return new LogbackNode(clientSocket, LogbackTCPAdapter.this);
            }
        };
    }

    void processEvent(ServerLogbackEvent logEvent) {
        LogData logData = new LogData();
        String address = logEvent.getHost().getHostAddress();
        logData.setHost(AdapterHelper.extractHostname(logEvent.getHost()));
        logData.setSeverity(LevelToSyslogSeverity.convert(logEvent.getLogEvent()));
        logData.setFacility(LogFacility.user.getValue());
        logData.setDate(new Date(logEvent.getLogEvent().getTimeStamp()));
        logData.setMessage(getLogbackMessage(logEvent.getLogEvent()));
        logData.setProgram(getProgram());

        logger.info("New " + getLoggerTypeName() + " event caught");
        getLogCollector().saveLog(logData);
    }

    private String getLogbackMessage(ILoggingEvent logEvent) {
        StringBuilder builder = new StringBuilder();
        builder.append(logEvent.getMessage());
        if (logEvent.getThrowableProxy() != null) {
            builder.append("\n");
            builder.append(ThrowableProxyUtil.asString(logEvent.getThrowableProxy()));
        }
        return builder.toString();
    }

    //Copied partially from log4j library
    private static class LogbackNode extends LoggerAcceptorNode<ILoggingEvent> {

        private final LogbackTCPAdapter logbackAdapter;

        private LogbackNode(Socket socket, LogbackTCPAdapter logbackAdapter) {
            super(socket, logger);
            this.logbackAdapter = logbackAdapter;
        }

        @Override
        protected void processEvent(Socket socket, ILoggingEvent event) {
            logbackAdapter.processEvent(new ServerLogbackEvent(event, socket.getInetAddress()));
        }
    }
}
