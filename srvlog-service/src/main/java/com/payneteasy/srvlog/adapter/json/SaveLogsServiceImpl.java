package com.payneteasy.srvlog.adapter.json;

import com.payneteasy.srvlog.adapter.json.messages.SaveLogsMessage;
import com.payneteasy.srvlog.adapter.json.messages.SaveLogsRequest;
import com.payneteasy.srvlog.adapter.json.messages.SaveLogsResponse;
import com.payneteasy.srvlog.data.LogData;
import com.payneteasy.srvlog.service.ILogCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SaveLogsServiceImpl implements ISaveLogsService {

    private static final Logger LOG = LoggerFactory.getLogger(SaveLogsServiceImpl.class);

    private final ILogCollector logCollector;

    @Autowired
    public SaveLogsServiceImpl(ILogCollector logCollector) {
        this.logCollector = logCollector;
    }

    @Override
    public SaveLogsResponse saveLogs(String aRemoveIpAddress, SaveLogsRequest aLogs) {
        if(aLogs.getMessages() == null) {
            return SaveLogsResponse.error(aLogs.getRequestId(), "No messages");
        }

        for (SaveLogsMessage message : aLogs.getMessages()) {
            LogData logData = mapMessage(aRemoveIpAddress, message);

            try {
                logCollector.saveLog(logData);
            } catch (Exception e) {
                LOG.error("Cannot save json: {}", logData, e);
            }
        }

        LOG.debug("Saved {} with {} messages from {}", aLogs.getRequestId(), aLogs.getMessages().size(), aRemoveIpAddress);

        return SaveLogsResponse.success(aLogs.getRequestId());
    }

    private LogData mapMessage(String aRemoveIpAddress, SaveLogsMessage aMessage) {
        LogData data = new LogData();
        data.setDate     ( new Date(aMessage.getTime()) );
        data.setHost     ( aRemoveIpAddress             );
        data.setFacility ( aMessage.getFacility()       );
        data.setSeverity ( aMessage.getSeverity()       );
        data.setMessage  ( aMessage.getMessage()        );
        data.setProgram  ( aMessage.getProgram()        );
        return data;
    }
}
