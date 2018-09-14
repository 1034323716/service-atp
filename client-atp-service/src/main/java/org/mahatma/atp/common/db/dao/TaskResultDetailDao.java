package org.mahatma.atp.common.db.dao;

import org.mahatma.atp.common.db.bean.TaskResultDetail;

import java.util.Date;

/**
 * Created by lyfx on 17-10-11.
 */
public interface TaskResultDetailDao {
    /**
     * 用例运行开始时创建一条记录
     *
     * @param taskResult
     * @return 插入行id  -1表示失败
     */
    long beginTestCase(TaskResultDetail taskResult);

    /**
     * 用例运行过成功更新记录
     *
     * @param taskResult
     */
    void updateTestCase(TaskResultDetail taskResult);

    /**
     * 通过用例id来删除
     *
     * @param tcId
     */
    void deleteByTcId(Long tcId);

    /**
     * @param date 删除date之前的时间创建的记录
     */
    void clearByDay(Date date);
}
