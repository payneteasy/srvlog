package com.payneteasy.srvlog.adapter.json;

import com.payneteasy.http.server.log.IHttpLogger;
import com.payneteasy.http.server.log.LogMessageFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpLoggerSlf implements IHttpLogger {

    private static final Logger LOG = LoggerFactory.getLogger(HttpLoggerSlf.class);

    private final LogMessageFormatter formatter = new LogMessageFormatter();

    @Override
    public void debug(String aTemplate, Object... objects) {
        LOG.debug("{}", formatter.format(aTemplate, objects));
    }

    @Override
    public void error(String aMessage) {
        LOG.error(aMessage);
    }

    @Override
    public void error(String aMessage, Exception e) {
        LOG.error(aMessage, e);
    }
}
