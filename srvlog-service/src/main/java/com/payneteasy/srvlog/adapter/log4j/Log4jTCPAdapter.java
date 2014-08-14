package com.payneteasy.srvlog.adapter.log4j;

import com.payneteasy.srvlog.adapter.AbstractTCPLoggerAdapter;
import com.payneteasy.srvlog.adapter.LoggerAcceptorNode;
import com.payneteasy.srvlog.adapter.utils.IRunnableFactory;
import com.payneteasy.srvlog.data.LogData;
import com.payneteasy.srvlog.service.ILogCollector;
import org.apache.log4j.spi.LoggingEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.Socket;

/**
 * Date: 19.06.13
 * Time: 20:04
 */
@Service
public class Log4jTCPAdapter extends AbstractTCPLoggerAdapter {
    @Autowired
    public Log4jTCPAdapter(
            ILogCollector logCollector,
            @Value("${log4jProgram}") String program,
            @Value("${log4jTCPPort}") int serverPort) {
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
                return new Log4jNode(clientSocket, Log4jTCPAdapter.this);
            }
        };
    }

    void processEvent(ServerLog4JEvent logEvent) {
        LogData logData = Log4jAdapterUtils.buildLogData(logEvent, getProgram());

        logger.info("New " + getLoggerTypeName() + " event caught");
        getLogCollector().saveLog(logData);
    }

    //Copied partially from log4j library
    private static class Log4jNode extends LoggerAcceptorNode<LoggingEvent> {

        private final Log4jTCPAdapter log4jAdapter;

        private Log4jNode(Socket socket, Log4jTCPAdapter log4jAdapter) {
            super(socket, logger);
            this.log4jAdapter = log4jAdapter;
        }

        @Override
        protected void processEvent(Socket socket, LoggingEvent event) {
            log4jAdapter.processEvent(new ServerLog4JEvent(event, socket.getInetAddress()));
        }
    }
}
