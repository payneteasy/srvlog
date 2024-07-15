package com.payneteasy.srvlog.adapter.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.payneteasy.srvlog.adapter.AbstractTCPLoggerAdapter;
import com.payneteasy.srvlog.adapter.LoggerAcceptorNode;
import com.payneteasy.srvlog.adapter.utils.IRunnableFactory;
import com.payneteasy.srvlog.data.LogData;
import com.payneteasy.srvlog.service.ILogCollector;
import com.payneteasy.startup.parameters.StartupParametersFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.Socket;

@Service
public class LogbackTCPAdapter extends AbstractTCPLoggerAdapter {

    private static final ILogbackAdapterConfig adapterConfig = StartupParametersFactory
            .getStartupParameters(ILogbackAdapterConfig.class);

    @Autowired
    public LogbackTCPAdapter(ILogCollector logCollector) {
        super(logCollector, adapterConfig.getProgram(), adapterConfig.getTcpPort());
    }

    public LogbackTCPAdapter(ILogCollector logCollector, String program, int serverPort) {
        super(logCollector, program, serverPort);
    }

    @Override
    protected String getLoggerTypeName() {
        return "logback";
    }

    @Override
    protected IRunnableFactory createWorkerFactory() {
        return clientSocket -> new LogbackNode(clientSocket, LogbackTCPAdapter.this);
    }

    void processEvent(ServerLogbackEvent logEvent) {
        logger.debug("Obtain message from syslog: {}", logEvent.getLogEvent().getFormattedMessage());

        LogData logData = LogbackAdapterUtils.buildLogData(logEvent, LogbackAdapterUtils.getLogbackProgram(logEvent.getLogEvent(), getProgram()));

        logger.debug("Saving " + getLoggerTypeName() + " event: {}", logData);
        getLogCollector().saveLog(logData);
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
