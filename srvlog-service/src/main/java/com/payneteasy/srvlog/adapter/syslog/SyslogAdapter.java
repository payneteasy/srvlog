package com.payneteasy.srvlog.adapter.syslog;

import com.nesscomputing.syslog4j.impl.message.structured.StructuredSyslogMessageIF;
import com.nesscomputing.syslog4j.server.SyslogServer;
import com.nesscomputing.syslog4j.server.SyslogServerEventIF;
import com.nesscomputing.syslog4j.server.SyslogServerIF;
import com.nesscomputing.syslog4j.server.SyslogServerSessionlessEventHandlerIF;
import com.nesscomputing.syslog4j.server.impl.event.structured.StructuredSyslogServerEvent;
import com.payneteasy.srvlog.data.LogData;
import com.payneteasy.srvlog.data.LogFacility;
import com.payneteasy.srvlog.data.LogLevel;
import com.payneteasy.srvlog.service.ILogCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

/**
 * Date: 03.01.13 Time: 16:07
 */
@Service
public class SyslogAdapter implements SyslogServerSessionlessEventHandlerIF {

    private static final Logger LOG = LoggerFactory.getLogger(SyslogAdapter.class);

    @Autowired
    private ILogCollector logCollector;

    @Autowired
    private ISyslogAdapterConfig logAdapterConfig;


    private SyslogServerIF syslog4jInstance;

    @PostConstruct
    public void init() {
        int port = logAdapterConfig.getSyslogPort();
        String protocol = logAdapterConfig.getSyslogProtocol();
        LOG.info("  Starting syslog4j server on port = {} and on protocol = {} ...", port, protocol );


        // Stupid thing which needs to be done for integration tests. However it does not affect application performance anyhow
        SyslogServer.shutdown();
        SyslogServer.initialize();
        // Now create a new instance of Syslog.
        syslog4jInstance = SyslogServer.getInstance(protocol);
        syslog4jInstance.getConfig().setPort(port);
        syslog4jInstance.getConfig().addEventHandler(this);
        syslog4jInstance.getConfig().setUseStructuredData(true);

        LOG.info("  Trying to obtain threaded instance of syslog server....");
        SyslogServer.getThreadedInstance(protocol);

        LOG.info("  Waiting for syslog4j server to be run ...");
        for (int i = 0; i < 10 && !syslog4jInstance.isStarted(); i++) {

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                LOG.error(" Can't run syslog4j server", e);
            }
            LOG.info("  Waiting for syslog4j server to be run. {} seconds passed ", i);
        }

        LOG.info("Syslog server successfully started on port={}, using protocol={}", port, protocol);
    }


    @PreDestroy
    public void destroy() {
        LOG.info("  Stopping syslog4j server ...");

        //syslog4jInstance.shutdown();

        //SyslogServer.destroyInstance(syslog4jInstance);
        SyslogServer.shutdown();


        for (int i = 0; i < 5 && !syslog4jInstance.isStopped(); i++) {
            LOG.info("  Waiting syslog4j server to be stop ...");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                LOG.error(" Can't stop syslog4j server", e);
            }
        }
        if (!syslog4jInstance.isStopped()) {
            LOG.info("Syslog server was not stopped in 5 seconds interval");
        }
    }


    public void setLogAdapterConfig(ISyslogAdapterConfig logAdapterConfig) {
        this.logAdapterConfig = logAdapterConfig;
    }

    @Override
    public void event(SyslogServerIF syslogServer, SocketAddress socketAddress, SyslogServerEventIF event) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Obtain message from syslog: {}", new String(event.getRaw()));
        }
        LogData log = new LogData();
        log.setDate(event.getDate());

        log.setFacility(event.getFacility() == null ? LogFacility.local0.getValue() : (event.getFacility().getValue() >> 3));
        log.setSeverity(event.getLevel() == null ? LogLevel.INFO.getValue() : (event.getLevel().getValue()));

        if (event instanceof StructuredSyslogServerEvent) { //rfc5424
            log.setHost(event.getHost());
            log.setProgram(((StructuredSyslogServerEvent) event).getApplicationName());
            log.setMessage(event.getMessage());
        } else {  //rfc3164
            log.setHost(event.getHost());

            String message = event.getMessage();

            int tagIdx = message.indexOf("[");

            int tagEndIdx = message.indexOf(":");

            if (tagIdx == -1) {
                message = message.substring(tagEndIdx + 1);
            } else if (tagEndIdx > -1 && tagEndIdx < tagIdx) {
                message = parseProgramField(log, message, tagEndIdx);
            } else if (tagIdx > -1) {
                message = parseProgramField(log, message, tagIdx);
            }

            log.setMessage(message);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Saving log: " + log);
        }
        logCollector.saveLog(log);
    }

    private String parseProgramField(LogData log, String message, int tagTerminationIdx) {
        String hostAndTag = message.substring(0, tagTerminationIdx);
        if (hostAndTag.split(" ").length <= 2 && hostAndTag.length() <= 32) { //rfc 3164
            message = message.substring(tagTerminationIdx);
            String[] hostAndTagSplited = hostAndTag.split(" ");
            if (hostAndTagSplited.length > 1) {
                log.setProgram(hostAndTagSplited[1]);
            } else {
                log.setProgram(hostAndTagSplited[0]);
            }
        }
        return message;
    }

    @Override
    public void exception(SyslogServerIF syslogServer, SocketAddress socketAddress, Exception exception) {
        LOG.error("While listening syslog.", exception);
    }

    @Override
    public void initialize(SyslogServerIF syslogServer) {
        LOG.info("Initializing syslog server on port={} using protocol={}", syslogServer.getActualPort(), syslogServer.getProtocol());
    }

    @Override
    public void destroy(SyslogServerIF syslogServer) {
        LOG.info("Destroying syslog server on port={} using protocol={}", syslogServer.getConfig().getPort(), syslogServer.getProtocol());
    }

    public void setLogCollector(ILogCollector logCollector) {
        this.logCollector = logCollector;
    }
}
