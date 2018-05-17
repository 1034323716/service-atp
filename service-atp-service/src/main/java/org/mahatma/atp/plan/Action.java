package org.mahatma.atp.plan;

import org.helium.framework.annotations.ServiceInterface;
import org.mahatma.atp.service.ControlTest;

/**
 * Created by lyfx on 17-9-11.
 */
@ServiceInterface(id = "plan:Action")
public interface Action {

    /**
     * 具体的处理
     */
    void process(PlanBean planBean, ControlTest controlTest);

}
