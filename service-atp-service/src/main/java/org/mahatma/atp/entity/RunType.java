package org.mahatma.atp.entity;

import com.feinno.superpojo.type.EnumInteger;

/**
 * Created by JiYunfei on 18-3-27.
 */
public enum RunType implements EnumInteger {
    RunPlan(1), RunTask(2);

    int value;

    RunType(int value) {
        this.value = value;
    }

    @Override
    public int intValue() {
        return this.value;
    }
}
