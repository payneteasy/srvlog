package com.payneteasy.srvlog.util;

import org.junit.Assert;
import org.junit.Test;

public class TruncationUtilTest {

    @Test
    public void testNullInput() {
        Assert.assertNull(TruncationUtil.truncateString(10, null));
    }

    @Test
    public void testAsciiWithinLimit() {
        String s = "hello"; // 5 bytes
        Assert.assertEquals("hello", TruncationUtil.truncateString(10, s));
        Assert.assertEquals("hello", TruncationUtil.truncateString(5, s));
    }

    @Test
    public void testAsciiExceedingLimit() {
        String s = "abcdefghij"; // 10 bytes
        Assert.assertEquals("abcd", TruncationUtil.truncateString(4, s));
        Assert.assertEquals("abcdef", TruncationUtil.truncateString(6, s));
    }

    @Test
    public void testTwoByteUtf8Characters() {
        String s = "Привет"; // Cyrillic, each char 2 bytes in UTF-8, total 12 bytes
        // 11 bytes limit cannot include 6th char completely -> 5 chars = 10 bytes
        Assert.assertEquals(s.substring(0,5), TruncationUtil.truncateString(11, s));
        // Exact boundary 12 bytes -> full string
        Assert.assertEquals(s, TruncationUtil.truncateString(12, s));
        // 1 byte -> cannot fit even first char -> empty string
        Assert.assertEquals("", TruncationUtil.truncateString(1, s));
    }

    @Test
    public void testThreeByteUtf8Characters() {
        String s = "你好世界"; // Chinese, each 3 bytes, total 12
        Assert.assertEquals("你", TruncationUtil.truncateString(3, s));
        Assert.assertEquals("你好", TruncationUtil.truncateString(6, s));
        Assert.assertEquals("你好世", TruncationUtil.truncateString(9, s));
        Assert.assertEquals("你好世界", TruncationUtil.truncateString(12, s));
        // 2 bytes limit -> cannot include any
        Assert.assertEquals("", TruncationUtil.truncateString(2, s));
    }

    @Test
    public void testFourByteEmoji() {
        String s = "A😀B"; // 'A'(1), '😀'(4), 'B'(1) => 6 bytes
        Assert.assertEquals("A😀B", TruncationUtil.truncateString(6, s));
        Assert.assertEquals("A😀", TruncationUtil.truncateString(5, s));
        Assert.assertEquals("A", TruncationUtil.truncateString(2, s));
        Assert.assertEquals("A", TruncationUtil.truncateString(3, s));
        // 1 byte -> only 'A'
        Assert.assertEquals("A", TruncationUtil.truncateString(1, s));
    }

    @Test
    public void testCombiningSequence() {
        String base = "e";
        String combining = "\u0301"; // combining acute accent
        String s = base + combining; // visually 'é' but 2 code points; bytes: 1 + 2 = 3
        Assert.assertEquals(s, TruncationUtil.truncateString(3, s));
        // If truncated at 2 bytes, only 'e' (1 byte) should remain; combining mark alone would be dropped by decoder
        Assert.assertEquals("e", TruncationUtil.truncateString(2, s));
    }

    @Test
    public void testLengthHeuristicEarlyReturn() {
        // The method returns early if input.length() <= maxBytes/4
        String s = "1234567890"; // length 10
        Assert.assertEquals(s, TruncationUtil.truncateString(40, s)); // 40/4 = 10 -> <= true
        // But also ensure that when heuristic fails yet bytes <= maxBytes it still returns input
        String ascii = "abcdef"; // 6 bytes
        Assert.assertEquals(ascii, TruncationUtil.truncateString(6, ascii));
    }
}
