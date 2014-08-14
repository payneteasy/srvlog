package com.payneteasy.srvlog.adapter.udp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * UDP processing server. Contains two threads: acceptor thread reads
 * datagrams from the socket and puts them to the blocking queue;
 * while processing thread takes them from the queue and processes them.
 * If the queue is full, acceptor thread silently discards current
 * datagram.
 *
 * @author rpuch
 */
public class UDPServer {
    private static final int MAX_DATAGRAM_SIZE = 65527;

    private static final Logger logger = LoggerFactory.getLogger(UDPServer.class);

    private final int serverPort;
    private final IDatagramProcessor datagramProcessor;
    // this executor must be single-threaded
    private final ExecutorService acceptorExecutor = Executors.newSingleThreadExecutor();
    private DatagramChannel channel;

    private final BlockingQueue<Datagram> queue;
    private final ExecutorService processingExecutor = Executors.newSingleThreadExecutor();

    private volatile boolean started = false;
    private volatile boolean stopped = false;

    private final byte[] bufferArray = new byte[MAX_DATAGRAM_SIZE];
    private final ByteBuffer buffer = ByteBuffer.wrap(bufferArray);

    public UDPServer(int serverPort, int queueCapacity, IDatagramProcessor datagramProcessor) {
        this.serverPort = serverPort;
        this.datagramProcessor = datagramProcessor;

        queue = new LinkedBlockingQueue<Datagram>(queueCapacity);
    }

    public void start() {
        if (started) {
            throw new IllegalStateException("Already started");
        }
        logger.info("Starting UDP acceptor on port {}...", serverPort);
        started = true;
        channel = openAndBindChannel();
        logger.info("Started UDP acceptor on port {}", serverPort);
        acceptorExecutor.execute(new Runnable() {
            public void run() {
                acceptDatagrams();
            }
        });
        processingExecutor.execute(new Runnable() {
            @Override
            public void run() {
                processorLoop();
            }
        });
    }

    private void processorLoop() {
        while (!stopped) {
            Datagram datagram;
            try {
                datagram = queue.take();
                try {
                    datagramProcessor.processDatagram(datagram);
                } catch (Exception e) {
                    logger.error("Exception while processing a datagram", e);
                }
            } catch (InterruptedException e) {
                // ignoring as we are leaving the loop on stopped condition
            }
        }
    }

    private void acceptDatagrams() {
        try {
            acceptorLoop();
        } catch (IOException e) {
            if (!stopped) {
                logger.error("Caught exception in accept loop, exiting it", e);
                stop();
            }
        }
    }

    private void acceptorLoop() throws IOException {
        while (!stopped) {
            buffer.clear();
            SocketAddress address = channel.receive(buffer);
            if (address != null) {
                buffer.flip();
                Datagram datagram = new Datagram(getInetAddress(address),
                        bufferArray, buffer.position(), buffer.limit());
                // inserting to the queue
                if (!queue.offer(datagram)) {
                    logger.error("Datagram from address {} discarded because queue is full", datagram.getSourceAddress());
                }
            } else {
                logger.warn("Unexpected null address returned from channel.receive()");
            }
        }
    }

    private InetAddress getInetAddress(SocketAddress address) {
        if (address instanceof InetSocketAddress) {
            InetSocketAddress inetSocketAddress = (InetSocketAddress) address;
            return inetSocketAddress.getAddress();
        }
        // something unknown
        return null;
    }

    private DatagramChannel openAndBindChannel() {
        DatagramChannel channel;
        try {
            channel = DatagramChannel.open();
            channel.bind(new InetSocketAddress(serverPort));
            return channel;
        } catch (IOException e) {
            throw new IllegalStateException("Cannot open channel", e);
        }
    }

    public synchronized void stop() {
        if (stopped) {
            return;
        }

        logger.info("Stopping UDP acceptor on port {}...", serverPort);
        stopped = true;

        // closing UDP channel
        try {
            channel.close();
        } catch (IOException e) {
            // ignoring
        }
        logger.info("Stopped UDP acceptor on port {}", serverPort);

        // shutting down executors
        acceptorExecutor.shutdown();
        processingExecutor.shutdown();
        try {
            acceptorExecutor.awaitTermination(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            // ignoring
        }
    }
}
