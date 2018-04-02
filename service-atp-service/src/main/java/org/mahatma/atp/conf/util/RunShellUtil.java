package org.mahatma.atp.conf.util;

import org.mahatma.atp.common.exception.AutoTestRuntimeException;
import org.mahatma.atp.common.util.FormatUtil;
import org.mahatma.atp.dao.TaskLogStore;
import org.mahatma.atp.service.ControlTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;

/**
 * Created by JiYunfei on 17-9-27.
 */
public class RunShellUtil {
    private static Logger LOGGER = LoggerFactory.getLogger(RunShellUtil.class);

    public static void runShellAndRead(String command) {
        try {
            LOGGER.info(command);
            Process exec = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", command});
            InputStream stream = exec.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
            while ((bufferedReader.readLine()) != null) {
            }
            exec.waitFor();
            exec.destroy();
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
        }
    }

    public static void runShell(String command) {
        try {
            LOGGER.info(command);
            Process exec = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", command});
            exec.waitFor();
            exec.destroy();
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
        }
    }

    public static void runShellAndThrows(String command) throws InterruptedException, IOException {
        LOGGER.info(command);
        Process exec = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", command});
        exec.waitFor();
        exec.destroy();
    }

    public static void runShellAndStore(String command, TaskLogStore taskLogStore, Long taskResultId, ControlTaskService controlTaskService) {
        try {
            LOGGER.info("run shell and store log start, command:{}", command);
            Process exec = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", command});
            LOGGER.info("run shell and store log success, process id is:{}", getPid(exec));

            new Thread(() -> {
                try {
                    taskLogStore.insertLogFromFile(FormatUtil.logPath(taskResultId), taskResultId);
                } catch (AutoTestRuntimeException e) {
                    LOGGER.error(e.getMessage());
                }
            }).start();


            InputStream errorStream = exec.getErrorStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(errorStream));
            String errorTmp;
            while ((errorTmp = bufferedReader.readLine()) != null) {
                LOGGER.error("errorStream:" + errorTmp);
            }
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(exec.getInputStream()));
            while ((errorTmp = inputReader.readLine()) != null) {
                LOGGER.info("inputStream:" + errorTmp);
            }

            exec.getInputStream().close();
            exec.getErrorStream().close();
            exec.waitFor();
            exec.destroy();
            taskLogStore.closeLogFile(taskResultId);

            if (controlTaskService != null) {
                controlTaskService.remove(taskResultId);
            }

        } catch (Throwable e) {
            LOGGER.error("run shell and store log fail", e);
        }
    }

    public static long getPid(Process process) throws Exception {
        Field field = process.getClass().getDeclaredField("pid");
        field.setAccessible(true);
        long pid = field.getLong(process);
        return pid;
    }
}
