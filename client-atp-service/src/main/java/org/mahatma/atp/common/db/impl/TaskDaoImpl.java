package org.mahatma.atp.common.db.impl;

import org.helium.database.DataRow;
import org.helium.database.DataTable;
import org.helium.database.Database;
import org.mahatma.atp.common.db.bean.Task;
import org.mahatma.atp.common.db.dao.TaskDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by JiYunfei on 17-10-10.
 */
public class TaskDaoImpl implements TaskDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskDaoImpl.class);

    private Database database;

    public TaskDaoImpl(Database database) {
        this.database = database;
    }

    @Override
    public Task get(Long id) {
        try {
            String sql = "SELECT * FROM ATP_task where `id` = ?";

            DataTable dt = database.executeTable(sql, id);

            Task task = null;

            if (dt != null && dt.getRowCount() == 1) {
                DataRow dataRow = dt.getRow(0);
                task = new Task();
                task.setId(dataRow.getLong("id"));
                task.setCreateTime(dataRow.getDateTime("createTime"));
                task.setDesc(dataRow.getString("desc"));
                task.setName(dataRow.getString("name"));
                task.setUserId(dataRow.getLong("userId"));
                task.setSummarys(dataRow.getString("summarys"));
                return task;
            }
        } catch (Exception ex) {
            LOGGER.error("get tasks error" + ex.getMessage());
        }
        return null;
    }
}
