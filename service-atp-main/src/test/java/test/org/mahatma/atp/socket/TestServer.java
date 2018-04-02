package test.org.mahatma.atp.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by JiYunfei on 18-3-23.
 */
public class TestServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestServer.class);

    public static void main(String[] args) throws IOException {
        // 监听指定的端口
        int port = 6688;
        ServerSocket server = new ServerSocket(port);
        // server将一直等待连接的到来
        LOGGER.info("server将一直等待连接的到来");
        while (true) {
            Socket socket = server.accept();
            // 建立好连接后，从socket中获取输入流，并建立缓冲区进行读取
            InputStream inputStream = socket.getInputStream();
            byte[] bytes = new byte[1024];
            int len;
            StringBuilder sb = new StringBuilder();
            //只有当客户端关闭它的输出流的时候，服务端才能取得结尾的-1
            while ((len = inputStream.read(bytes)) != -1) {
                // 注意指定编码格式，发送方和接收方一定要统一，建议使用UTF-8
                sb.append(new String(bytes, 0, len, "UTF-8"));
            }
            LOGGER.info("get message from client: " + sb);

            inputStream.close();
        }
    }
}
