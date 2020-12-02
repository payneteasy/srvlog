package com.payneteasy.srvlog.adapter.json;

import com.payneteasy.srvlog.adapter.json.messages.SaveLogsRequest;
import com.payneteasy.srvlog.adapter.json.messages.SaveLogsResponse;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface ISaveLogsService {

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    SaveLogsResponse saveLogs(String aRemoteIpAddress, SaveLogsRequest aLogs);

}
