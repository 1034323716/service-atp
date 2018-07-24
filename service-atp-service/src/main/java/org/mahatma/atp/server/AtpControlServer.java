package org.mahatma.atp.server;

import com.feinno.superpojo.SuperPojoManager;
import org.helium.framework.annotations.ServiceImplementation;
import org.helium.framework.annotations.ServiceSetter;
import org.helium.framework.tag.Initializer;
import org.mahatma.atp.common.engine.spi.AtpThreadFactory;
import org.mahatma.atp.common.util.entity.ControlPkg;
import org.mahatma.atp.conf.AtpEnvConfiguration;
import org.mahatma.atp.service.ControlTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author JiYunfei
 * @date 18-3-23
 */
@ServiceImplementation
public class AtpControlServer implements IAtpControlServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(AtpControlServer.class);

    @ServiceSetter
    private ControlTest controlTest;

    @Initializer
    public void initControlServer() throws IOException {
        LOGGER.info("task control server init start");
        // 监听指定的端口
        int port = AtpEnvConfiguration.getInstance().getServerSocketPort();
        ServerSocket server = new ServerSocket(port);
        // server将一直等待连接的到来
        LOGGER.info("server将一直等待连接的到来");
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(),
                new AtpThreadFactory("ATP-AtpControlServer"));
        threadPool.execute(() -> {
            while (true) {
                try {
                    Socket socket = server.accept();
                    // 建立好连接后，从socket中获取输入流，并建立缓冲区进行读取
                    InputStream inputStream = socket.getInputStream();
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    byte[] bytes = new byte[1024];
                    int len;
                    //只有当客户端关闭它的输出流的时候，服务端才能取得结尾的-1
                    while ((len = inputStream.read(bytes)) != -1) {
                        // 注意指定编码格式，发送方和接收方一定要统一，建议使用UTF-8
                        byteArrayOutputStream.write(bytes, 0, len);
                    }
                    byte[] pkgBytes = byteArrayOutputStream.toByteArray();
                    ControlPkg controlPkg = SuperPojoManager.parsePbFrom(pkgBytes, ControlPkg.class);
                    LOGGER.info("收到控制信息:{}", controlPkg);
                    if (controlPkg.getStatus() == 1) {
                        controlTest.add(controlPkg.getTaskResultId(), controlPkg);
                    } else if (controlPkg.getStatus() == 0) {
                        controlTest.remove(controlPkg.getTaskResultId());
                    }

                    inputStream.close();
                } catch (Exception e) {
                    LOGGER.error("AtpControlServer Exception", e);
                }
            }
        });
    }
}
