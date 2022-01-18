package com.payneteasy.srvlog.service.impl;

import com.payneteasy.srvlog.data.LogData;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InMemoryLogStorageTest {

    @Test
    public void inMemoryLogStorageTest() {

        InMemoryLogStorage inMemoryLogStorage = new InMemoryLogStorage(2);

        LogData logData1 = new LogData();
        logData1.setId(1L);
        logData1.setDate(new Date());
        inMemoryLogStorage.add(logData1);

        LogData logData2 = new LogData();
        logData2.setId(2L);
        logData2.setDate(new Date());
        inMemoryLogStorage.add(logData2);

        LogData logData3 = new LogData();
        logData3.setId(3L);
        logData3.setDate(new Date());
        inMemoryLogStorage.add(logData3);

        assertEquals(1, inMemoryLogStorage.getPointerValue());

        List<LogData> logDataList = inMemoryLogStorage.asLogList();

        assertEquals(2, logDataList.size());
        assertTrue(logDataList.contains(logData2));
        assertTrue(logDataList.contains(logData3));
        assertFalse(logDataList.contains(logData1));
    }
}