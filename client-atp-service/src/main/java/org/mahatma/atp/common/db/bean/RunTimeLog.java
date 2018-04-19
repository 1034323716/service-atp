package org.mahatma.atp.common.db.bean;

/**
 *
 * create table ATP_log
 (
 id bigint auto_increment comment 'id'
 primary key,
 taskResultId bigint null comment 'taskResultId',
 log blob null comment '运行日志'
 )
 ;


 * Created by lyfx on 17-10-11.
 */

public class RunTimeLog {
    private long id;
    private long taskResultId;
    private String log;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTaskResultId() {
        return taskResultId;
    }

    public void setTaskResultId(long taskResultId) {
        this.taskResultId = taskResultId;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }
}
