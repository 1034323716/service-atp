package org.mahatma.atp.common.db.dao;

import org.mahatma.atp.common.db.bean.Task;

/**
 * Created by JiYunfei on 17-9-8.
 */
public interface TaskDao {
    Task get(Long id);
}
