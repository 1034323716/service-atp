package org.mahatma.atp.common.db.dao;

import org.mahatma.atp.common.db.bean.RunTimeLog;

/**
 * Created by lyfx on 17-10-11.
 */

public interface LogDao {

    /**
     * 插入log
     * @param runTimeLog
     */
    void insertLog(RunTimeLog runTimeLog);
}
