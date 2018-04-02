package org.mahatma.atp.dao;

import org.helium.framework.annotations.ServiceInterface;
import org.mahatma.atp.common.db.bean.RunTimeLog;
import org.mahatma.atp.common.db.bean.TaskResult;
import org.mahatma.atp.common.exception.AutoTestRuntimeException;

import java.io.InputStream;

/**
 * Created by lyfx on 17-10-11.
 */
@ServiceInterface(id = "atp:TaskLogStore")
public interface TaskLogStore {

    /**
     * @param taskResult
     * @return taskResultId
     */
    long createTaskResult(TaskResult taskResult);

    void insertLog(RunTimeLog log);

    void insertLogFromStream(InputStream inputStream, long taskResultId) throws AutoTestRuntimeException;

    /**
     * 从日志文件读取日志存入数据库
     *
     * @param logFilePath
     * @param taskResultId
     * @throws AutoTestRuntimeException
     */
    void insertLogFromFile(String logFilePath, long taskResultId) throws AutoTestRuntimeException;

    /**
     * 脚本运行完成，关闭文件流
     *
     * @param taskResultId
     */
    void closeLogFile(long taskResultId) throws AutoTestRuntimeException;


    void updateTaskResult(TaskResult taskResult);

}
