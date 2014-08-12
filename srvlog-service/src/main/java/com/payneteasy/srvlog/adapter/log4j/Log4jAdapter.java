package com.payneteasy.srvlog.adapter.log4j;

import com.payneteasy.srvlog.adapter.AbstractLoggerAdapter;
import com.payneteasy.srvlog.adapter.LoggerAcceptorNode;
import com.payneteasy.srvlog.adapter.utils.AdapterHelper;
import com.payneteasy.srvlog.adapter.utils.IRunnableFactory;
import com.payneteasy.srvlog.data.LogData;
import com.payneteasy.srvlog.data.LogFacility;
import com.payneteasy.srvlog.service.ILogCollector;
import org.apache.log4j.spi.LoggingEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.Socket;
import java.util.Date;

/**
 * Date: 19.06.13
 * Time: 20:04
 */
@Service
public class Log4jAdapter extends AbstractLoggerAdapter {
    @Autowired
    public Log4jAdapter(
            ILogCollector logCollector,
            @Value( "${log4jProgram}" ) String program,
            @Value( "${log4jPort}" )int serverPort) {
        super(logCollector, program, serverPort);
    }

    @Override
    protected String getLoggerTypeName() {
        return "log4j";
    }

    @Override
    protected IRunnableFactory createWorkerFactory() {
        return new IRunnableFactory() {
            @Override
            public Runnable createWorker(Socket clientSocket) {
                return new Log4jNode(clientSocket, Log4jAdapter.this);
            }
        };
    }

    void processEvent(ServerLog4JEvent logEvent) {
        LogData logData = new LogData();
        String address = logEvent.getHost().getHostAddress();
        logData.setHost(AdapterHelper.extractHostname(logEvent.getHost()));
        logData.setSeverity(logEvent.getLogEvent().getLevel().getSyslogEquivalent());
        logData.setFacility(LogFacility.user.getValue());
        logData.setDate(new Date(logEvent.getLogEvent().getTimeStamp()));
        logData.setMessage(getLog4Message(logEvent.getLogEvent()));
        logData.setProgram(getProgram());

        logger.info("New " + getLoggerTypeName() + " event caught");
        getLogCollector().saveLog(logData);
    }

    private String getLog4Message(LoggingEvent logEvent) {
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

    //Copied partially from log4j library
    private static class Log4jNode extends LoggerAcceptorNode<LoggingEvent> {

        private final Log4jAdapter log4jAdapter;

        private Log4jNode(Socket socket, Log4jAdapter log4jAdapter) {
            super(socket, logger);
            this.log4jAdapter = log4jAdapter;
        }

        @Override
        protected void processEvent(Socket socket, LoggingEvent event) {
            log4jAdapter.processEvent(new ServerLog4JEvent(event, socket.getInetAddress()));
        }
    }
}
