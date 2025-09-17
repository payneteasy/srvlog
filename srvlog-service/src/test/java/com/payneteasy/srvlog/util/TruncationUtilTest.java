package com.payneteasy.srvlog.util;

import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class TruncationUtilTest {

    @Test
    public void testNullInputReturnsNull() {
        Assert.assertNull(TruncationUtil.truncateString(StandardCharsets.UTF_8, 10, null));
    }

    @Test
    public void testAsciiBelowLimitReturnsSame() {
        String input = "hello";
        String out = TruncationUtil.truncateString(StandardCharsets.UTF_8, 10, input);
        Assert.assertEquals(input, out);
    }

    @Test
    public void testAsciiExactlyAtLimitReturnsSame() {
        String input = "1234567890"; // 10 bytes in UTF-8
        String out = TruncationUtil.truncateString(StandardCharsets.UTF_8, 10, input);
        Assert.assertEquals(input, out);
    }

    @Test
    public void testAsciiAboveLimitTruncatesByBytes() {
        String input = "abcdefghijK"; // 11 bytes
        String out = TruncationUtil.truncateString(StandardCharsets.UTF_8, 10, input);
        Assert.assertEquals("abcdefghij", out);
    }

    @Test
    public void testUtf8DoesNotBreakMultibyteChar() {
        // "Привет" in Russian; each Cyrillic char is 2 bytes in UTF-8
        String input = "Привет"; // 6 chars, 12 bytes in UTF-8
        // Choose maxBytes that cuts inside the 2nd char: first char 'П' is 2 bytes
        int maxBytes = 3; // 3 bytes will include full first char (2 bytes) and 1 byte of next char -> next char should be dropped
        String out = TruncationUtil.truncateString(StandardCharsets.UTF_8, maxBytes, input);
        Assert.assertEquals("П", out);

        // Another check: 5 bytes -> should decode first two full chars (4 bytes), drop incomplete third
        out = TruncationUtil.truncateString(StandardCharsets.UTF_8, 5, input);
        Assert.assertEquals("Пр", out);
    }

    @Test
    public void testEmojiSurrogatePairUtf8() {
        String emoji = "😀"; // U+1F600, 4 bytes in UTF-8
        String input = emoji + "X"; // 4 + 1 bytes

        // Cut inside emoji -> should return empty string because first code point incomplete
        String out = TruncationUtil.truncateString(StandardCharsets.UTF_8, 1, input);
        Assert.assertEquals("", out);

        out = TruncationUtil.truncateString(StandardCharsets.UTF_8, 3, input);
        Assert.assertEquals("", out);

        // Exactly full emoji
        out = TruncationUtil.truncateString(StandardCharsets.UTF_8, 4, input);
        Assert.assertEquals(emoji, out);

        // Emoji plus 'X' truncated to 5 returns emoji + X
        out = TruncationUtil.truncateString(StandardCharsets.UTF_8, 5, input);
        Assert.assertEquals(emoji + "X", out);
    }

    @Test
    public void testMaxBytesZero() {
        String out = TruncationUtil.truncateString(StandardCharsets.UTF_8, 0, "anything");
        Assert.assertEquals("", out);
    }

    @Test
    public void testIso88591SingleByteEncoding() {
        Charset iso = StandardCharsets.ISO_8859_1;
        String input = "Ångström"; // In ISO-8859-1, each char is single byte (Å is 0xC5)
        byte[] bytes = input.getBytes(iso);
        Assert.assertTrue(bytes.length > 4);
        String out = TruncationUtil.truncateString(iso, 4, input);
        // Should be first 4 bytes/characters
        Assert.assertEquals(new String(bytes, 0, 4, iso), out);
    }

    @Test
    public void testUtf16TruncationOnCodeUnitBoundaries() {
        // Note: UTF-16 uses 2 bytes per code unit; using BMP char 'Ж' (U+0416) and emoji
        String bmp = "Ж"; // 2 bytes in UTF-16BE/LE per code unit, but Java default Charset UTF_16 includes BOM; use specific
        Charset utf16be = StandardCharsets.UTF_16BE;
        String input = bmp + bmp + "A"; // 2+2+1 chars
        // bytes: bmp(2), bmp(2), 'A'(2) => total 6 bytes in UTF-16BE
        // Truncate to 3 bytes -> first code unit complete (2 bytes) + 1 byte of next => decoder should keep only first char
        String out = TruncationUtil.truncateString(utf16be, 3, input);
        Assert.assertEquals(bmp, out);

        // Truncate to 4 bytes -> two BMP chars
        out = TruncationUtil.truncateString(utf16be, 4, input);
        Assert.assertEquals(bmp + bmp, out);

        // Emoji surrogate pair in UTF-16BE is 4 bytes; cut inside surrogate pair should drop it
        String emoji = "😀"; // surrogate pair -> 4 bytes in UTF-16BE
        out = TruncationUtil.truncateString(utf16be, 1, emoji);
        Assert.assertEquals("", out);
        out = TruncationUtil.truncateString(utf16be, 3, emoji);
        Assert.assertEquals("", out);
        out = TruncationUtil.truncateString(utf16be, 4, emoji);
        Assert.assertEquals(emoji, out);
    }
}
