package com.payneteasy.srvlog.service.impl;

import com.payneteasy.srvlog.data.LogData;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class InMemoryLogStorageTest {

    @Test
    public void inMemoryLogStorageSimpleTest() {

        InMemoryLogStorage inMemoryLogStorage = new InMemoryLogStorage(10);

        for (int i = 0; i < 30; i++) {
            LogData logData = new LogData();
            logData.setId((long) i);
            inMemoryLogStorage.add(logData);
        }

        assertEquals(20L, (long) inMemoryLogStorage.getFirst().getId());
        assertEquals(29L, (long) inMemoryLogStorage.getLast().getId());
        assertEquals(10, inMemoryLogStorage.size());
    }
}