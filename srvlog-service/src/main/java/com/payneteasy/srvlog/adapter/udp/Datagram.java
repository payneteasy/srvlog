package com.payneteasy.srvlog.adapter.udp;

import java.net.InetAddress;

/**
 * @author rpuch
 */
public class Datagram {
    private final InetAddress sourceAddress;
    private final byte[] data;

    public Datagram(InetAddress sourceAddress, byte[] sourceBuffer, int offset, int length) {
        this.sourceAddress = sourceAddress;
        this.data = new byte[length];
        System.arraycopy(sourceBuffer, offset, this.data, 0, length);
    }

    public InetAddress getSourceAddress() {
        return sourceAddress;
    }

    public byte[] getData() {
        return data;
    }
}
