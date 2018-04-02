package org.mahatma.atp.conf.util;

import com.feinno.superpojo.util.StringUtils;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by JiYunfei on 17-9-25.
 */
public class SendResponseUtil {
    private static Logger LOGGER = LoggerFactory.getLogger(SendResponseUtil.class);

    //发送返回的json
    public static void sendResponse(String code, String desc, String content, HttpServletResponse response) throws Exception {
        JsonObject jsonObject = createResponse(code, desc, content);
        sendResponse(jsonObject, response);
    }

    //发送返回的json
    public static void sendResponse(JsonObject jsonObject, HttpServletResponse response) throws Exception {
        response.getOutputStream().write(jsonObject.toString().getBytes("UTF-8"));
        response.getOutputStream().close();
        LOGGER.info(jsonObject.toString());
    }

    //组装返回的json
    public static JsonObject createResponse(String code, String desc, String content) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("code", code);
        jsonObject.addProperty("desc", desc);
        if (!StringUtils.isNullOrEmpty(content)) {
            jsonObject.addProperty("content", content);
        }
        return jsonObject;
    }
}
