package com.payneteasy.srvlog.adapter.syslog;

import com.nesscomputing.syslog4j.server.SyslogServer;
import com.nesscomputing.syslog4j.server.SyslogServerEventIF;
import com.nesscomputing.syslog4j.server.SyslogServerIF;
import com.nesscomputing.syslog4j.server.SyslogServerSessionlessEventHandlerIF;
import com.payneteasy.srvlog.data.LogData;
import com.payneteasy.srvlog.service.ILogCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.SocketAddress;
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
    public void init(){
            LOG.info("  Starting syslog4j server....");

            // Stupid thing which needs to be done for integration tests. However it does not affect application performance anyhow
            SyslogServer.shutdown();
            SyslogServer.initialize();
            // Now create a new instance of Syslog.
            syslog4jInstance = SyslogServer.getInstance(logAdapterConfig.getSyslogProtocol());
            syslog4jInstance.getConfig().setPort(logAdapterConfig.getSyslogPort());
            syslog4jInstance.getConfig().addEventHandler(this);

            LOG.info("  Trying to obtain threaded instance of syslog server....");
            SyslogServer.getThreadedInstance(logAdapterConfig.getSyslogProtocol());

            LOG.info("  Waiting for syslog4j server to be run ...");
            for(int i=0; i<10 && !syslog4jInstance.isStarted(); i++) {

                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    LOG.error(" Can't run syslog4j server", e);
                }
                LOG.info("  Waiting for syslog4j server to be run. {} seconds passed ", i);
            }
    }


    @PreDestroy
    public void destroy(){
        LOG.info("  Stopping syslog4j server ...");

        //syslog4jInstance.shutdown();

        //SyslogServer.destroyInstance(syslog4jInstance);
        SyslogServer.shutdown();



        for(int i=0; i<5 && !syslog4jInstance.isStopped(); i++) {
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

        LogData log = new LogData();
        log.setDate(event.getDate());
        log.setFacility(event.getFacility().getValue());
        log.setHost(event.getHost());
        log.setMessage(event.getMessage());
        log.setSeverity(event.getLevel().getValue());
        if (LOG.isDebugEnabled()) {
            LOG.debug("Saving log: " + log);
        }
        logCollector.saveLog(log);
    }

    @Override
    public void exception(SyslogServerIF syslogServer, SocketAddress socketAddress, Exception exception) {
        LOG.error("Error while retrieving log.", exception);
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
