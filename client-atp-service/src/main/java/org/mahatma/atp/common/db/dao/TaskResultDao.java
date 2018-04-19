package org.mahatma.atp.common.db.dao;

import org.mahatma.atp.common.db.bean.TaskResult;

/**
 * Created by lyfx on 17-10-11.
 */

public interface TaskResultDao {
    /**
     * 用例运行开始时创建一条记录
     *
     * @param taskResult
     * @return 插入行id  -1表示失败
     */
    long createTaskResult(TaskResult taskResult);

    /**
     * 用例运行过成功更新记录
     *
     * @param taskResult
     */
    void updateTaskResult(TaskResult taskResult);

    void updateTaskResultState(long taskResultId, int state);

    boolean isExisted(long taskResultId);
}
