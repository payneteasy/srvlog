package com.payneteasy.srvlog.logsender;

import com.payneteasy.srvlog.adapter.json.messages.SaveLogsMessage;
import com.payneteasy.srvlog.adapter.json.messages.SaveLogsRequest;
import com.payneteasy.srvlog.adapter.json.messages.SaveLogsResponse;
import com.payneteasy.srvlog.ui.CommonUiIntegrationTest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.UUID;

import static com.payneteasy.srvlog.adapter.json.messages.SaveLogsStatus.SUCCESS;
import static com.payneteasy.srvlog.data.LogFacility.local0;
import static com.payneteasy.srvlog.data.LogLevel.DEBUG;
import static java.lang.System.currentTimeMillis;

public class LogSenderWebTest extends CommonUiIntegrationTest {


    @Test
    public void sendLog() {
        SaveLogsClient   client   = new SaveLogsClient("http://localhost:8080");
        SaveLogsRequest  request  = createSampleRequest();
        SaveLogsResponse response = client.saveLogs("127.0.0.1", request);
        Assert.assertEquals(request.getRequestId(), response.getRequestId());
        Assert.assertEquals(SUCCESS, response.getStatus());
    }

    private SaveLogsRequest createSampleRequest() {
        ArrayList<SaveLogsMessage> messages = new ArrayList<>();
        messages.add(new SaveLogsMessage(currentTimeMillis(), "test", local0.getValue(), DEBUG.getValue(), "message 1"));
        messages.add(new SaveLogsMessage(currentTimeMillis(), "test", local0.getValue(), DEBUG.getValue(), "message 2"));
        return new SaveLogsRequest(UUID.randomUUID().toString(), messages);
    }


    @Override
    @After
    public void tearDown() {
        super.tearDown();
    }

}
