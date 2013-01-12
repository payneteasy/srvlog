package com.payneteasy.srvlog.data;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Date: 12.01.13
 * Time: 20:15
 */
public class LogLevelTest {

    @Test
    public void testFindByValue() {

        for (LogLevel ll: LogLevel.values()) {
            assertEquals(ll.name(), LogLevel.forValue(ll.getValue()));
        }

    }

}
