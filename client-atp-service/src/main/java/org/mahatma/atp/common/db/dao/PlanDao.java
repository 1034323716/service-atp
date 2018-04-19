package org.mahatma.atp.common.db.dao;

import org.mahatma.atp.common.db.bean.Plan;

import java.util.List;

/**
 * Created by lyfx on 17-10-11.
 */

public interface PlanDao {
    boolean isExisted(long planId);

    Plan get(Long planId);

    List<Plan> getActionPlan();
}
