package org.mahatma.atp.plan;

import org.helium.database.Database;
import org.helium.framework.annotations.FieldSetter;
import org.helium.framework.annotations.ServiceImplementation;
import org.helium.framework.annotations.ServiceSetter;
import org.mahatma.atp.conf.util.RunTaskUtil;
import org.mahatma.atp.dao.TaskLogStore;
import org.mahatma.atp.entity.RunType;

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
    public void process(PlanBean planBean) {
        Long taskId = planBean.getPlan().getTaskId();
        Long taskResultId = RunTaskUtil.prepare(taskId, taskLogStore, planBean.getPlan().getId());
        RunTaskUtil.run(taskId, taskLogStore, taskResultId, null, atpDB, RunType.RunPlan);
    }
}
