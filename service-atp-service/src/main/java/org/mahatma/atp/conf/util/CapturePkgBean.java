package org.mahatma.atp.conf.util;

import org.mahatma.atp.conf.AtpEnvConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by JiYunfei on 18-3-28.
 */
public class CapturePkgBean {
    private static Logger LOGGER = LoggerFactory.getLogger(CapturePkgBean.class);
    private long taskResultId;
    private Process exec;

    public CapturePkgBean(long taskResultId) {
        this.taskResultId = taskResultId;
    }

    public void start() {
        if (AtpEnvConfiguration.getInstance().isCapturePkg()) {
            try {
                String command = "tcpdump -i eth0 -w /tmp/atpPkg/" + taskResultId + ".pcap";
                LOGGER.info("capture package start, command:{}, task result id:{}", command, taskResultId);
                exec = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", command});
                LOGGER.info("capture package process id:{}, task result id:{}", RunShellUtil.getPid(exec), taskResultId);
            } catch (Exception e) {
                LOGGER.error("capture pkg fail, task result id:{}", taskResultId, e);
            }
        }
    }

    public void stop() {
        if (AtpEnvConfiguration.getInstance().isCapturePkg()) {
            LOGGER.info("capture package process destroy start, task result id:{}", taskResultId);
            exec.destroy();
            LOGGER.info("capture package process destroy success, task result id:{}", taskResultId);
        }
    }
}
