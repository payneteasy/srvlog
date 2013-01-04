package com.payneteasy.srvlog.service.impl;

import com.nesscomputing.syslog4j.server.SyslogServer;
import com.nesscomputing.syslog4j.server.SyslogServerEventIF;
import com.nesscomputing.syslog4j.server.SyslogServerIF;
import com.payneteasy.srvlog.dao.ILogManagerDao;
import com.payneteasy.srvlog.data.LogData;
import com.payneteasy.srvlog.service.ILogManager;
import com.payneteasy.srvlog.service.ILogManagerConfig;
import com.payneteasy.srvlog.service.LogManagerEventHandler;
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
public class LogManagerImpl implements ILogManager {

    private static final Logger LOG = LoggerFactory.getLogger(LogManagerImpl.class);

    @PostConstruct
    public void init(){
            LOG.info("  Starting syslog4j server....");

            syslog4jInstance = SyslogServer.getInstance(logManagerConfig.getSyslog4jProtocol());
            syslog4jInstance.getConfig().setPort(logManagerConfig.getSyslog4jPort());
            syslog4jInstance.getConfig().addEventHandler(new LogManagerEventHandler() {
                @Override
                public void event(SyslogServerIF syslogServer, SocketAddress socketAddress, SyslogServerEventIF event) {
                    LogData log = new LogData();
                    log.setDate(event.getDate());
                    log.setFacility(event.getFacility().getValue());
                    log.setHost(event.getHost());
                    log.setMessage(event.getMessage());
                    log.setSeverity(event.getLevel().getValue());

                    logManagerDao.saveLog(log);
                }

                @Override
                public void exception(SyslogServerIF syslogServer, SocketAddress socketAddress, Exception exception) {
                    LOG.error(" Error while initializing log server", exception);
                }
            });
            SyslogServer.getThreadedInstance(logManagerConfig.getSyslog4jProtocol());

            for(int i=0; i<5 && !syslog4jInstance.isStarted(); i++) {
                LOG.info("  Waiting syslog4j server to be run ...");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    LOG.error(" Can't run syslog4j server", e);
                }
            }
    }


    @PreDestroy
    public void destroy(){
        LOG.info("  Stopping syslog4j server ...");
        syslog4jInstance.shutdown();

        for(int i=0; i<5 && !syslog4jInstance.isStopped(); i++) {
            LOG.info("  Waiting syslog4j server to be stop ...");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                LOG.error(" Can't stop syslog4j server", e);
            }
        }
    }

    @Override
    public boolean isStarted() {
        return syslog4jInstance.isStarted();
    }

    private SyslogServerIF syslog4jInstance;

    public void setLogManagerConfig(ILogManagerConfig logManagerConfig) {
        this.logManagerConfig = logManagerConfig;
    }

    public void setLogManagerDao(ILogManagerDao logManagerDao) {
        this.logManagerDao = logManagerDao;
    }

    @Autowired
    private ILogManagerConfig logManagerConfig;

    @Autowired
    private ILogManagerDao logManagerDao;
}
