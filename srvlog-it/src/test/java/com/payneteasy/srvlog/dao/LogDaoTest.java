package com.payneteasy.srvlog.dao;

import com.payneteasy.srvlog.CommonIntegrationTest;
import static com.payneteasy.srvlog.adapter.syslog.SnortMessage.createSnortMessage;
import com.payneteasy.srvlog.data.UnprocessedSnortLogData;
import com.payneteasy.srvlog.data.HostData;
import com.payneteasy.srvlog.data.LogData;
import com.payneteasy.srvlog.data.SnortLogData;
import com.payneteasy.srvlog.util.DateRange;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.joda.time.DateTime;

import static org.junit.Assert.*;

/**
 * Date: 04.01.13
 */
public class LogDaoTest extends CommonIntegrationTest {

    private ILogDao logDao;

    @Before
    public void setUp() throws IOException, InterruptedException {
        super.setUp();
        logDao = context.getBean(ILogDao.class);
    }

    @Test
    public void testSaveHost() {
        addLocalhostToHostList();

        List<HostData> hostDataList = logDao.loadHosts();
        assertTrue(hostDataList.size() > 0);
    }

    @Test
    public void testSaveLogWithUnknownHost() {
        LogData logData = new LogData();
        logData.setSeverity(1);
        logData.setDate(new Date());
        logData.setFacility(1);
        logData.setMessage("message");
        logData.setHost("unknown.host");
        logData.setProgram("program");

        logDao.saveLog(logData);

        LogData persistedLogData = logDao.load(logData.getId());
        assertNull("Should be save in unprocessed_logs table", persistedLogData);

        // check presence in the unprocessed_logs table
        LogData unprocessedLogData = logDao.finInUnporocessed(logData.getId());
        assertNotNull("Should be present in unprocessed_logs table", unprocessedLogData);
    }

    @Test
    public void testSaveLog(){
        addLocalhostToHostList();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MILLISECOND,0);
        Date date = calendar.getTime();

        LogData logData = saveTestLog(1, date);

        assertNotNull("ID must be assigned to logData entity", logData.getId());

