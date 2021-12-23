package com.payneteasy.srvlog.service.impl;

import com.payneteasy.srvlog.data.LogData;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class InMemoryLogListTest {

    @Test
    public void inMemoryLogListSimpleTest() {

        InMemoryLogStorage inMemoryLogList = new InMemoryLogStorage(10);

        for (int i = 0; i < 30; i++) {
            LogData logData = new LogData();
            logData.setId((long) i);
            inMemoryLogList.add(logData);
        }

        assertEquals(20L, (long) inMemoryLogList.getFirst().getId());
        assertEquals(29L, (long) inMemoryLogList.getLast().getId());
        assertEquals(10, inMemoryLogList.size());
    }
}