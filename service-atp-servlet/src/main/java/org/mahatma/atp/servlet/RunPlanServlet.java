package org.mahatma.atp.servlet;

import org.helium.database.Database;
import org.helium.framework.annotations.FieldSetter;
import org.helium.framework.annotations.ServiceSetter;
import org.helium.framework.annotations.ServletImplementation;
import org.helium.http.servlet.HttpMappings;
import org.helium.http.servlet.HttpServlet;
import org.helium.http.servlet.HttpServletContext;
import org.mahatma.atp.common.db.dao.PlanDao;
import org.mahatma.atp.common.db.impl.PlanDaoImpl;
import org.mahatma.atp.plan.Action;
import org.mahatma.atp.plan.PlanBean;
import org.mahatma.atp.plan.RunPlan;
import org.mahatma.atp.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;

/**
 * 执行计划任务，也就是执行定时任务
 * <p>
 * Created by JiYunfei on 17-9-11.
 */
@ServletImplementation(id = "atp:RunPlanServlet")
@HttpMappings(contextPath = "/autoTest/apps", urlPattern = "/run/plan")
public class RunPlanServlet extends HttpServlet {
    private static Logger LOGGER = LoggerFactory.getLogger(RunPlanServlet.class);

    @FieldSetter("URCS_ATPDB")
    private Database atpDB;
    @ServiceSetter
    private Action action;
    @ServiceSetter
    private RunPlan runPlan;

    @Override
    public void process(HttpServletContext ctx) throws Exception {
        HttpServletRequest request = ctx.getRequest();
        HttpServletResponse response = ctx.getResponse();
        PlanDao planDao = new PlanDaoImpl(atpDB);

        Long planId;
        planId = Long.parseLong(request.getParameter("id"));
        if (planId == null || planId < 1 || !planDao.isExisted(planId)) {
            HttpUtil.sendResponse("400", "BAD_REQUEST", "", response);
            return;
        } else {
            try {
                runPlan.registerPlan(new PlanBean(planDao.get(planId), action));
                HttpUtil.sendResponse("202", "ACCEPT", "", response);
                return;
            } catch (ParseException e) {
                HttpUtil.sendResponse("400", "CRON_ERROR", "", response);
                return;
            }
        }
    }
}
