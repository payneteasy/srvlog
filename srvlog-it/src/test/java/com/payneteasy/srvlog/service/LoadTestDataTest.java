package com.payneteasy.srvlog.service;

import com.payneteasy.srvlog.CommonIntegrationTest;
import com.payneteasy.srvlog.DatabaseUtil;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

/**
 * Date: 11.01.13
 */
public class LoadTestDataTest extends CommonIntegrationTest {


    @Override
    protected void createDatabase() throws IOException, InterruptedException {
        // do not touch database here
    }

    @Ignore
    @Test
    public void testLoadData() {
        ILogCollector collector = context.getBean(ILogCollector.class);
        DatabaseUtil.generateTestLogs(collector);
    }

}
