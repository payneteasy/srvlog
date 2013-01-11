package com.payneteasy.srvlog.service;

import com.payneteasy.srvlog.CommonIntegrationTest;
import com.payneteasy.srvlog.DatabaseUtil;
import org.junit.Test;

/**
 * Date: 11.01.13
 */
public class LoadTestDataTest extends CommonIntegrationTest {

    @Test
    public void testLoadData() {
        ILogCollector collector = context.getBean(ILogCollector.class);
        DatabaseUtil.generateTestLogs(collector);
    }

}
