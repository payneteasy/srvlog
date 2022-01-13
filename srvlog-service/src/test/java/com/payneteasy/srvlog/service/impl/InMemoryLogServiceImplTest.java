package com.payneteasy.srvlog.service.impl;

import com.payneteasy.srvlog.data.LogData;
import com.payneteasy.srvlog.service.IInMemoryLogService;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class InMemoryLogServiceImplTest {

    private static final int PROGRAM_LOG_STORAGE_CAPACITY = 100;
    private static final int PROGRAM_LOG_LINES_PROVIDED_NUMBER = 120;

    private static final String PROGRAM_LOG_MESSAGE_TEMPLATE = "Test log message from program [%s] with id [%d]";

    @Test
    public void inMemoryLogServiceStorageTest() {

        IInMemoryLogService inMemoryLogService = new InMemoryLogServiceImpl(PROGRAM_LOG_STORAGE_CAPACITY);

        List<String> hostList = Collections.unmodifiableList(Arrays.asList("host1", "host2"));
        List<String> programList = Collections.unmodifiableList(Arrays.asList("program1", "program2"));

        for (String hostName : hostList) {
            for (String programName : programList) {
                for (int i = 0; i < PROGRAM_LOG_LINES_PROVIDED_NUMBER; i++) {

                    LogData logData = new LogData();
                    logData.setDate(new Date());
                    logData.setHost(hostName);
                    logData.setMessage(String.format(PROGRAM_LOG_MESSAGE_TEMPLATE, programName, i));
                    logData.setId((long) i);
                    logData.setProgram(programName);

                    inMemoryLogService.handleReceivedLogData(logData);
                }
            }
        }


    }

}