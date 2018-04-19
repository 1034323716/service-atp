package org.mahatma.atp.common.db.dao;

import org.mahatma.atp.common.db.bean.TaskResultDetail;

/**
 * Created by lyfx on 17-10-11.
 */

public interface TaskResultDetailDao {
    /**
     * 用例运行开始时创建一条记录
     * @param taskResult
     * @return 插入行id  -1表示失败
     */
    long beginTestCase(TaskResultDetail taskResult);

    /**
     * 用例运行过成功更新记录
     * @param taskResult
     */
    void updateTestCase(TaskResultDetail taskResult);

    void deleteByTcId(Long tcId);
}
