package com.payneteasy.srvlog.service.impl;

import com.payneteasy.srvlog.data.LogData;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class InMemoryLogStorageTest {

    private static final int TEST_LOG_STORAGE_BUFFER_SIZE = 100;

    private static final int TEST_LOG_QUANTITY =345;

    private static final int BUFFER_POINTER_INITIAL_VALUE = 0;

    @Test
    public void inMemoryLogStorageTest() {

        InMemoryLogStorage inMemoryLogStorage = new InMemoryLogStorage(TEST_LOG_STORAGE_BUFFER_SIZE);

        assertEquals(BUFFER_POINTER_INITIAL_VALUE, inMemoryLogStorage.getPointerValue());

        for (int i = 0; i < TEST_LOG_QUANTITY; i++) {

            LogData logData = new LogData();
            logData.setId((long) i);
            logData.setDate(new Date(System.currentTimeMillis() + i));

            inMemoryLogStorage.add(logData);
        }

        int fullBufferCirclesNumber = TEST_LOG_QUANTITY / TEST_LOG_STORAGE_BUFFER_SIZE;
        int currentCircleLogsNumber = TEST_LOG_QUANTITY % TEST_LOG_STORAGE_BUFFER_SIZE;

        assertEquals(currentCircleLogsNumber, inMemoryLogStorage.getPointerValue());

        LogData[] logDataBuffer = inMemoryLogStorage.getLogDataBuffer();

        for (int i = 0; i < currentCircleLogsNumber; i++) {
            LogData logData = logDataBuffer[i];
            assertEquals(Long.valueOf(TEST_LOG_STORAGE_BUFFER_SIZE * fullBufferCirclesNumber + i), logData.getId());
        }

        for (int i = currentCircleLogsNumber; i < TEST_LOG_STORAGE_BUFFER_SIZE; i++) {
            LogData logData = logDataBuffer[i];
            assertEquals(
                    Long.valueOf(TEST_LOG_STORAGE_BUFFER_SIZE * fullBufferCirclesNumber + i - TEST_LOG_STORAGE_BUFFER_SIZE),
                    logData.getId()
            );
        }
    }
}