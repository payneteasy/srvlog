package com.payneteasy.srvlog.service;

import com.payneteasy.srvlog.data.LogData;
import com.payneteasy.srvlog.service.impl.Subscription;
import org.eclipse.jetty.websocket.api.Session;
import java.util.List;

public interface ILogBroadcastingService {

    List<String> getHostNameList();

    List<String> getProgramNameList();

    List<LogData> getLogDataListByHostAndProgram(String host, String program);

    List<LogData> getLogDataListByHostsAndPrograms(List<String> hosts, List<String> programs);

    void saveBroadcastingSession(Session session, Subscription subscription);

    void removeBroadcastingSession(Session session);

    void handleLogBroadcastingRequest(Session session, String requestText);

    void handleReceivedLogData(LogData logData);
}
