package org.mahatma.atp.service;

import org.helium.framework.annotations.ServiceInterface;

/**
 * Created by JiYunfei on 18-4-2.
 */
@ServiceInterface(id = "atp:ControlTaskService")
public interface ControlTaskService {
    void stop(Long taskResultId);

    void add(Long taskResultId, int processId);

    void remove(Long taskResultId);

    boolean exist(Long taskResultId);
}
