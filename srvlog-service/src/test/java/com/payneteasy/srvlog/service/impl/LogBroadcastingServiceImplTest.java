package com.payneteasy.srvlog.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.payneteasy.srvlog.data.LogData;
import com.payneteasy.srvlog.service.ILogBroadcastingService;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.websocket.Session;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class LogBroadcastingServiceImplTest {

    private static final int TEST_PROGRAM_LOG_STORAGE_CAPACITY = 10;

    private static final List<String> TEST_HOST_LIST = Arrays.asList("host-1", "host-2");
    private static final List<String> TEST_PROGRAM_LIST = Arrays.asList("program-1", "program-2");

    private static final int TEST_LOG_NUMBER_PER_PROGRAM = 14;

    private static final ObjectMapper jsonMapper = new ObjectMapper();

    private static ILogBroadcastingService logBroadcastingService;

    @BeforeClass
    public static void createLogBroadcastingServiceImpl() {

        logBroadcastingService = new LogBroadcastingServiceImpl(TEST_PROGRAM_LOG_STORAGE_CAPACITY);

        for (LogData logData : generateTestLogDataList()) {
            logBroadcastingService.handleReceivedLogData(logData);
        }
    }

    private static List<LogData> generateTestLogDataList() {

        List<LogData> result = new ArrayList<>();

        for (String hostName : TEST_HOST_LIST) {
            for (String programName : TEST_PROGRAM_LIST) {
                for (int i = 0; i < TEST_LOG_NUMBER_PER_PROGRAM; i++) {

                    LogData logData = new LogData();

                    logData.setDate(new Date(System.currentTimeMillis() + i));
                    logData.setHost(hostName);
                    logData.setId((long) i);
                    logData.setProgram(programName);

                    result.add(logData);
                }
            }
        }

        return result;
    }

    @Test
    public void getHostNameListTest() {

        List<String> hostNameList = logBroadcastingService.getHostNameList();
        assertEquals(TEST_HOST_LIST.size(), hostNameList.size());

        for (String testHostName : TEST_HOST_LIST) {
            assertTrue(hostNameList.contains(testHostName));
        }
    }

    @Test
    public void getProgramNameListTest() {

        List<String> programNameList = logBroadcastingService.getProgramNameList();
        assertEquals(TEST_PROGRAM_LIST.size(), programNameList.size());

        for (String testProgramName : TEST_PROGRAM_LIST) {
            assertTrue(programNameList.contains(testProgramName));
        }
    }

    @Test
    public void getLogDataListByHostAndProgramTest() {

        for (String hostName : TEST_HOST_LIST) {
            for (String programName : TEST_PROGRAM_LIST) {
                List<LogData> logDataList = logBroadcastingService.getLogDataListByHostAndProgram(hostName, programName);
                validateHostProgramLogDataList(hostName, programName, logDataList);
            }
        }
    }

    private void validateHostProgramLogDataList(String hostName, String programName, List<LogData> logDataList) {

        assertEquals(TEST_PROGRAM_LOG_STORAGE_CAPACITY, logDataList.size());

        logDataList.sort(Comparator.comparingLong(LogData::getId));

        Long firstLogDataId = logDataList.get(0).getId();
        Long expectedFirstLogDataId = (long) (TEST_LOG_NUMBER_PER_PROGRAM % TEST_PROGRAM_LOG_STORAGE_CAPACITY);

        assertEquals(expectedFirstLogDataId, firstLogDataId);

        Long lastLogDataId = logDataList.get(logDataList.size() - 1).getId();
        Long expectedLastLogDataId = (long) TEST_LOG_NUMBER_PER_PROGRAM - 1;

        assertEquals(expectedLastLogDataId, lastLogDataId);

        for (LogData logData : logDataList) {
            assertEquals(hostName, logData.getHost());
            assertEquals(programName, logData.getProgram());
        }
    }

    @Test
    public void logBroadcastingTest() throws Exception {

        MockWsSession wsSession1 = emulateWebSocketOpenConnectionRequest();

        String subscriptionHost1 = TEST_HOST_LIST.get(0);
        String subscriptionProgram1 = TEST_PROGRAM_LIST.get(0);

        LogBroadcastingResponse logBroadcastingResponse1 =
                emulateWebSocketLogBroadcastingSubscriptionRequest(wsSession1, subscriptionHost1, subscriptionProgram1);

        assertTrue(logBroadcastingResponse1.isSuccess());
        assertNull(logBroadcastingResponse1.getErrorMessage());

        List<LogData> logDataList1 = logBroadcastingResponse1.getLogDataList();

        validateHostProgramLogDataList(subscriptionHost1, subscriptionProgram1, logDataList1);

        MockWsSession wsSession2 = emulateWebSocketOpenConnectionRequest();

        String subscriptionHost2 = TEST_HOST_LIST.get(1);
        String subscriptionProgram2 = TEST_PROGRAM_LIST.get(1);

        LogBroadcastingResponse logBroadcastingResponse2 =
                emulateWebSocketLogBroadcastingSubscriptionRequest(wsSession2, subscriptionHost2, subscriptionProgram2);

        assertTrue(logBroadcastingResponse2.isSuccess());
        assertNull(logBroadcastingResponse2.getErrorMessage());

        List<LogData> logDataList2 = logBroadcastingResponse2.getLogDataList();

        validateHostProgramLogDataList(subscriptionHost2, subscriptionProgram2, logDataList2);

        MockWsSession wsSession3 = emulateWebSocketOpenConnectionRequest();

        LogBroadcastingResponse logBroadcastingResponse3 =
                emulateWebSocketLogBroadcastingSubscriptionRequest(wsSession3, null, null);

        assertFalse(logBroadcastingResponse3.isSuccess());
        assertEquals("Incorrect request parameters", logBroadcastingResponse3.getErrorMessage());

        LogData newLogData = new LogData();

        long newLogDataId = 123;

        newLogData.setDate(new Date());
        newLogData.setHost(subscriptionHost2);
        newLogData.setId(newLogDataId);
        newLogData.setProgram(subscriptionProgram2);

        logBroadcastingService.handleReceivedLogData(newLogData);

        LogBroadcastingResponse newLogBroadcastingResponse = wsSession2.getLogBroadcastingResponse();

        assertTrue(newLogBroadcastingResponse.isSuccess());
        assertNull(newLogBroadcastingResponse.getErrorMessage());

        List<LogData> newLogDataList = newLogBroadcastingResponse.getLogDataList();

        assertEquals(1, newLogDataList.size());

        LogData logData = newLogDataList.get(0);

        assertEquals(subscriptionHost2, logData.getHost());
        assertEquals(subscriptionProgram2, logData.getProgram());
        assertEquals(newLogDataId, (long) logData.getId());
    }

    private MockWsSession emulateWebSocketOpenConnectionRequest() {

        MockWsSession testWsSession = new MockWsSession();
        logBroadcastingService.saveBroadcastingSession(testWsSession, Subscription.initialState());

        return testWsSession;
    }

    private LogBroadcastingResponse emulateWebSocketLogBroadcastingSubscriptionRequest(
            Session session, String subscriptionHost, String subscriptionProgram) throws JsonProcessingException {

        LogBroadcastingRequest logBroadcastingRequest =
                new LogBroadcastingRequest(
                    subscriptionHost, subscriptionProgram, Subscription.State.INITIAL.name()
                );

        String requestText = jsonMapper.writeValueAsString(logBroadcastingRequest);

        logBroadcastingService.handleLogBroadcastingRequest(session, requestText);

        return ((MockWsSession) session).getLogBroadcastingResponse();
    }
}