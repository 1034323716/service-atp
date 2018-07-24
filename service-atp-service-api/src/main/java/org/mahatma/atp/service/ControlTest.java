package org.mahatma.atp.service;

import org.helium.framework.annotations.ServiceInterface;
import org.mahatma.atp.common.util.entity.ControlPkg;

import java.util.List;

/**
 * Created by JiYunfei on 18-4-2.
 */
@ServiceInterface(id = "atp:ControlTest")
public interface ControlTest {
    void stop(long taskResultId);

    void add(long taskResultId, ControlPkg controlPkg);

    void remove(long taskResultId);

    boolean exist(long taskResultId);

    boolean planIsRun(long planId);

    /**
     * 这个计划正在运行着的taskResultId
     **/
    List<Long> planRuningId(long planId);

    void stopPlan(long planId);
}
