package com.payneteasy.srvlog.service;

import com.payneteasy.srvlog.DatabaseUtil;
import com.payneteasy.srvlog.data.LogFacility;
import com.payneteasy.srvlog.data.LogLevel;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sphx.api.SphinxException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.*;


/**
 * Date: 09.01.13
 * Time: 1:36
 */
public class SphinxIndexerServiceIntegrationTest {

    private static final Logger LOG = LoggerFactory.getLogger(SphinxIndexerServiceIntegrationTest.class);

    private static Process sphinxDaemonProcess = null;

    private static ClassPathXmlApplicationContext context;

    private static IIndexerService indexerService;

    @BeforeClass
    public static void setUpAll() throws IOException, InterruptedException {
        DatabaseUtil.runCommandAndWaitUntilFinished(Arrays.asList("bash", "./create_database.sh"), null);
        context = new ClassPathXmlApplicationContext("classpath:spring/spring-test-datasource.xml","classpath:spring/spring-dao.xml", "classpath:spring/spring-service.xml");
        DatabaseUtil.generateTestLogs(context.getBean(ILogCollector.class));
        DatabaseUtil .runCommandAndWaitUntilFinished(Arrays.asList("bash", "./create_sphinx_conf.sh"),new File("sphinx"));
        File targetWorkingDir = new File("target");
        DatabaseUtil.runCommandAndWaitUntilFinished(Arrays.asList("indexer", "--config","test-sphinx.conf", "--all"), targetWorkingDir);
        sphinxDaemonProcess = DatabaseUtil.runCommand(Arrays.asList("searchd", "--config", "test-sphinx.conf"), targetWorkingDir);
        indexerService = context.getBean(IIndexerService.class);
        TimeUnit.SECONDS.sleep(3);
    }


    @Test
    public void testQueryByFacilities() throws SphinxException, IndexerServiceException {
        List<Long> idList = indexerService.search(null, null, Arrays.asList(LogFacility.kern.getValue()), null, null, null, 0, 30);
        assertEquals("20 logs should be found by the facility", 20, idList.size());
    }


    @Test
    public void testQueryByHosts() throws IndexerServiceException {
        List<Long> idList = indexerService.search(null, null, null, null, Arrays.asList(1), null, 0, 50);
        assertEquals("40 logs should be found by the host", 40, idList.size());
    }

    @Test
    public void testQueryBySeveritiesAndHosts() throws IndexerServiceException {
        List<Long> idList = indexerService.search(null, null, null, Arrays.asList(LogLevel.EMERGENCY.getValue()), Arrays.asList(1), null, 0, 30);
        assertEquals("10 logs should be found by the host and the severity", 10, idList.size());
    }

    @Test
    public void testQueryByProgram() throws IndexerServiceException {
        int offset = 30;
        List<Long> idList = indexerService.search(null, null, null, null, null, "program", 0, offset);
        assertEquals("30 logs should be found by the program", offset, idList.size());
    }

    @Test
    public void testQueryByDates() throws IndexerServiceException {
        Calendar c = Calendar.getInstance();
        c.set(2012, 0, 2, 0, 0, 0);
        Date from = c.getTime();
        c.roll(Calendar.DAY_OF_YEAR, 1);
        Date to = c.getTime();

        List<Long> idList = indexerService.search(from, to, null, null, null, null, null, null);

        assertEquals("8 logs should be found by date range specified", 8, idList.size());
    }

    @Test
    public void testQueryByPattern() throws IndexerServiceException {
        List<Long> idList = indexerService.search(null, null, null, null, null, "host", 0, 100);
        assertEquals(80, idList.size());

        idList = indexerService.search(null, null, null, null, null, "host1", 0, 100);
        assertEquals(40, idList.size());

        idList = indexerService.search(null, null, null, null, null, "@message \"host1\"", 0, 100);
        assertEquals("@message \"host1\" should return 40 elements", 40, idList.size());

    }

    @AfterClass
    public static void tearDown() {
        if (context !=null) {
            context.close();
        }
        if (sphinxDaemonProcess != null) {
            sphinxDaemonProcess.destroy();
        }
    }

}
