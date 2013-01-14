package com.payneteasy.srvlog.service;

import com.payneteasy.srvlog.CommonIntegrationTest;
import com.payneteasy.srvlog.DatabaseUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sphx.api.SphinxClient;
import org.sphx.api.SphinxException;
import org.sphx.api.SphinxMatch;
import org.sphx.api.SphinxResult;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Date: 09.01.13
 * Time: 1:36
 */
public class SphinxBaseIntegrationTest extends CommonIntegrationTest {

    private static final Logger LOG = LoggerFactory.getLogger(SphinxBaseIntegrationTest.class);

    Process sphinxDaemonProcess = null;

    @Before
    public void setUp() throws IOException, InterruptedException {
        super.setUp();
        //DatabaseUtil.runCommandAndWaitUntilFinished(Arrays.asList("bash", "./create_database.sh"), null);
        DatabaseUtil.generateTestLogs(context.getBean(ILogCollector.class));
        DatabaseUtil .runCommandAndWaitUntilFinished(Arrays.asList("bash", "./create_sphinx_conf.sh"),new File("sphinx"));
        File targetWorkingDir = new File("target");
        DatabaseUtil.runCommandAndWaitUntilFinished(Arrays.asList("indexer", "--config","test-sphinx.conf", "--all"), targetWorkingDir);
        sphinxDaemonProcess = DatabaseUtil.runCommand(Arrays.asList("searchd", "--config", "test-sphinx.conf"), targetWorkingDir);
        TimeUnit.SECONDS.sleep(3);
    }


    @Test
    public void testQuerySearchd() throws SphinxException {
        /**
         * Sphinx Daemon needs to be started and index prepared before running the tests
         */

        SphinxClient client = new SphinxClient();
        client.SetMatchMode(SphinxClient.SPH_MATCH_EXTENDED);
        client.SetSelect("log_date, facility, severity, host_id");
        client.SetFilter("host_id", 1, false);
        client.SetLimits(20, 20);
        client.SetSortMode(SphinxClient.SPH_SORT_ATTR_DESC, "log_date");
        SphinxResult result = client.Query("", "srvlog_index");

        System.out.println(result.matches.length + " results found.");
        for (SphinxMatch sm : result.matches) {
            System.out.println(sm.docId);
            System.out.println(sm.attrValues);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(((Long) sm.attrValues.get(0)) * 1000);


            System.out.println(calendar.getTime());
        }

        //SphinxClient client = new SphinxClient();
    }

    @After
    public void tearDown() {
        if (sphinxDaemonProcess != null) {
            sphinxDaemonProcess.destroy();
        }
    }

}
