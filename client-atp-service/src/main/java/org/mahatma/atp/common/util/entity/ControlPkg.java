package org.mahatma.atp.common.util.entity;

import com.feinno.superpojo.SuperPojo;
import com.feinno.superpojo.annotation.Field;

public class ControlPkg extends SuperPojo {
    @Field(id = 1)
    private long taskResultId;
    @Field(id = 2)
    private int pid;
    // 若不是定时执行，这个id为0
    @Field(id = 3)
    private long planId;
    @Field(id = 4)
    private long taskId;
    // 状态，为1时代表启动，为0是代表结束
    @Field(id = 5)
    private int status;

    public long getTaskResultId() {
        return taskResultId;
    }

    public void setTaskResultId(long taskResultId) {
        this.taskResultId = taskResultId;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public long getPlanId() {
        return planId;
    }

    public void setPlanId(long planId) {
        this.planId = planId;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}