package com.payneteasy.srvlog.util;

import org.junit.Assert;
import org.junit.Test;

public class ComparisonUtilsTest {
    @Test
    public void testHostAddressesAreEqual() {
        Assert.assertTrue(ComparisonUtils.hostAddressesAreEqual(null, null));
        Assert.assertFalse(ComparisonUtils.hostAddressesAreEqual(null, "host"));
        Assert.assertFalse(ComparisonUtils.hostAddressesAreEqual("host", null));
        Assert.assertTrue(ComparisonUtils.hostAddressesAreEqual("host", "host"));
        Assert.assertTrue(ComparisonUtils.hostAddressesAreEqual("127.0.0.1", "localhost"));
        Assert.assertTrue(ComparisonUtils.hostAddressesAreEqual("localhost", "127.0.0.1"));
        Assert.assertFalse(ComparisonUtils.hostAddressesAreEqual("host", "another-host"));
    }
}