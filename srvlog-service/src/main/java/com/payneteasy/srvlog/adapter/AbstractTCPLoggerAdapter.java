package com.payneteasy.srvlog.adapter;

import com.payneteasy.srvlog.adapter.utils.IRunnableFactory;
import com.payneteasy.srvlog.adapter.utils.ThreadPooledServer;
import com.payneteasy.srvlog.service.ILogCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public abstract class AbstractTCPLoggerAdapter {

    protected static final Logger logger = LoggerFactory.getLogger(AbstractTCPLoggerAdapter.class);

    private final ILogCollector logCollector;
    private final String program;
    private final int serverPort;

    private ThreadPooledServer server = null;
    private final ExecutorService serverRunner = Executors.newSingleThreadExecutor();

    protected AbstractTCPLoggerAdapter(ILogCollector logCollector, String program, int serverPort) {
        this.logCollector = logCollector;
        this.program = program;
        this.serverPort = serverPort;
    }

    @PostConstruct
    public void init() {
        server = new ThreadPooledServer(serverPort, createWorkerFactory());
        serverRunner.execute(server);
        logger.info("  Waiting for " + getLoggerTypeName() + " TCP server to be run ...");
        for (int i = 0; i < 10 && !server.isStarted(); i++) {

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                logger.error(" Can't run " + getLoggerTypeName() + " TCP server", e);
            }
            logger.info("  Waiting for " + getLoggerTypeName() + " TCP server to be run. {} seconds passed ", i);
        }

        if (!server.isStarted()) {
            throw new IllegalStateException("Could not start " + getLoggerTypeName() + " TCP server on port " + serverPort);
        }

        logger.info(getLoggerTypeName() + " TCP server successfully started on port={}", serverPort);
    }

    protected abstract String getLoggerTypeName();

    protected abstract IRunnableFactory createWorkerFactory();

    @PreDestroy
    public void destroy() {
        logger.info("  Stopping " + getLoggerTypeName() + " TCP server ...");

        server.stop();

        for (int i = 0; i < 5 && !server.isStopped(); i++) {
            logger.info("  Waiting for " + getLoggerTypeName() + " TCP server to stop ...");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                logger.error(" Can't stop " + getLoggerTypeName() + " TCP server", e);
            }
        }
        if (!server.isStopped()) {
            logger.warn(getLoggerTypeName() + " TCP server was not stopped in 5 seconds interval");
        }
    }


    public ILogCollector getLogCollector() {
        return logCollector;
    }

    public String getProgram() {
        return program;
    }
}
