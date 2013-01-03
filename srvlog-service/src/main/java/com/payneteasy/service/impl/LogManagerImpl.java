package com.payneteasy.service.impl;

import com.nesscomputing.syslog4j.server.SyslogServer;
import com.nesscomputing.syslog4j.server.SyslogServerIF;
import com.payneteasy.service.ILogManager;
import com.payneteasy.service.ILogManagerEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
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

            syslog4jInstance = SyslogServer.getInstance(syslog4jProtocol);
            syslog4jInstance.getConfig().setPort(syslog4jPort);
            syslog4jInstance.getConfig().addEventHandler(logManagerEventHandler);
            SyslogServer.getThreadedInstance("udp");

            for(int i=0; i<5 && !syslog4jInstance.isStarted(); i++) {
                LOG.info("  Waiting syslog4j server to be run ...");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    LOG.error("{}: Can't run syslog4j server", e);
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

    @Override
    public String getSyslog4jProtocol() {
        return syslog4jProtocol;
    }

    @Override
    public int getSyslog4jPort() {
        return syslog4jPort;
    }

    @Value( "${port}" )
    private int syslog4jPort;

    @Value( "${protocol}" )
    private String syslog4jProtocol;

    private SyslogServerIF syslog4jInstance;

    @Autowired
    private ILogManagerEventHandler logManagerEventHandler;
}
