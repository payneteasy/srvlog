package com.payneteasy.srvlog.service;

import com.payneteasy.srvlog.DatabaseUtil;
import com.payneteasy.srvlog.data.LogFacility;
import com.payneteasy.srvlog.data.LogLevel;
import com.payneteasy.srvlog.service.impl.SphinxIndexerService;
import com.payneteasy.srvlog.util.DateRange;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sphx.api.SphinxException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.io.IOException;
import java.util.*;
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
    public void testQueryByPatternWithStarr() throws Exception {
        List<Long> idList = indexerService.search(null, null, null, null, null, "mes*", 0, 30);

        assertEquals("30 logs should be found by pattern", 30, idList.size());
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

        idList = indexerService.search(null, null, null, null, null, " @program \"host1program\" ", 0, 100);
        assertEquals("'@program \"host1program\"' should return 40 elements", 40, idList.size());

    }

    @Test
    public void testQueryLogsCountAndGroupResultsDaily() throws IndexerServiceException {
        Calendar c = Calendar.getInstance();
        c.set(2012, 0, 2, 0, 0, 0);
        Date from = c.getTime();

        c.roll(Calendar.MONTH, 1);
        Date to = c.getTime();

        Map<Date, Long> results = indexerService.numberOfLogsByDate(from, to);
        assertEquals("Should be 8 groups of date bucket in test data", 10, results.size());

        c.setTime(from);
        c.set(Calendar.MILLISECOND, 0);

        assertNotNull(results.get(c.getTime())); //check the first date

    }

    @Test
    public void testQueryLogsCountGrouppedBySeverity() throws IndexerServiceException {
        Calendar c = Calendar.getInstance();
        c.set(2012, 0, 2, 0, 0, 0);
        Date from = c.getTime();

        c.roll(Calendar.MONTH, 1);
        Date to = c.getTime();

        Map<LogLevel, Long> results = indexerService.numberOfLogsBySeverity(from, to);
        assertEquals("Should be 8 groups of severity buckets", 8, results.size());
    }

    public void testForZeroQueryLogsCountGrouppedBySeverity() throws IndexerServiceException {
        Calendar c = Calendar.getInstance();
        c.set(2000, 0, 2, 0, 0, 0);
        Date from = c.getTime();

        c.roll(Calendar.MONTH, 1);
        Date to = c.getTime();

        Map<LogLevel, Long> results = indexerService.numberOfLogsBySeverity(from, to);
        assertEquals("Should be 8 groups of severity buckets", 8, results.size());

        List<Map.Entry<LogLevel, Long>> list = new ArrayList<Map.Entry<LogLevel, Long>>(results.entrySet());
        assertEquals("Should be 8 groups of severity buckets", 8, list.size());
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

    public static void main(String[] args) throws SphinxException, IndexerServiceException {
        SphinxIndexerService service = new SphinxIndexerService();
        service.setHost("93.188.253.57");
        service.setPort(9312);
        service.setConnectTimeout(30000);

        DateRange thisMonth = DateRange.thisMonth();
        Map<Date, Long> dateLongMap = service.numberOfLogsByDate(thisMonth.getFromDate(), thisMonth.getToDate(), 0, 30);

        System.out.println(dateLongMap.size());


        /*SphinxClient client = new SphinxClient("93.188.253.57", 9312);
        client.SetConnectTimeout(1000000);

        client.SetMatchMode(SphinxClient.SPH_MATCH_EXTENDED2);
        client.SetLimits(0, 100);
        //client.SetSelect("@count as mycount, @group as mygroup");
        //DateRange lastMonth = DateRange.lastMonth();
        //client.SetFilterRange("log_date", (lastMonth.getFromDate().getTime()/1000), lastMonth.getToDate().getTime()/1000);
        client.SetGroupBy("log_date", SphinxClient.SPH_GROUPBY_DAY);
        SphinxResult result = client.Query("");
        System.out.println(result.warning);
        System.out.println(result.error );

        for (SphinxMatch sm: result.matches) {
            System.out.println();
            int i = 0;
            for(Object o: sm.attrValues) {
                if ("log_date".equalsIgnoreCase(result.attrNames[i])) {
                    System.out.print(result.attrNames[i] + "("  + result.attrTypes[i] + ")"+ "=" + new Date(((Long)o) * 1000) + ", " + o.getClass().getName());
                } else {
                    System.out.print(result.attrNames[i] + "("  + result.attrTypes[i] + ")"+ "=" + o + ", " + o.getClass().getName());
                }
                i++;
            }

            //System.out.println("count = " + sm.attrValues.get(0)+ ",  group = " + sm.attrValues.get(1));

        }

        System.out.println("\n " + result.matches.length + " results found");*/
    }

}
