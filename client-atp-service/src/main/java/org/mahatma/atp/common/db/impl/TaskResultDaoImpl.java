package org.mahatma.atp.common.db.impl;

import org.helium.database.DataTable;
import org.helium.database.Database;
import org.mahatma.atp.common.db.bean.TaskResult;
import org.mahatma.atp.common.db.dao.TaskResultDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Date;

/**
 * @author lyfx
 * @date 17-10-11
 */
public class TaskResultDaoImpl implements TaskResultDao {

    private Database atpDatabase;

    public TaskResultDaoImpl(Database database) {
        this.atpDatabase = database;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskResultDaoImpl.class);

    @Override
    public long createTaskResult(TaskResult taskResult) {
        String sql = "INSERT INTO ATP_taskResult(`code`,`desc`,`createTime`,`finishTime`,`taskId`,`planId`,`state`)VALUES(?,?,?,?,?,?,?)";
        try {
            long id = atpDatabase.executeInsert(sql, taskResult.getCode(), taskResult.getDesc(),
                    taskResult.getCreateTime(), taskResult.getFinishTime(), taskResult.getTaskId(), taskResult.getPlanId(), 0);
            return id;
        } catch (SQLException e) {
            LOGGER.error("INSERT INTO ATP_taskResult error!", e);
        }
        return -1;

    }

    @Override
    public void updateTaskResult(TaskResult taskResult) {
        String sql = "UPDATE ATP_taskResult SET `code`=?,`desc`=?,`finishTime`=?,`state`=? WHERE `id` = ?";
        try {
            atpDatabase.executeUpdate(sql, taskResult.getCode(), taskResult.getDesc(), taskResult.getFinishTime(), taskResult.getState(), taskResult.getId());
        } catch (SQLException e) {
            LOGGER.error("UPDATE ATP_taskResult error!", e);
        }
    }

    @Override
    public void updateTaskResultState(long taskResultId, int state) {
        String sql = "UPDATE ATP_taskResult SET `state`=?,`finishTime`=? WHERE `id` = ?";
        try {
            atpDatabase.executeUpdate(sql, state, new Date(), taskResultId);
        } catch (SQLException e) {
            LOGGER.error("UPDATE ATP_taskResult error!", e);
        }
    }

    @Override
    public boolean isExisted(long taskResultId) {
        String sql = "select count(id) from ATP_taskResult where `id` = ?";
        int i = 0;
        try {
            DataTable dataTable = atpDatabase.executeTable(sql, taskResultId);
            if (dataTable != null) {
                if (dataTable.getRows().size() > 0) {
                    i = dataTable.getRows().get(0).getInt(1);
                }
            }
        } catch (SQLException e) {
            LOGGER.error("isExisted error!", e);
        }
        return i != 0;
    }
}
