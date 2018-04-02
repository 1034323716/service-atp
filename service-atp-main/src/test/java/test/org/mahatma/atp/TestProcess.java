package test.org.mahatma.atp;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

/**
 * Created by JiYunfei on 18-3-23.
 */
public class TestProcess {
    public static void main(String[] args) {
        System.out.println(getProcessID());
    }

    public static final int getProcessID() {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        return Integer.valueOf(runtimeMXBean.getName().split("@")[0])
                .intValue();
    }
}
