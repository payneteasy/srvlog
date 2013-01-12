package com.payneteasy.srvlog.data;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Date: 12.01.13
 * Time: 20:34
 */
public class LogFacilityTest {

    @Test
    public void testFindByValue () {

        for (LogFacility lf: LogFacility.values()) {
            assertEquals(lf.name(), LogFacility.forValue(lf.getValue()));
        }

    }
}
