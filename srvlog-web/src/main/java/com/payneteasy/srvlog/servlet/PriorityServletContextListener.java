package com.payneteasy.srvlog.servlet;

import ch.qos.logback.ext.spring.web.LogbackConfigListener;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoaderListener;

import jakarta.servlet.ServletContextEvent;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.ServletContextListener;

public class PriorityServletContextListener implements ServletContextListener {

    private static final Logger LOG = LoggerFactory.getLogger(PriorityServletContextListener.class);

    private final List<ServletContextListener> listeners;

    public PriorityServletContextListener() {
        this.listeners = new ArrayList<>();
        //TODO configure logback for loggin during context init listeners.add(new LogbackConfigListener());
        listeners.add(new ContextLoaderListener());
    }

    @Override
    public void contextInitialized(ServletContextEvent aEvent) {
        LOG.info("contextInitialized() ...");
        for (ServletContextListener listener : listeners) {
            LOG.info("Initializing {} ...", listener.getClass().getSimpleName());
            listener.contextInitialized(aEvent);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent aEvent) {
        LOG.info("contextDestroyed() ...");
        for (ServletContextListener listener : Lists.reverse(listeners) ) {
            LOG.info("Destroying {} ...", listener.getClass().getSimpleName());
            try {
                listener.contextDestroyed(aEvent);
            } catch (Exception e) {
                LOG.error("Cannot destroy listener", e);
            }
        }
    }
}
