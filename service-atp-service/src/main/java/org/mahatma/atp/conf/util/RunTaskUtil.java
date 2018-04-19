package org.mahatma.atp.conf.util;

import org.helium.database.Database;
import org.mahatma.atp.common.db.bean.TaskResult;
import org.mahatma.atp.common.util.FormatUtil;
import org.mahatma.atp.conf.AtpEnvConfiguration;
import org.mahatma.atp.dao.TaskLogStore;
import org.mahatma.atp.entity.RunType;
import org.mahatma.atp.service.ControlTaskService;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * Created by JiYunfei on 17-11-3.
 */
public class RunTaskUtil {

    public static void run(Long taskId, TaskLogStore taskLogStore, Long taskResultId,
                           ControlTaskService controlTaskService, Database atpDB, RunType runType) {
        File file = new File(FormatUtil.logPath(taskResultId));
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        SelectClassPathUtils selectClassPathUtils = new SelectClassPathUtils(atpDB);
        String addClassPath = selectClassPathUtils.getClassPathsByTaskId(taskId);

        int retest;
        if(AtpEnvConfiguration.getInstance().isRetest()) {
            retest = 1;
        } else {
            retest = 0;
        }

        String runShell = "sh " + FormatUtil.shellDir + FormatUtil.shellName
                + " '-taskId " + taskId
                + " -taskResultId " + taskResultId
                + " -retest " + retest
                + " -runType " + runType.intValue() + "' '" + addClassPath + "' >"
                + FormatUtil.logPath(taskResultId) + " 2>&1";
        CapturePkgBean capturePkgBean = new CapturePkgBean(taskResultId);
        capturePkgBean.start();
        RunShellUtil.runShellAndStore(runShell, taskLogStore, taskResultId, controlTaskService);
        capturePkgBean.stop();
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
