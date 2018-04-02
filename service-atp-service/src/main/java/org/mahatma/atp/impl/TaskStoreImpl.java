package org.mahatma.atp.impl;

import org.helium.database.DataTable;
import org.helium.database.Database;
import org.helium.framework.annotations.FieldSetter;
import org.helium.framework.annotations.ServiceImplementation;
import org.mahatma.atp.dao.TaskStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

/**
 * Created by JiYunfei on 17-10-12.
 */
@ServiceImplementation
public class TaskStoreImpl implements TaskStore {

    private static Logger LOGGER = LoggerFactory.getLogger(TaskLogStoreImpl.class);

    @FieldSetter("URCS_ATPDB")
    private Database atpDB;

    @Override
    public boolean isExisted(long taskId) {
        String sql = "select count(*) from ATP_task where `id` = ?";
        int i = 0;
        try {
            DataTable dataTable = atpDB.executeTable(sql, taskId);
            if (dataTable != null) {
                if (dataTable.getRows().size() > 0) {
                    i = dataTable.getRows().get(0).getInt(1);
                }
            }
        } catch (SQLException e) {
            LOGGER.error("taskId isExisted error!", e);
        }
        return i != 0;
    }
}
