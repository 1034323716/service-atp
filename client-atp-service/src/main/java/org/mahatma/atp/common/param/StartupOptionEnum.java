package org.mahatma.atp.common.param;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JiYunfei on 17-10-11.
 */
public enum StartupOptionEnum {
    RETEST, TASKID, TASKRESULTID, RUNTYPE, PLANID;

    /**
     * 是否开启当前操作
     */
    private boolean isEnable = false;

    /**
     * 当前操作参数
     */
    private List<String> args = new ArrayList<String>();

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
