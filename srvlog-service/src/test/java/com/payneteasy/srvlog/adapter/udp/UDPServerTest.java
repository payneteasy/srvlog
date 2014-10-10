package com.payneteasy.srvlog.adapter.udp;

import com.payneteasy.srvlog.util.ComparisonUtils;
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
        final Map<String, Address> accepted = Collections.synchronizedMap(new HashMap<String, Address>());
        final CountDownLatch latch = new CountDownLatch(3);

        UDPServer server = new UDPServer(3333, 100, new IDatagramProcessor() {
            @Override
            public void processDatagram(Datagram datagram) {
                accepted.put(new String(datagram.getData(), UTF8), new Address(datagram.getSourceAddress().getHostName()));
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

        Map<String, Address> expected = new HashMap<String, Address>();
        expected.put("one", new Address("localhost"));
        expected.put("two", new Address("localhost"));
        expected.put("three", new Address("localhost"));
        Assert.assertEquals(expected, accepted);
    }

    private void sendDatagram(DatagramChannel channel, String text) throws IOException {
        channel.write(ByteBuffer.wrap(text.getBytes(UTF8)));
    }

    private static class Address {
        private final String addressString;

        private Address(String address) {
            this.addressString = address;
        }

        public String getAddressString() {
            return addressString;
        }

        @Override
        public String toString() {
            return addressString;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Address address = (Address) o;

            if (!ComparisonUtils.hostAddressesAreEqual(addressString, address.addressString)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return 0;
        }
    }
}