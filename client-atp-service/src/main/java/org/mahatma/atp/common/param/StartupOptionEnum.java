package org.mahatma.atp.common.param;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JiYunfei
 * @date 17-10-11
 */
public enum StartupOptionEnum {
    /**
     * 失败时重试
     */
    RETEST,
    /**
     * 任务ID
     */
    TASKID,
    /**
     * 任务结果ID
     */
    TASKRESULTID,
    /**
     * 运行类型:手动执行任务；自动执行计划
     */
    RUNTYPE,
    /**
     * 计划ID
     */
    PLANID;

    /**
     * 是否开启当前操作
     */
    private boolean isEnable = false;

    /**
     * 当前操作参数
     */
    private List<String> args = new ArrayList<>();

    public void setEnable(boolean isEnable) {
        this.isEnable = isEnable;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void addArgs(String args) {
        this.args.add(args);
    }

    public List<String> getArgs() {
        return args;
    }

}
