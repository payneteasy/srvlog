package com.payneteasy.srvlog.adapter.syslog.surricata;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.apache.xerces.impl.dv.util.HexBin;

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
