package com.payneteasy.srvlog.util;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;

public class TruncationUtil {

    private static final Charset CHARSET = StandardCharsets.UTF_8;
    private static final int MAX_BYTES_PER_SYMBOL = 4;

    public static String truncateString(int maxBytes, String input) {

        if (input == null || input.isEmpty()) {
            return null;
        }

        if (input.length() <= maxBytes / MAX_BYTES_PER_SYMBOL) {
            return input;
        }

        byte[] bytes = input.getBytes(CHARSET);
        if (bytes.length <= maxBytes) {
            return input;
        }

        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes, 0, maxBytes);
        CharBuffer charBuffer = CharBuffer.allocate(maxBytes);

        CharsetDecoder decoder = CHARSET.newDecoder()
                .onMalformedInput(CodingErrorAction.IGNORE)
                .onUnmappableCharacter(CodingErrorAction.IGNORE);

        decoder.decode(byteBuffer, charBuffer, true);
        decoder.flush(charBuffer);

        return new String(charBuffer.array(), 0, charBuffer.position());
    }

}
