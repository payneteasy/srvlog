package com.payneteasy.srvlog.adapter.log4j;

import com.payneteasy.srvlog.adapter.logback.AbstractUDPLoggerAdapter;
import com.payneteasy.srvlog.adapter.udp.Datagram;
import com.payneteasy.srvlog.adapter.udp.IDatagramProcessor;
import com.payneteasy.srvlog.data.LogData;
import com.payneteasy.srvlog.service.ILogCollector;
import org.apache.log4j.spi.LoggingEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

@Service
public class Log4jUDPAdapter extends AbstractUDPLoggerAdapter {

    @Autowired
    public Log4jUDPAdapter(
            ILogCollector logCollector,
            @Value("${log4jProgram}") String program,
            @Value("${log4jUDPPort}") int serverPort) {
        super(serverPort, program, logCollector);
    }

    @Override
    protected String getLoggerTypeName() {
        return "log4j";
    }

    void processEvent(ServerLog4JEvent logEvent) {
        LogData logData = Log4jAdapterUtils.buildLogData(logEvent, getProgram());

        logger.info("New " + getLoggerTypeName() + " event caught");
        getLogCollector().saveLog(logData);
    }

    @Override
    protected IDatagramProcessor createProcessor() {
        return new Log4jProcessor(this);
    }

    private static class Log4jProcessor implements IDatagramProcessor {

        private final Log4jUDPAdapter logbackAdapter;

        private Log4jProcessor(Log4jUDPAdapter logbackAdapter) {
            this.logbackAdapter = logbackAdapter;
        }

        @Override
        public void processDatagram(Datagram datagram) {
            LoggingEvent event;
            try {
                ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(datagram.getData()));
                event = (LoggingEvent) ois.readObject();
            } catch (IOException e) {
                throw new IllegalStateException("Cannot read event data", e);
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException("Cannot read event data", e);
            }
            logbackAdapter.processEvent(new ServerLog4JEvent(event, datagram.getSourceAddress()));
        }
    }
}
