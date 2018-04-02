package org.mahatma.atp.servlet;

import org.helium.framework.annotations.ServletImplementation;
import org.helium.http.servlet.HttpMappings;
import org.helium.http.servlet.HttpServlet;
import org.helium.http.servlet.HttpServletContext;
import org.mahatma.atp.conf.util.CronUtil;
import org.mahatma.atp.conf.util.SendResponseUtil;
import org.mahatma.atp.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;

/**
 * Created by JiYunfei on 17-11-7.
 */
@ServletImplementation(id = "atp:VerifyCronServlet")
@HttpMappings(contextPath = "/autoTest/apps", urlPattern = "/verifyCron")
public class VerifyCronServlet extends HttpServlet {
    private static Logger LOGGER = LoggerFactory.getLogger(VerifyCronServlet.class);

    @Override
    public void process(HttpServletContext ctx) throws Exception {
        HttpServletRequest request = ctx.getRequest();
        HttpServletResponse response = ctx.getResponse();

        String cron = request.getParameter("cron");
        if (cron == null || "".equals(cron)) {
            HttpUtil.sendResponse("404", "BAD_REQUEST", "", response);
            return;
        } else {
            Boolean valid;
            try {
                valid = CronUtil.isValidExpression(cron);
            } catch (ParseException e) {
                HttpUtil.sendResponse("400", "ERROR", "", response);
                return;
            }
            if (valid != null && valid) {
                HttpUtil.sendResponse("200", "SUCCESS", "", response);
                return;
            } else {
                HttpUtil.sendResponse("400", "ERROR", "", response);
                return;
            }
        }
    }
}