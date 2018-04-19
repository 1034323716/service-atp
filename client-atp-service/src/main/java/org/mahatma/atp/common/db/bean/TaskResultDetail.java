package org.mahatma.atp.common.db.bean;

import java.util.Date;

/**
 *
 -- auto-generated definition
 create table ATP_taskResultDetail
 (
 id bigint auto_increment comment 'id'
 primary key,
 taskResultId bigint not null comment 'taskResultId',
 code varchar(256) null comment '运行结果返回码',
 `desc` varchar(256) null comment '运行结果描述',
 createTime datetime null comment '测试时间',
 finishTime datetime null comment '结束时间',
 record blob null comment '运行中间值保存(协议栈提供埋点接口)',
 tcsId bigint null comment '用例集Id',
 tcId bigint null comment '用例Id',
 state int not null comment '这条用例的运行状态 0：运行中  1：已完成'
 )
 ;


 * Created by lyfx on 17-10-11.
 */
public class TaskResultDetail {
    private long id;
    private String code;
    private String desc;
    private Date createTime;
    private Date finishTime;
    private long taskResultId;
    private long tcsId;
    private long tcId;
    private String record;
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

    public long getTaskResultId() {
        return taskResultId;
    }

    public void setTaskResultId(long taskResultId) {
        this.taskResultId = taskResultId;
    }

    public long getTcsId() {
        return tcsId;
    }

    public void setTcsId(long tcsId) {
        this.tcsId = tcsId;
    }

    public long getTcId() {
        return tcId;
    }

    public void setTcId(long tcId) {
        this.tcId = tcId;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
