package org.mahatma.atp.service.impl;

import org.helium.database.Database;
import org.helium.framework.annotations.FieldSetter;
import org.helium.framework.annotations.ServiceImplementation;
import org.mahatma.atp.common.db.bean.TaskResult;
import org.mahatma.atp.common.db.dao.TaskResultDao;
import org.mahatma.atp.common.db.impl.TaskResultDaoImpl;
import org.mahatma.atp.common.util.entity.ControlPkg;
import org.mahatma.atp.conf.AtpResultCodeManager;
import org.mahatma.atp.conf.ResultCodeParam;
import org.mahatma.atp.conf.util.RunShellUtil;
import org.mahatma.atp.service.ControlTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author JiYunfei
 * @date 17-11-16
 */
@ServiceImplementation
public class ControlTestImpl implements ControlTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ControlTestImpl.class);
    private static Map<Long, ControlPkg> runProcessMap = new ConcurrentHashMap<>();

    public static Map<Long, ControlPkg> getRunProcessMap() {
        return runProcessMap;
    }

    @FieldSetter("URCS_ATPDB")
    private Database atpDB;

    @Override
    public void stop(long taskResultId) {
        ControlPkg controlPkg = runProcessMap.get(taskResultId);
        int processId = controlPkg.getPid();
        killProcessTree(processId);
        LOGGER.info("stop task success, task result id:{}", taskResultId);
        TaskResultDao taskResultDao = new TaskResultDaoImpl(atpDB);
        TaskResult taskResult = new TaskResult();
        taskResult.setId(taskResultId);
        taskResult.setDesc(AtpResultCodeManager.getReturnCodeDoc(ResultCodeParam.HALFWAYSTOP).getDoc());
        // 1代表的已结束
        taskResult.setState(1);
        taskResult.setCode(String.valueOf(AtpResultCodeManager.getReturnCodeDoc(ResultCodeParam.HALFWAYSTOP).getCode()));
        taskResultDao.updateTaskResult(taskResult);
        remove(taskResultId);
    }

    private static void killProcessTree(int processId) {
        try {
            LOGGER.info("pid:{}", processId);
            RunShellUtil.runShell(getKillProcessTreeCmd(processId));
        } catch (Exception e) {
            LOGGER.error("killProcess exception:\n{}", e);
        }
    }

    private static String getKillProcessTreeCmd(int processId) {
        return "kill -9 " + processId;
    }

    @Override
    public void add(long taskResultId, ControlPkg controlPkg) {
        runProcessMap.put(taskResultId, controlPkg);
    }

    @Override
    public void remove(long taskResultId) {
        TaskResultDao taskResultDao = new TaskResultDaoImpl(atpDB);
        taskResultDao.updateTaskResultState(taskResultId, 1);
        LOGGER.info("taskResultId:{} get log end!", taskResultId);

        runProcessMap.remove(taskResultId);
    }

    @Override
    public boolean exist(long taskResultId) {
        return runProcessMap.containsKey(taskResultId);
    }

    @Override
    public boolean planIsRun(long planId) {
        List<Long> taskResultIdList = planRuningId(planId);

        if (taskResultIdList.size() != 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Long> planRuningId(long planId) {
        List<Long> taskResultIdList = new ArrayList<>();

        for (Map.Entry<Long, ControlPkg> longControlPkgEntry : runProcessMap.entrySet()) {
            ControlPkg value = longControlPkgEntry.getValue();
            if (value.getPlanId() == planId && value.getPlanId() != -1L) {
                Long taskResultId = longControlPkgEntry.getKey();
                taskResultIdList.add(taskResultId);
            }
        }

        return taskResultIdList;
    }

    @Override
    public void stopPlan(long planId) {
        List<Long> taskResultIdList = planRuningId(planId);
        for (Long taskResultId : taskResultIdList) {
            stop(taskResultId);
        }
    }
}
