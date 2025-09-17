package com.payneteasy.srvlog.util;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;

public class TruncationUtil {

    public static String truncateString(Charset charset, int maxBytes, String input) {

        if (input == null) {
            return null;
        }

        byte[] bytes = input.getBytes(charset);
        if (bytes.length <= maxBytes) {
            return input;
        }

        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes, 0, maxBytes);
        CharBuffer charBuffer = CharBuffer.allocate(maxBytes);

        CharsetDecoder decoder = charset.newDecoder()
                .onMalformedInput(CodingErrorAction.IGNORE)
                .onUnmappableCharacter(CodingErrorAction.IGNORE);

        decoder.decode(byteBuffer, charBuffer, true);
        decoder.flush(charBuffer);

        return new String(charBuffer.array(), 0, charBuffer.position());
    }

}
