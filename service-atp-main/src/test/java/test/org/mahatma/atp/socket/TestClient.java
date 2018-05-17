package test.org.mahatma.atp.socket;

import org.mahatma.atp.common.util.ProcessUtil;
import org.mahatma.atp.common.util.AtpControlClient;

import java.io.IOException;

/**
 * Created by JiYunfei on 18-3-23.
 */
public class TestClient {
    public static void main(String[] args) throws IOException {
        AtpControlClient atpControlClient = new AtpControlClient();
        atpControlClient.connection("10.10.12.185", 6688);
        atpControlClient.send(123 + ":" + ProcessUtil.getProcessID());
    }
}
