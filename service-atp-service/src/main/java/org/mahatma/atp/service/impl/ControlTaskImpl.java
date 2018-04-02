package org.mahatma.atp.service.impl;

import org.helium.database.Database;
import org.helium.framework.annotations.FieldSetter;
import org.helium.framework.annotations.ServiceImplementation;
import org.mahatma.atp.common.db.bean.TaskResult;
import org.mahatma.atp.common.db.dao.TaskResultDao;
import org.mahatma.atp.common.db.daoImpl.TaskResultDaoImpl;
import org.mahatma.atp.conf.util.RunShellUtil;
import org.mahatma.atp.service.ControlTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by JiYunfei on 17-11-16.
 */
@ServiceImplementation
public class ControlTaskImpl implements ControlTaskService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ControlTaskImpl.class);
    private static Map<Long, Integer> runProcessMap = new HashMap<>();

    @FieldSetter("URCS_ATPDB")
    private Database atpDB;

    @Override
    public void stop(Long taskResultId) {
        int processId = runProcessMap.get(taskResultId);
        killProcessTree(processId);
        LOGGER.info("stop task success , task result id is {}", taskResultId);
        TaskResultDao taskResultDao = new TaskResultDaoImpl(atpDB);
        TaskResult taskResult = new TaskResult();
        taskResult.setId(taskResultId);
        taskResult.setDesc("taks halfway stop");
        taskResult.setState(1);
        taskResult.setCode("321");
        taskResultDao.updateTaskResult(taskResult);
    }

    private static void killProcessTree(int processId) {
        try {
            LOGGER.info("pid is {}", processId);
            RunShellUtil.runShell(getKillProcessTreeCmd(processId));
        } catch (Exception e) {
            LOGGER.error("killProcess exception : \n{}", e);
        }
    }

    private static String getKillProcessTreeCmd(int processId) {
        return "kill -9 " + processId;
    }

    @Override
    public void add(Long taskResultId, int processId) {
        runProcessMap.put(taskResultId, processId);
    }

    @Override
    public void remove(Long taskResultId) {
        runProcessMap.remove(taskResultId);
    }

    @Override
    public boolean exist(Long taskResultId) {
        return runProcessMap.containsKey(taskResultId);
    }
}
