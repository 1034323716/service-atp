package org.mahatma.atp.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by JiYunfei on 18-3-23.
 */
public class AtpControlClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(AtpControlClient.class);
    private Socket socket;
    public void connection(String remoteIP, int remotePort) throws IOException {
        // 与服务端建立连接
        socket = new Socket(remoteIP, remotePort);
    }
    public void disconnect() throws IOException {
        socket.close();
    }
    public void send(String message) throws IOException {
        LOGGER.info("TaskControlClient Send:{}", message);
        send(message.getBytes("UTF-8"));
    }
    public void send(byte[] pkg) throws IOException {
        // 建立连接后获得输出流
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(pkg);
        outputStream.close();
    }
    public void closeSend() throws IOException {
        //通过shutdownOutput高速服务器已经发送完数据，后续只能接受数据
        socket.shutdownOutput();
    }
}
