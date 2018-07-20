package org.mahatma.atp.conf.util;

import org.helium.database.Database;
import org.mahatma.atp.common.db.bean.TaskResult;
import org.mahatma.atp.conf.AtpEnvConfiguration;
import org.mahatma.atp.dao.TaskLogStore;
import org.mahatma.atp.entity.RunType;
import org.mahatma.atp.service.ControlTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by JiYunfei on 17-11-3.
 */
public class RunTaskUtil {
    private static Logger LOGGER = LoggerFactory.getLogger(RunTaskUtil.class);

    /**若执行的不是定时任务则planId为0**/
    public static void run(Long taskId, long planId, TaskLogStore taskLogStore, Long taskResultId,
                           ControlTest controlTest, Database atpDB, RunType runType) {
        String logPath = FilePathUtil.logPath(taskResultId);
        boolean createLogResult = FilePathUtil.checkFileAndCreate(logPath);
        if (!createLogResult) {
            LOGGER.error("create log fail, log path:{}", logPath);
            return;
        }
        SelectClassPathUtils selectClassPathUtils = new SelectClassPathUtils(atpDB);
        String addClassPath = selectClassPathUtils.getClassPathsByTaskId(taskId);

        String runShell = "sh " + AtpEnvConfiguration.getInstance().getRunShPath()
                + " '-taskId " + taskId
                + " '-planId " + planId
                + " -taskResultId " + taskResultId
                + " -retest " + isRetest()
                + " -runType " + runType.intValue() + "' '" + addClassPath + "' >"
                + logPath + " 2>&1";
        CapturePkgBean capturePkgBean = new CapturePkgBean(taskResultId);
        capturePkgBean.start();
        RunShellUtil.runShellAndStore(runShell, taskLogStore, taskResultId, controlTest);
        capturePkgBean.stop();
    }

    /**返回1代表要失败时重测，但会0代表不重测**/
    private static int isRetest() {
        int retest;
        if (AtpEnvConfiguration.getInstance().isRetest()) {
            retest = 1;
        } else {
            retest = 0;
        }
        return retest;
    }

    public static Long prepare(Long taskId, TaskLogStore taskLogStore, Long planId) {
        TaskResult taskResult = new TaskResult();
        taskResult.setTaskId(taskId);
        taskResult.setCreateTime(new Date());
        if (planId != null) {
            taskResult.setPlanId(planId);
        }
        return taskLogStore.createTaskResult(taskResult);
    }
}
