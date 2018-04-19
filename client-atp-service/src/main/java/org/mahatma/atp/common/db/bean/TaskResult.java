package org.mahatma.atp.common.db.bean;

import java.util.Date;

/**
 *
 -- auto-generated definition
 create table ATP_taskResult
 (
 id bigint auto_increment comment 'id'
 primary key,
 code varchar(256) null comment '运行结果返回码',
 `desc` varchar(256) null comment '运行结果描述',
 createTime datetime null comment '测试时间',
 finishTime datetime null comment '结束时间',
 taskId bigint not null comment '任务Id',
 planId bigint null comment '计划id',
 state int not null comment '这条task的运行状态 0：运行中  1：已完成'
 )
 ;
 * Created by lyfx on 17-10-11.
 */
public class TaskResult {
    private long id;
    private String code;
    private String desc;
    private Date createTime;
    private Date finishTime;
    private long taskId;
    private long planId;

    /**
     * task运行状态
     * 0:运行中
     * 1:已完成
     */
    private int state;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public long getPlanId() {
        return planId;
    }

    public void setPlanId(long planId) {
        this.planId = planId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
