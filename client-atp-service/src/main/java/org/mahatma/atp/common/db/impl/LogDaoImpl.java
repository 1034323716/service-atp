package org.mahatma.atp.common.db.impl;

import org.helium.database.Database;
import org.mahatma.atp.common.db.bean.RunTimeLog;
import org.mahatma.atp.common.db.dao.LogDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

/**
 * Created by lyfx on 17-10-11.
 */
public class LogDaoImpl implements LogDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskResultDaoImpl.class);

    private Database atpDatabase;

    public LogDaoImpl(Database database) {
        this.atpDatabase = database;
    }

    @Override
    public void insertLog(RunTimeLog runTimeLog) {
        String sql = "INSERT into ATP_log(`taskResultId`, `log`) VALUE (?,?)";
        try {
            atpDatabase.executeInsert(sql,runTimeLog.getTaskResultId(),runTimeLog.getLog());
        } catch (SQLException e) {
            LOGGER.error("INSERT into ATP_log error!runTimeLog resultId {} runTimeLog size {}",runTimeLog.getTaskResultId(),runTimeLog.getLog().length(),e);
        }
    }
}
