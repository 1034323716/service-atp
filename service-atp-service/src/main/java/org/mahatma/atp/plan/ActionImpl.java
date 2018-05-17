package org.mahatma.atp.plan;

import org.helium.database.Database;
import org.helium.framework.annotations.FieldSetter;
import org.helium.framework.annotations.ServiceImplementation;
import org.helium.framework.annotations.ServiceSetter;
import org.mahatma.atp.conf.util.RunTaskUtil;
import org.mahatma.atp.dao.TaskLogStore;
import org.mahatma.atp.entity.RunType;
import org.mahatma.atp.service.ControlTest;

/**
 * Created by JiYunfei on 17-9-19.
 */
@ServiceImplementation
public class ActionImpl implements Action {

    @ServiceSetter
    private TaskLogStore taskLogStore;
    @FieldSetter("URCS_ATPDB")
    private Database atpDB;

    @Override
    public void process(PlanBean planBean, ControlTest controlTest) {
        Long taskId = planBean.getPlan().getTaskId();
        long planId = planBean.getPlan().getId();
        Long taskResultId = RunTaskUtil.prepare(taskId, taskLogStore, planBean.getPlan().getId());
        RunTaskUtil.run(taskId, planId, taskLogStore, taskResultId, controlTest, atpDB, RunType.RunPlan);
    }
}
