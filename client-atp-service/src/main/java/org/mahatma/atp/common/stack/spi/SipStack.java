package org.mahatma.atp.common.stack.spi;


import com.feinno.sip.provider.SipProvider;
import com.feinno.superpojo.util.StringUtils;
import com.feinno.urcs.sip.SipConnector;
import org.mahatma.atp.common.annotations.TestStack;
import org.mahatma.atp.common.engine.ModuleReportManager;
import org.mahatma.atp.common.stack.ModuleStack;
import org.mahatma.atp.common.type.StackTypec;
import org.mahatma.atp.common.util.SysFreePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;

@TestStack(id = StackTypec.SIP)
public class SipStack implements ModuleStack {

    private SipProvider sipProvider;

    private String proto = SipProvider.PROTO_TCP;

    private Logger LOGGER = LoggerFactory.getLogger(ModuleReportManager.class);

    private String localIp;

    private SipConnector connector;
    private int port;

    @Override
    public void start() throws Exception {
        if (sipProvider == null) {
            //本地的ip和端口
            localIp = StringUtils.isNullOrEmpty(System.getenv("PRIVATE_IP")) ? InetAddress.getLocalHost().getHostAddress() : System.getenv("PRIVATE_IP");
//            port = PortUtil.getPort(localIp);
//            connector = new SipConnector(localIp, port, proto);
//            if (port < 0) {
//                throw new Exception("no port is available!");
//            }
//            AtpFormatUtil.printListening(proto, localIp, port);
        }
    }

    @Override
    public void stop() throws Exception {
        sipProvider.halt();
    }

    /**
     * 每次拿到的都是不同的，新的sip协议栈
     */
    @Override
    public Object getStack() throws IOException {
        port = SysFreePort.getFreePort();
        connector = new SipConnector(localIp, port, proto);
        return connector;
    }

    public String getLocalIp() {
        return localIp;
    }

    public void setLocalIp(String localIp) {
        this.localIp = localIp;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
