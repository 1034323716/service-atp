package org.mahatma.atp.task;

import org.helium.database.Database;
import org.helium.framework.annotations.FieldSetter;
import org.helium.framework.annotations.ScheduledTaskImplementation;
import org.helium.framework.task.ScheduledTask;
import org.mahatma.atp.common.db.dao.TaskResultDao;
import org.mahatma.atp.common.db.dao.TaskResultDetailDao;
import org.mahatma.atp.common.db.impl.TaskResultDaoImpl;
import org.mahatma.atp.common.db.impl.TaskResultDetailDaoImpl;
import org.mahatma.atp.common.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * 定时清理DB，避免数据库过大，页面长时间加载不出来
 * <p>
 * 凌晨2点清理db
 *
 * @author JiYunfei
 * @date 18-7-3
 */
@ScheduledTaskImplementation(id = "atp:SlimmingDbTask", cronExpression = "0 0 2 * * ? ")
public class SlimmingDbTask implements ScheduledTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(SlimmingDbTask.class);

    @FieldSetter("URCS_ATPDB")
    private Database atpDB;

    @Override
    public void processTask(Object lock) {
        // 删除7天前的结果记录
        Date date = DateUtil.getDateByday(-7);

        TaskResultDetailDao taskResultDetailDao = new TaskResultDetailDaoImpl(atpDB);
        taskResultDetailDao.clearByDay(date);

        TaskResultDao taskResultDao = new TaskResultDaoImpl(atpDB);
        taskResultDao.clearByDay(date);
    }

}