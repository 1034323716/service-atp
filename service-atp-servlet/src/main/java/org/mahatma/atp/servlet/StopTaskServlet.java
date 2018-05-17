package org.mahatma.atp.servlet;

import org.helium.framework.annotations.ServiceSetter;
import org.helium.framework.annotations.ServletImplementation;
import org.helium.http.servlet.HttpMappings;
import org.helium.http.servlet.HttpServlet;
import org.helium.http.servlet.HttpServletContext;
import org.mahatma.atp.service.ControlTest;
import org.mahatma.atp.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by JiYunfei on 17-11-16.
 */
@ServletImplementation(id = "atp:StopTaskServlet")
@HttpMappings(contextPath = "/autoTest/apps", urlPattern = "/stop/task")
public class StopTaskServlet extends HttpServlet {
    private static Logger LOGGER = LoggerFactory.getLogger(StopTaskServlet.class);

    @ServiceSetter
    private ControlTest controlTest;

    @Override
    public void process(HttpServletContext ctx) throws Exception {
        HttpServletRequest request = ctx.getRequest();
        HttpServletResponse response = ctx.getResponse();

        String taskResultIdStr = request.getParameter("id");
        Long taskResultId = Long.valueOf(taskResultIdStr);
        if (taskResultId == null || "".equals(taskResultId) || !controlTest.exist(taskResultId)) {
            HttpUtil.sendResponse("404", "BAD_REQUEST", "", response);
            return;
        }
        try {
            controlTest.stop(taskResultId);
        } catch (Exception e) {
            LOGGER.error("stop task error , task result id is {} , exception is {}", taskResultId, e);
            HttpUtil.sendResponse("500", "INNER_SERVER_ERROR", "", response);
        }
        HttpUtil.sendResponse("200", "SUCCESS", "", response);
        return;
    }
}
