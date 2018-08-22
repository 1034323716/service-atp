package org.mahatma.atp.conf.util;

import org.mahatma.atp.common.exception.AutoTestRuntimeException;
import org.mahatma.atp.common.executor.AtpExecutorFactory;
import org.mahatma.atp.dao.TaskLogStore;
import org.mahatma.atp.service.ControlTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.concurrent.Executor;

/**
 * @author JiYunfei
 * @date 17-9-27
 */
public class RunShellUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(RunShellUtil.class);
    private static final int TASK_EXECUTOR_SIZE = 32;
    private static Executor executor = AtpExecutorFactory.newFixedExecutor("Run-Shell-Executor", TASK_EXECUTOR_SIZE, TASK_EXECUTOR_SIZE * 64);

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

    public static void runShellAndStoreLog(String command, TaskLogStore taskLogStore, Long taskResultId, ControlTest controlTest) {
        try {
            LOGGER.info("run shell and store log start, command:{}", command);
            Process exec = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", command});
            LOGGER.info("run shell and store log success, process id is:{}", getPid(exec));

            executor.execute(() -> {
                try {
                    taskLogStore.insertLogFromFile(FilePathUtil.logPath(taskResultId), taskResultId);
                } catch (AutoTestRuntimeException e) {
                    LOGGER.error(e.getMessage());
                }
            });

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

            if (controlTest != null) {
                controlTest.remove(taskResultId);
            }

        } catch (Throwable e) {
            LOGGER.error("run shell and store log fail", e);
        }
    }

    public static void runShellNonStoreLog(String command, long taskResultId, ControlTest controlTest) {
        try {
            LOGGER.info("run shell non store log start, command:{}", command);
            Process exec = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", command});
            LOGGER.info("run shell non store log success, process id is:{}", getPid(exec));

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

            if (controlTest != null) {
                controlTest.remove(taskResultId);
            }

        } catch (Throwable e) {
            LOGGER.error("run shell non store log fail", e);
        }
    }

    public static long getPid(Process process) throws Exception {
        Field field = process.getClass().getDeclaredField("pid");
        field.setAccessible(true);
        long pid = field.getLong(process);
        return pid;
    }
}
