package org.mahatma.atp.plan;

import org.helium.framework.spi.task.CronExpression;
import org.mahatma.atp.common.db.bean.Plan;

import java.text.ParseException;

/**
 * Created by JiYunfei on 17-10-18.
 */
public class PlanBean {
    /**
     * 触发时间
     * yyyy-MM-dd HH:mm:ss
     */
    private CronExpression triggerTime;

    /**
     * ATP_plan中对应的bean
     */
    private Plan plan;
    private Action action;

    public PlanBean(Plan plan, Action action) throws ParseException {
        this.triggerTime = new CronExpression(plan.getCronExpression());
        this.plan = plan;
        this.action = action;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public CronExpression getTriggerTime() {
        return triggerTime;
    }

    public void setTriggerTime(CronExpression triggerTime) {
        this.triggerTime = triggerTime;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }
}
