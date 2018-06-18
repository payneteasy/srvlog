package com.payneteasy.srvlog.adapter.syslog.surricata;

import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;
import org.apache.commons.io.HexDump;

import java.io.IOException;
import java.net.*;

public class SuricataTestUdpMessage {

    public static void main(String[] args) throws IOException {

        DatagramSocket socket  = new DatagramSocket();
        InetAddress    address = InetAddress.getByName("185.15.175.20");
        byte[] buf = HexBin.decode("00 00 00 11 49 00 00 00 00 00 00 00 00 00 00 00 00 00 00 28 9E 00 00 ".replace(" ", ""));
//        byte[] buf = "Intel SDK for UPnP devices".getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 50000);
        socket.send(packet);
        socket.close();
    }
}
