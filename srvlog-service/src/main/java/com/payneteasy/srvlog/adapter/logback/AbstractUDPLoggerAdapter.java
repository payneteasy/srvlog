package com.payneteasy.srvlog.adapter.logback;

import com.payneteasy.srvlog.adapter.udp.IDatagramProcessor;
import com.payneteasy.srvlog.adapter.udp.UDPServer;
import com.payneteasy.srvlog.service.ILogCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author rpuch
 */
public abstract class AbstractUDPLoggerAdapter {
    protected static final Logger logger = LoggerFactory.getLogger(AbstractUDPLoggerAdapter.class);
    protected final ILogCollector logCollector;
    protected final String program;
    protected final int serverPort;
    private UDPServer server;

    protected AbstractUDPLoggerAdapter(
            int serverPort, String program, ILogCollector logCollector) {
        this.serverPort = serverPort;
        this.logCollector = logCollector;
        this.program = program;
    }

    protected abstract String getLoggerTypeName();

    @PostConstruct
    public void init() {
        server = new UDPServer(serverPort, 1000, createProcessor());
        server.start();

        logger.info(getLoggerTypeName() + " UDP server successfully started on port={}", serverPort);
    }

    protected abstract IDatagramProcessor createProcessor();

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
}
