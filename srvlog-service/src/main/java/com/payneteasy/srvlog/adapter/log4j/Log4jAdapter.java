package com.payneteasy.srvlog.adapter.log4j;

import com.payneteasy.srvlog.adapter.utils.AdapterHelper;
import com.payneteasy.srvlog.adapter.utils.IRunnableFacotry;
import com.payneteasy.srvlog.adapter.utils.ThreadPooledServer;
import com.payneteasy.srvlog.data.LogData;
import com.payneteasy.srvlog.data.LogFacility;
import com.payneteasy.srvlog.service.ILogCollector;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.spi.LoggingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Date: 19.06.13
 * Time: 20:04
 */
@Service
public class Log4jAdapter {

    private static final Logger logger = LoggerFactory.getLogger(Log4jAdapter.class);

    @Autowired
    private ILogCollector logCollector;

    @Value( "${log4jProgram}" )
    private String program;

    @Value( "${log4jPort}" )
    private int log4jPort;

    private ThreadPooledServer server = null;
    private ExecutorService serverRunner = Executors.newSingleThreadExecutor();

    @PostConstruct
    public void init() {
        server = new ThreadPooledServer(log4jPort,  new IRunnableFacotry() {
            @Override
            public Runnable createWorker(Socket clientSocket) {
                return new Log4jNode(clientSocket, Log4jAdapter.this);
            }
        });
        serverRunner.execute(server);
        logger.info("  Waiting for log4j server to be run ...");
        for (int i = 0; i < 10 && !server.isStarted(); i++) {

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                logger.error(" Can't run log4j server", e);
            }
            logger.info("  Waiting for log4j server to be run. {} seconds passed ", i);
        }

        logger.info("log4j server successfully started on port={}", log4jPort);
    }

    @PreDestroy
    public void destroy() {
        logger.info("  Stopping log4j server ...");

        server.stop();

        for (int i = 0; i < 5 && !server.isStopped(); i++) {
            logger.info("  Waiting for log4j server to stop ...");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                logger.error(" Can't stop log4j server", e);
            }
        }
        if (!server.isStopped()) {
            logger.warn("log4j server was not stopped in 5 seconds interval");
        }
    }


    public void setLogCollector(ILogCollector logCollector) {
        this.logCollector = logCollector;
    }

    public ILogCollector getLogCollector() {
        return logCollector;
    }

    public void processEvent(ServerLog4JEvent logEvent) {
        LogData logData = new LogData();
        String address = logEvent.getHost().getHostAddress();
        logData.setHost(AdapterHelper.extractHostname(logEvent.getHost()));
        logData.setSeverity(logEvent.getLogEvent().getLevel().getSyslogEquivalent());
        logData.setFacility(LogFacility.user.getValue());
        logData.setDate(new Date(logEvent.getLogEvent().getTimeStamp()));
        logData.setMessage(getLog4Message(logEvent.getLogEvent()));
        logData.setProgram(program);

        logger.info("New log4j event caught");
        logCollector.saveLog(logData);
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
        return builder.toString();  //To change body of created methods use File | Settings | File Templates.
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getProgram() {
        return program;
    }

    //Copied partially from log4j library
    private class Log4jNode implements Runnable {

        ObjectInputStream ois;
        private final Socket socket;
        private final Log4jAdapter log4jAdapter;

        public Log4jNode(Socket socket, Log4jAdapter log4jAdapter) {
            this.socket = socket;
            this.log4jAdapter = log4jAdapter;
            try {
                ois = new ObjectInputStream(
                        new BufferedInputStream(socket.getInputStream()));
            } catch(InterruptedIOException e) {
                Thread.currentThread().interrupt();
                logger.error("Could not open ObjectInputStream to "+socket, e);
            } catch(IOException e) {
                logger.error("Could not open ObjectInputStream to "+socket, e);
            } catch(RuntimeException e) {
                logger.error("Could not open ObjectInputStream to "+socket, e);
            }

        }

        @Override
        public void run() {
            try {
                if (ois != null) {
                    while(true) {
                        LoggingEvent event = (LoggingEvent) ois.readObject();
                        log4jAdapter.processEvent(new ServerLog4JEvent(event, socket.getInetAddress()));
                    }
                }
            } catch(java.io.EOFException e) {
                logger.info("Caught java.io.EOFException closing conneciton.");
            } catch(java.net.SocketException e) {
                logger.info("Caught java.net.SocketException closing conneciton.");
            } catch(InterruptedIOException e) {
                Thread.currentThread().interrupt();
                logger.info("Caught java.io.InterruptedIOException: "+e);
                logger.info("Closing connection.");
            } catch(IOException e) {
                logger.info("Caught java.io.IOException: "+e);
                logger.info("Closing connection.");
            } catch(Exception e) {
                logger.error("Unexpected exception. Closing conneciton.", e);
            } finally {
                IOUtils.closeQuietly(ois);
                IOUtils.closeQuietly(socket);
            }
        }
    }
}
