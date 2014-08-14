package com.payneteasy.srvlog.adapter.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.payneteasy.srvlog.adapter.AbstractTCPLoggerAdapter;
import com.payneteasy.srvlog.adapter.LoggerAcceptorNode;
import com.payneteasy.srvlog.adapter.utils.IRunnableFactory;
import com.payneteasy.srvlog.data.LogData;
import com.payneteasy.srvlog.service.ILogCollector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.Socket;

@Service
public class LogbackTCPAdapter extends AbstractTCPLoggerAdapter {
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
        LogData logData = LogbackAdapterUtils.buildLogData(logEvent, getProgram());

        logger.info("New " + getLoggerTypeName() + " event caught");
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
