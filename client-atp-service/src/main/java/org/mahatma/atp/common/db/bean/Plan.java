package org.mahatma.atp.common.db.bean;

import java.util.Date;

/**
 * -- auto-generated definition
 * CREATE TABLE ATP_plan
 * (
 * id             BIGINT AUTO_INCREMENT
 * COMMENT 'ID'
 * PRIMARY KEY,
 * name           VARCHAR(256) DEFAULT ''             NOT NULL
 * COMMENT '名称',
 * `desc`         VARCHAR(256) DEFAULT ''             NOT NULL
 * COMMENT '描述',
 * createTime     TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
 * COMMENT '创建时间',
 * userId         INT                                 NOT NULL
 * COMMENT '所属用户',
 * cronExpression VARCHAR(256) DEFAULT ''             NOT NULL
 * COMMENT 'cron表达式',
 * taskId         BIGINT                              NULL
 * COMMENT '任务id',
 * state          INT                                 NOT NULL
 * COMMENT '计划状态 0：未激活  1：已激活'
 * );
 * <p>
 * <p>
 * <p>
 * Created by JiYunfei on 17-10-16.
 */
public class Plan {
    private Long id;
    private String name;
    private String desc;
    private Date createTime;
    private Long userId;
    private String cronExpression;
    private Long taskId;
    private int state;
    private boolean alarm;

    public boolean isAlarm() {
        return alarm;
    }

    public void setAlarm(boolean alarm) {
        this.alarm = alarm;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
