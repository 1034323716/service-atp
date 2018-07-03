package org.mahatma.atp.plan;

import org.helium.framework.annotations.ServiceInterface;

/**
 * Created by JiYunfei on 17-10-18.
 */
@ServiceInterface(id = "atp:RunPlan")
public interface RunPlan {
    void registerPlan(PlanBean planBean);

    void removePlan(PlanBean planBean);

    boolean existPlan(PlanBean planBean);

    boolean pkgInPlan(Long pkgId);
}
