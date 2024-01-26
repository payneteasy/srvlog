package com.payneteasy.srvlog.adapter.utils;

import java.net.Socket;

/**
 * Date: 19.06.13
 * Time: 23:28
 */
public interface IRunnableFactory {

    Runnable createWorker(Socket clientSocket);

}
