package org.mahatma.atp.impl;

import com.feinno.superpojo.util.StringUtils;
import org.helium.database.Database;
import org.helium.framework.annotations.FieldSetter;
import org.helium.framework.annotations.ServiceImplementation;
import org.mahatma.atp.common.db.bean.RunTimeLog;
import org.mahatma.atp.common.db.bean.TaskResult;
import org.mahatma.atp.common.db.dao.LogDao;
import org.mahatma.atp.common.db.dao.TaskResultDao;
import org.mahatma.atp.common.db.impl.LogDaoImpl;
import org.mahatma.atp.common.db.impl.TaskResultDaoImpl;
import org.mahatma.atp.common.exception.AutoTestRuntimeException;
import org.mahatma.atp.dao.TaskLogStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lyfx on 17-10-11.
 */
@ServiceImplementation
public class TaskLogStoreImpl implements TaskLogStore {
    private static Logger LOGGER = LoggerFactory.getLogger(TaskLogStoreImpl.class);


    @FieldSetter("URCS_ATPDB")
    private Database atpDB;

    private Map<Long, Boolean> taskIdReadFileSwitch = new HashMap<>();

    private TaskResultDao taskResultDao = null;

    @Override
    public long createTaskResult(TaskResult taskResult) {
        TaskResultDao taskResultDao = new TaskResultDaoImpl(atpDB);
        return taskResultDao.createTaskResult(taskResult);
    }

    @Override
    public void insertLog(RunTimeLog log) {
        LogDao logDao = new LogDaoImpl(atpDB);
        logDao.insertLog(log);
    }

    @Override
    public void insertLogFromStream(InputStream inputStream, long taskResultId) throws AutoTestRuntimeException {
        TaskResultDao taskResultDao = new TaskResultDaoImpl(atpDB);

        if (!taskResultDao.isExisted(taskResultId)) {
            throw new AutoTestRuntimeException("taskResultId: " + taskResultId + " is Not Existed in DB!");
        }

        LogDao logDao = new LogDaoImpl(atpDB);
        byte[] bytes = new byte[1024];
        RunTimeLog log = new RunTimeLog();
        log.setTaskResultId(taskResultId);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            int size = 0;

            while ((size = (inputStream.read(bytes))) > 0) {
                byteArrayOutputStream.write(bytes, 0, size);
                String tmp = new String(byteArrayOutputStream.toByteArray());
                log.setLog(tmp);
                logDao.insertLog(log);
                byteArrayOutputStream.reset();
            }
        } catch (IOException e) {
            LOGGER.error(" ", e);
        }
    }

    @Override
    public void insertLogFromFile(String logFilePath, long taskResultId) throws AutoTestRuntimeException {

        if (taskResultDao == null) {
            taskResultDao = new TaskResultDaoImpl(atpDB);
        }

        File file = new File(logFilePath);
        if (!file.exists()) {
            throw new AutoTestRuntimeException("file " + logFilePath + " not exist!");
        }

        TaskResultDao taskResultDao = new TaskResultDaoImpl(atpDB);
        Boolean aBoolean = taskIdReadFileSwitch.get(taskResultId);
        if (aBoolean == null) {
            taskIdReadFileSwitch.put(taskResultId, true);
        }

        if (!taskResultDao.isExisted(taskResultId)) {
            throw new AutoTestRuntimeException("taskResultId: " + taskResultId + " is Not Existed in DB!");
        }

        LogDao logDao = new LogDaoImpl(atpDB);
        RunTimeLog log = new RunTimeLog();
        log.setTaskResultId(taskResultId);
        String tmp;
        int lines = 0;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader fileBufferedReader = new BufferedReader(fileReader);
            while (true) {
                tmp = fileBufferedReader.readLine();
                lines++;
                if (tmp != null) {
                    if (StringUtils.isNullOrEmpty(tmp)) {
                        continue;
                    }
                    stringBuilder.append(tmp).append("\n");
                    if (lines == 5) {
                        lines = 0;
                        String logStr = stringBuilder.toString();
                        if (!isHandledLogStr(logStr, log, logDao)) {
                            insertLogToDB(stringBuilder.toString(), log, logDao);
                        }
                        stringBuilder = new StringBuilder();
                    }
                } else {
                    lines = 0;
                    if (!taskIdReadFileSwitch.get(taskResultId)) {
                        if (!StringUtils.isNullOrEmpty(stringBuilder.toString())) {
                            if (!isHandledLogStr(stringBuilder.toString(), log, logDao)) {
                                insertLogToDB(stringBuilder.toString(), log, logDao);
                            }
                        }
                        fileBufferedReader.close();
                        fileReader.close();
                        break;
                    } else {
                        if (!isHandledLogStr(stringBuilder.toString(), log, logDao)) {
                            if (!StringUtils.isNullOrEmpty(stringBuilder.toString())) {
                                insertLogToDB(stringBuilder.toString(), log, logDao);
                            }
                        }
                    }
                    stringBuilder = new StringBuilder();
                }
                Thread.sleep(200);
            }
        } catch (Exception e) {
            throw new AutoTestRuntimeException(e.getMessage());
        }
    }

    @Override
    public void closeLogFile(long taskResultId) throws AutoTestRuntimeException {
        LOGGER.info("close log file taskResultId:{}", taskResultId);
        Boolean aBoolean = taskIdReadFileSwitch.get(taskResultId);
        if (aBoolean == null) {
            throw new AutoTestRuntimeException("taskResultId: " + taskResultId + " is Not Existed !");
        }
        taskIdReadFileSwitch.put(taskResultId, false);
    }

    @Override
    public void updateTaskResult(TaskResult taskResult) {
        TaskResultDao taskResultDao = new TaskResultDaoImpl(atpDB);
        taskResultDao.updateTaskResult(taskResult);
    }

    private void insertLogToDB(String tmp, RunTimeLog log, LogDao logDao) {
        if (!StringUtils.isNullOrEmpty(tmp)) {
            log.setLog(tmp);
            logDao.insertLog(log);
        }
    }

    private boolean isHandledLogStr(String logStr, RunTimeLog log, LogDao logDao) {
        if (logStr.length() > 32 * 1024) {
            LOGGER.warn("length over 32k!begin split store!");
            int turns = logStr.length() / (32 * 1024);

            if ((logStr.length() % (32 * 1024)) != 0) {
                turns = turns + 1;
            }

            int lastIndex = 0;
            int endIndex = 0;
            for (int i = 0; i < turns; i++) {
                if (i == turns - 1) {
                    insertLogToDB(logStr.substring(lastIndex), log, logDao);
                } else {
                    endIndex = lastIndex + 32 * 1024;
                    lastIndex = endIndex;
                    insertLogToDB(logStr.substring(lastIndex, endIndex), log, logDao);
                }
            }
            return true;
        }

        return false;
    }

}
