package org.mahatma.atp.common.db.daoImpl;

import com.feinno.util.StringUtils;
import org.helium.database.Database;
import org.mahatma.atp.common.db.bean.TaskResultDetail;
import org.mahatma.atp.common.db.dao.TaskResultDetailDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

/**
 * Created by lyfx on 17-10-11.
 */
public class TaskResultDetailDaoImpl implements TaskResultDetailDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskResultDetailDaoImpl.class);

    private Database atpDatabase;

    public TaskResultDetailDaoImpl(Database database) {
        this.atpDatabase = database;
    }

    @Override
    public long beginTestCase(TaskResultDetail taskResult) {
        String sql = "INSERT into ATP_taskResultDetail(`taskResultId`,`createTime`,`tcsId`,`tcId`,`state`)VALUE(?,?,?,?,?)";
        try {
            long id = atpDatabase.executeInsert(sql, taskResult.getTaskResultId(),
                    taskResult.getCreateTime(), taskResult.getTcsId(), taskResult.getTcId(), 0);
            return id;
        } catch (SQLException e) {
            LOGGER.error("INSERT INTO ATP_taskResult error!", e);
        }
        return -1;
    }

    @Override
    public void updateTestCase(TaskResultDetail taskResult) {
        String sql = "UPDATE ATP_taskResultDetail SET `code`=?,`desc`=?,`finishTime`=?,`record`=?,`state`=? WHERE `id` = ?";
        try {
            String desc = taskResult.getDesc();
            if (!StringUtils.isNullOrEmpty(desc)) {
                if (desc.length() > 200) {
                    LOGGER.warn("length over 200!subString 0-200 store!");
                    taskResult.setDesc(desc.substring(0, 200));
                }
            }
            String record = taskResult.getRecord();
            if (!StringUtils.isNullOrEmpty(record)) {
                if (record.length() > 32 * 1024) {
                    LOGGER.warn("length over 32k!subString 0-32*1024 store!");
                    taskResult.setRecord(record.substring(0, 16 * 1024));
                }
            }
            String recodeTmp = "";
            if (!StringUtils.isNullOrEmpty(taskResult.getRecord())) {
                recodeTmp = new String(taskResult.getRecord().getBytes(), "utf-8");
            }

            atpDatabase.executeUpdate(sql, taskResult.getCode(), taskResult.getDesc(), taskResult.getFinishTime(), recodeTmp, taskResult.getState(), taskResult.getId());
        } catch (SQLException e) {
            LOGGER.error("UPDATE ATP_taskResult error!", e);
            try {
                atpDatabase.executeUpdate(sql, taskResult.getCode(), taskResult.getDesc(), taskResult.getFinishTime(), "record save exception", taskResult.getState(), taskResult.getId());
            } catch (SQLException e1) {
                LOGGER.error("UPDATE ATP_taskResult_record error!", e);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteByTcId(Long tcId) {
        String sql = "delete from ATP_taskResultDetail where `tcId` = ?";
        try {
            atpDatabase.executeUpdate(sql, tcId);
        } catch (SQLException e) {
            LOGGER.error("pkgId : " + tcId + ",delete tc error!", e);
        }
    }
}
