package test.org.mahatma.atp.socket;

import org.mahatma.atp.common.util.ProcessUtil;
import org.mahatma.atp.common.util.TaskControlClient;

import java.io.IOException;

/**
 * Created by JiYunfei on 18-3-23.
 */
public class TestClient {
    public static void main(String[] args) throws IOException {
        TaskControlClient taskControlClient = new TaskControlClient();
        taskControlClient.connection("10.10.12.185", 6688);
        taskControlClient.send(123 + ":" + ProcessUtil.getProcessID());
    }
}
