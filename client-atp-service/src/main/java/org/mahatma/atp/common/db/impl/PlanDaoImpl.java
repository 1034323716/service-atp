package org.mahatma.atp.common.db.impl;

import org.helium.database.DataRow;
import org.helium.database.DataTable;
import org.helium.database.Database;
import org.mahatma.atp.common.db.bean.Plan;
import org.mahatma.atp.common.db.dao.PlanDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JiYunfei on 17-10-17.
 */
public class PlanDaoImpl implements PlanDao {

    private static final Logger logger = LoggerFactory.getLogger(PkgDaoImpl.class);

    private Database database;

    public PlanDaoImpl(Database database) {
        this.database = database;
    }

    @Override
    public boolean isExisted(long planId) {
        String sql = "select count(*) from ATP_plan where `id` = ?";
        int i = 0;
        try {
            DataTable dataTable = database.executeTable(sql, planId);
            if (dataTable != null) {
                if (dataTable.getRows().size() > 0) {
                    i = dataTable.getRows().get(0).getInt(1);
                }
            }
        } catch (SQLException e) {
            logger.error("planId isExisted error!", e);
        }
        return i != 0;
    }

    @Override
    public Plan get(Long planId) {
        try {
            String sql = "SELECT * FROM ATP_plan where `id` = ?";
            DataTable dt = database.executeTable(sql, planId);
            Plan plan = new Plan();
            if (dt != null && dt.getRowCount() == 1) {
                DataRow row = dt.getRow(0);
                plan.setId(row.getLong("id"));
                plan.setName(row.getString("name"));
                plan.setDesc(row.getString("desc"));
                plan.setCreateTime(row.getDateTime("createTime"));
                plan.setUserId(row.getLong("userId"));
                plan.setCronExpression(row.getString("cronExpression"));
                plan.setTaskId(row.getLong("taskId"));
                plan.setState(row.getInt("state"));
                plan.setAlarm(row.getBoolean("isAlarm"));
                return plan;
            }
        } catch (Exception ex) {
            logger.error("get plan exception" + ex.getMessage());
        }
        return null;
    }

    @Override
    public List<Plan> getActionPlan() {
        List<Plan> plans = new ArrayList<>();
        try {
            String sql = "SELECT * FROM ATP_plan where `state` = ? or `state` = ?";
            DataTable dt = database.executeTable(sql, 1, 2);
            if (dt != null && dt.getRowCount() > 0) {
                for (DataRow row : dt.getRows()) {
                    Plan plan = new Plan();
                    plan.setId(row.getLong("id"));
                    plan.setName(row.getString("name"));
                    plan.setDesc(row.getString("desc"));
                    plan.setCreateTime(row.getDateTime("createTime"));
                    plan.setUserId(row.getLong("userId"));
                    plan.setCronExpression(row.getString("cronExpression"));
                    plan.setTaskId(row.getLong("taskId"));
                    plan.setState(row.getInt("state"));
                    plan.setAlarm(row.getBoolean("isAlarm"));
                    plans.add(plan);
                }
            }
        } catch (Exception ex) {
            logger.error("get action plan exception" + ex.getMessage());
        }
        return plans;
    }
}
