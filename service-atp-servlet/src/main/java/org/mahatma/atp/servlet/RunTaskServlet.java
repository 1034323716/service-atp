package org.mahatma.atp.servlet;

import org.helium.database.Database;
import org.helium.framework.annotations.FieldSetter;
import org.helium.framework.annotations.ServiceSetter;
import org.helium.framework.annotations.ServletImplementation;
import org.helium.http.servlet.HttpMappings;
import org.helium.http.servlet.HttpServlet;
import org.helium.http.servlet.HttpServletContext;
import org.mahatma.atp.conf.util.RunTaskUtil;
import org.mahatma.atp.dao.TaskLogStore;
import org.mahatma.atp.dao.TaskStore;
import org.mahatma.atp.entity.RunType;
import org.mahatma.atp.service.ControlTest;
import org.mahatma.atp.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by JiYunfei on 17-9-11.
 */
@ServletImplementation(id = "atp:RunTaskServlet")
@HttpMappings(contextPath = "/autoTest/apps", urlPattern = "/run/task")
public class RunTaskServlet extends HttpServlet {
    private static Logger LOGGER = LoggerFactory.getLogger(RunTaskServlet.class);

    @ServiceSetter
    private TaskLogStore taskLogStore;
    @ServiceSetter
    private TaskStore taskStore;
    @ServiceSetter
    private ControlTest controlTest;
    @FieldSetter("URCS_ATPDB")
    private Database atpDB;

    @Override
    public void process(HttpServletContext ctx) throws Exception {
        HttpServletRequest request = ctx.getRequest();
        HttpServletResponse response = ctx.getResponse();

        Long taskId = Long.parseLong(request.getParameter("id"));
        if (taskId == null || taskId < 1 || !taskStore.isExisted(taskId)) {
            HttpUtil.sendResponse("400", "BAD_REQUEST", "", response);
            return;
        } else {
            long taskResultId = RunTaskUtil.prepare(taskId, taskLogStore, null);
            HttpUtil.sendResponse("202", "ACCEPT", String.valueOf(taskResultId), response);
            RunTaskUtil.run(taskId, 0L, taskLogStore, taskResultId, controlTest, atpDB, RunType.RunTask);
        }
    }
}