        LogData persistedLogData = logDao.load(logData.getId());
        assertEquals("Persisted and original objects have to be equal", logData, persistedLogData);
    }

    private void addLocalhostToHostList() {
        HostData hostData = new HostData();
        hostData.setHostname("localhost");
        hostData.setIpAddress("127.0.0.1");
        logDao.saveHost(hostData);
    }

    @Test
    public void testLoadLatest() {
        addLocalhostToHostList();

        DateRange dateRange = DateRange.lastMonth();

        Calendar start = Calendar.getInstance();
        start.setTime(dateRange.getFromDate());
        Calendar end = Calendar.getInstance();
        end.setTime(dateRange.getToDate());
        end.add(Calendar.DATE, -3);

        for (; !start.after(end); start.add(Calendar.DATE, 1)) {
            Date current = start.getTime();
            saveTestLog(1, current);
        }

        List<LogData> logDataList = logDao.loadLatest(5, null);

        assertEquals("loadLatest should return the numOfLogs log entries.", 5, logDataList.size());

        logDataList = logDao.loadLatest(7, null);

        assertEquals("loadLatest should return the numOfLogs log entries.", 7, logDataList.size());
    }

    @Test
    public void testGetLogsDataByIds(){
        addLocalhostToHostList();

        for (int i = 0; i < 11; i++) {
            Date date = new Date();
            saveTestLog(i, date);
        }

        List<LogData> logDataList = logDao.getLogsByIds("1,2,3,4,5");
        assertEquals("getLogsByIds should return 5 log entries.", 5, logDataList.size());

    }

    private LogData saveTestLog(int i, Date date) {
        LogData logData = new LogData();
        logData.setDate(date);
        logData.setFacility(1);
        logData.setSeverity(1);
        logData.setHost("localhost");
        logData.setMessage("Log message " + i);
        logDao.saveLog(logData);
        return logData;
    }

    @Test
    public void testGetUnprocessedLogs() {
        saveTestLog(1, new Date());

        List<LogData> logDataList = logDao.loadUnprocessed(1);

        assertTrue("Should return one unprocessed log", logDataList.size() == 1);
    }

    @Test
    public void testSaveUnprocessedLogs() {
        saveTestLog(1, new Date());

        List<LogData> unprocessedLogs = logDao.loadUnprocessed(1);

        assertTrue("Should return one unprocessed log", unprocessedLogs.size() == 1);

        addLocalhostToHostList();

        logDao.saveUnprocessedLogs();

        unprocessedLogs = logDao.loadUnprocessed(1);

        assertTrue("Should return zero unprocessed logs when saveUnprocessedLogs has been invoked", unprocessedLogs.size() == 0);


        List<LogData> processedLogs = logDao.loadLatest(1, null);

        assertTrue("Should returne one processed logs when saveUnprocessedLogs has been invoked", processedLogs.size() == 1);


    }

    @Test
    public void testSaveAndGetSnortLog() {
        String message =
            "| [SNORTIDS[LOG]: [snortIds1-eth1] ] |" +
            "| 2014-09-11 16:23:01.479 1 [1:1310:5] Snort Alert [1:1310:5] |" +
            "| policy-violation |" +
            "| 6 192.168.1.137 173.194.39.114 4 20 0 1113 40840 2 0 65456 0 |" +
            "| 55536 80 3885499888 1996581709 8 0 24 229 41947 0 |" +
            "| 1127 10BF48C888A0AC220BC9529B0800450004599F8840004006FFB0C0A80189ADC22772D8F00050E79805F077016B4D8" +
            "01800E5A3DB00000101080A02779A7599D090E8474554202F7365617263683F713D66657469736820485454502F312E310D0A" +
            "557365722D4167656E743A204F706572612F392E383020285831313B204C696E7578207838365F3634292050726573746F2F322" +
            "E31322E3338382056657273696F6E2F31322E31360D0A486F73743A207777772E676F6F676C652E636F6D0D0A4163636570743" +
            "A20746578742F68746D6C2C206170706C69636174696F6E2F786D6C3B713D302E392C206170706C69636174696F6E2F7868746" +
            "D6C2B786D6C2C20696D6167652F706E672C20696D6167652F776562702C20696D6167652F6A7065672C20696D6167652F67696" +
            "62C20696D6167652F782D786269746D61702C202A2F2A3B713D302E310D0A4163636570742D4C616E67756167653A2072752D5" +
            "2552C72753B713D302E392C656E3B713D302E380D0A4163636570742D456E636F64696E673A20677A69702C206465666C61746" +
            "50D0A436F6F6B69653A20485349443D41624A475573673434413867617042585F3B204150495349443D4C615F5A5264614B626" +
            "B34356A696B742F414C366B4A664C534A51765378356B464E3B204E49443D36373D726C76562D4B6A7644586B6E6F4E6736455" +
            "F696F544969766C70716E467734617752314B705857674D497767477367612D6E30437365716C4156395144554F7A66752D623" +
            "7733058694C6738586E3147647649486A613447324A7641765F6A4C6353597950387971683641363373613657364D313178796" +
            "A373341677948392D6C3657556B64553433743244474648597A336C6632386F5749764D546A74754C736C38435974416F6D563" +
            "7766B6C6F51563161706371625A6C647448766E72676276676A494847555572444C58657730313457453B205349443D4451414" +
            "1414C4141414143765755456B4D3142686C6E692D63624E57516C31704C757561537042563079495148544158646F443154666" +
            "27173325A3641676330547A5A48385F6D3857797A77574A42555055704D4E486B2D6758786D5658787A664D7A42454A7338497" +
            "6756B655442777036644131684877374B456D6B614F6647326C4F4C7A713041596D36676F5339375F485F484A3266613671313" +
            "04D43584533736F3734672D347853476C33637631317458626A50395836726B6E78344F4A496575706E68694A56656C5361566" +
            "750373447615F61545A375F5A5F50614A6D552D485578746B66333573465A4276744F34465A513B204F4750433D34303631313" +
            "5352D32333A3B204F47503D2D343036313135353A3B20505245463D49443D616639656538316235343936363363613A553D333" +
            "665613666653664353032666631663A46463D303A4C443D72753A4E523D31303A544D3D313430353430383935303A4C4D3D313" +
            "431303434313737373A53473D333A533D6457513855344B6F69352D59766E366F0D0A436F6E6E656374696F6E3A204B6565702D416C6976650D0A0D0A|";

        SnortLogData snortLogData = createSnortMessage(message).toSnortLogData();
        snortLogData.setHash("hash");

        logDao.saveSnortLog(snortLogData);

        assertNotNull(snortLogData.getId());

        List<SnortLogData> snortLogDatas = logDao.getSnortLogsByHash("hash");

        assertFalse(snortLogDatas.isEmpty());
    }

    @Test
    public void testSaveSnortUnprossedLog() {
        UnprocessedSnortLogData unprocessedSnortLogData = new UnprocessedSnortLogData();

        unprocessedSnortLogData.setDate(new Date());
        unprocessedSnortLogData.setIdentifier("identifier");
        unprocessedSnortLogData.setMessage("message");

        logDao.saveUnprocessedSnortLog(unprocessedSnortLogData);

        assertNotNull(unprocessedSnortLogData.getId());
    }

    @Test
    public void testGetSnortUnprocessedLogLimited() {
        logDao.deleteAllUnprocessedSnortLogs();

        for (int i = 0; i < 15; i++) {
            createUnprocessedSnortLog("identifier", new Date());
        }

        Date dateTo = new Date();
        Date dateFrom = new DateTime(dateTo).minusMinutes(5).toDate();

        List<UnprocessedSnortLogData> unprocessedSnortLogs =
            logDao.getUnprocessedSnortLogs("identifier", dateFrom, dateTo);

        assertFalse(unprocessedSnortLogs.isEmpty());
        assertEquals(10, unprocessedSnortLogs.size());
    }

    @Test
    public void testGetSnortUnprocessedRespectsIdentifier() {
        logDao.deleteAllUnprocessedSnortLogs();

        createUnprocessedSnortLog("identifier_1", new Date());
        createUnprocessedSnortLog("identifier_2", new Date());

        Date dateTo = new Date();
        Date dateFrom = new DateTime(dateTo).minusMinutes(5).toDate();

        List<UnprocessedSnortLogData> unprocessedSnortLogs =
            logDao.getUnprocessedSnortLogs("identifier_1", dateFrom, dateTo);

        assertFalse(unprocessedSnortLogs.isEmpty());
        assertEquals(1, unprocessedSnortLogs.size());
    }

    @Test
    public void testGetSnortUnprocessedRespectsDataBounds() {
        logDao.deleteAllUnprocessedSnortLogs();

        Date dateTo = new Date();
        Date dateFrom = new DateTime(dateTo).minusMinutes(5).toDate();
        Date dateOlderThatBounds = new DateTime(dateTo).minusMinutes(15).toDate();

        for (int i = 0; i < 5; i++) {
            createUnprocessedSnortLog("identifier", dateTo);
        }

        for (int i = 0; i < 5; i++) {
            createUnprocessedSnortLog("identifier", dateOlderThatBounds);
        }

        List<UnprocessedSnortLogData> unprocessedSnortLogs =
            logDao.getUnprocessedSnortLogs("identifier", dateFrom, dateTo);

        assertFalse(unprocessedSnortLogs.isEmpty());
        assertEquals(5, unprocessedSnortLogs.size());
    }

    private void createUnprocessedSnortLog(String identifier, Date date) {
        UnprocessedSnortLogData unprocessedSnortLogData = new UnprocessedSnortLogData();

        unprocessedSnortLogData.setDate(date);
        unprocessedSnortLogData.setIdentifier(identifier);
        unprocessedSnortLogData.setMessage("message");

        logDao.saveUnprocessedSnortLog(unprocessedSnortLogData);
    }

    public static void main(String[] args) {
        DateRange dateRange = DateRange.thisMonth();

        Calendar start = Calendar.getInstance();
        start.setTime(dateRange.getFromDate());
        Calendar end = Calendar.getInstance();
        end.setTime(dateRange.getToDate());

        for (; !start.after(end); start.add(Calendar.DATE, 1)) {
            Date current = start.getTime();
            System.out.println(current);
        }
    }

}
