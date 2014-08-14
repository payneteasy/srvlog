package com.payneteasy.srvlog.adapter.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.payneteasy.srvlog.adapter.udp.Datagram;
import com.payneteasy.srvlog.adapter.udp.IDatagramProcessor;
import com.payneteasy.srvlog.adapter.udp.UDPServer;
import com.payneteasy.srvlog.data.LogData;
import com.payneteasy.srvlog.service.ILogCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

@Service
public class LogbackUDPAdapter {
    protected static final Logger logger = LoggerFactory.getLogger(LogbackUDPAdapter.class);
    private final ILogCollector logCollector;
    private final String program;
    private final int serverPort;
    private UDPServer server;

    @Autowired
    public LogbackUDPAdapter(
            ILogCollector logCollector,
            @Value("${logbackProgram}") String program,
            @Value("${logbackUDPPort}") int serverPort) {
        this.logCollector = logCollector;
        this.program = program;
        this.serverPort = serverPort;
    }

    private String getLoggerTypeName() {
        return "logback";
    }

    void processEvent(ServerLogbackEvent logEvent) {
        String program = getProgram();
        LogData logData = LogbackAdapterUtils.buildLogData(logEvent, program);

        logger.info("New " + getLoggerTypeName() + " event caught");
        getLogCollector().saveLog(logData);
    }

    @PostConstruct
    public void init() {
        server = new UDPServer(serverPort, 1000, new LogbackProcessor(this));
        server.start();

        logger.info(getLoggerTypeName() + " UDP server successfully started on port={}", serverPort);
    }

    @PreDestroy
    public void destroy() {
        logger.info("  Stopping " + getLoggerTypeName() + " UDP server ...");
        server.stop();
    }

    public ILogCollector getLogCollector() {
        return logCollector;
    }

    public String getProgram() {
        return program;
    }

    private static class LogbackProcessor implements IDatagramProcessor {

        private final LogbackUDPAdapter logbackAdapter;

        private LogbackProcessor(LogbackUDPAdapter logbackAdapter) {
            this.logbackAdapter = logbackAdapter;
        }

        @Override
        public void processDatagram(Datagram datagram) {
            ILoggingEvent event;
            try {
                ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(datagram.getData()));
                event = (ILoggingEvent) ois.readObject();
            } catch (IOException e) {
                throw new IllegalStateException("Cannot read event data", e);
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException("Cannot read event data", e);
            }
            logbackAdapter.processEvent(new ServerLogbackEvent(event, datagram.getSourceAddress()));
        }
    }
}
