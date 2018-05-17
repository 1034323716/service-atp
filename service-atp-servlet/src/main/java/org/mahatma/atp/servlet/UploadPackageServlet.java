package org.mahatma.atp.servlet;

import org.helium.framework.annotations.ServletImplementation;
import org.helium.http.servlet.HttpMappings;
import org.helium.http.servlet.HttpServlet;
import org.helium.http.servlet.HttpServletContext;
import org.mahatma.atp.conf.AtpEnvConfiguration;
import org.mahatma.atp.conf.arg.PackageSetting;
import org.mahatma.atp.conf.util.FilePathUtil;
import org.mahatma.atp.conf.util.ResourceUtil;
import org.mahatma.atp.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by JiYunfei on 17-9-11.
 */
@ServletImplementation(id = "atp:UploadPackageServlet")
@HttpMappings(contextPath = "/autoTest/apps", urlPattern = "/upload")
public class UploadPackageServlet extends HttpServlet {
    private static Logger LOGGER = LoggerFactory.getLogger(UploadPackageServlet.class);

    @Override
    public void process(HttpServletContext ctx) throws Exception {
        HttpServletRequest request = ctx.getRequest();
        HttpServletResponse response = ctx.getResponse();

        PackageSetting packageSetting;
        String filePath = AtpEnvConfiguration.getInstance().getTempFolder() + request.getParameter("location");;

        ResourceUtil resourceUtils = new ResourceUtil(filePath);
        if (!FilePathUtil.checkPathExist(filePath)) {
            HttpUtil.sendResponse("404", "FileNotFound", "", response);
            return;
        } else {
            try {
                resourceUtils.init();
            } catch (Exception e) {
                LOGGER.warn("resourceUtils init error:" + e.getMessage());
                HttpUtil.sendResponse("500", "INTERNAL_SERVER_ERROR", "", response);
                return;
            }
            packageSetting = resourceUtils.getPackageSetting();
            if (packageSetting != null) {
                HttpUtil.sendResponse("200", "success", packageSetting.toXmlString(), response);
                return;
            }
        }
    }
}
