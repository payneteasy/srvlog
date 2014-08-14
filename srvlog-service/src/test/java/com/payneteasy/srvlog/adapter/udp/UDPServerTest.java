package com.payneteasy.srvlog.adapter.udp;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class UDPServerTest {
    private static final Charset UTF8 = Charset.forName("utf-8");

    @Test
    public void test() throws IOException, InterruptedException {
        final Map<String, String> accepted = Collections.synchronizedMap(new HashMap<String, String>());
        final CountDownLatch latch = new CountDownLatch(3);

        UDPServer server = new UDPServer(3333, 100, new IDatagramProcessor() {
            @Override
            public void processDatagram(Datagram datagram) {
                accepted.put(new String(datagram.getData(), UTF8), datagram.getSourceAddress().getHostName());
                latch.countDown();
            }
        });
        server.start();

        DatagramChannel channel = DatagramChannel.open();
        channel.connect(new InetSocketAddress("localhost", 3333));

        sendDatagram(channel, "one");
        sendDatagram(channel, "two");
        sendDatagram(channel, "three");
        channel.close();

        Assert.assertTrue("Not all messages have arrived", latch.await(5, TimeUnit.SECONDS));

        server.stop();

        Map<String, String> expected = new HashMap<String, String>();
        expected.put("one", "localhost");
        expected.put("two", "localhost");
        expected.put("three", "localhost");
        Assert.assertEquals(expected, accepted);
    }

    private void sendDatagram(DatagramChannel channel, String text) throws IOException {
        channel.write(ByteBuffer.wrap(text.getBytes(UTF8)));
    }
}