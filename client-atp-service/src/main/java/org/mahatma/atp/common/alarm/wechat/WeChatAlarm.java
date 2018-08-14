package org.mahatma.atp.common.alarm.wechat;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Random;

/**
 * 参考文档： https://work.weixin.qq.com/api/doc#10013 https 请求：http://blog.csdn.net/guozili1/article/details/53995121
 *
 * @author YONGJIAN DUAN
 */
public class WeChatAlarm {
    private static WeChatAlarm weChatAlarm = new WeChatAlarm();
    private static Logger LOGGER = LoggerFactory.getLogger(WeChatAlarm.class);

    public String corpid = "ww8d775452251a7eae";
    public String corpsecret = "k0-tI0NdRQGTtii4ydU5LrkL8oahnKMCoG7luOdZ8uA";
    public int agentId = 1000002;

    private long tokenExpiresTime = 0;
    private String access_token = null;

    public static void main(String[] args) throws IOException {
//        WeChatAlarm weChatAlarm = new WeChatAlarm();
//
//        String token = weChatAlarm.getToken();
//        // System.out.println(token);
//        // weChatAlarm.getAgentList(token, "拨测打卡告警");
//        weChatAlarm.sendMessage(token, "wechat alarm test." + new Random().nextInt());
        send("wechat alarm test." + new Random().nextInt());
    }

    public static void send(String content) throws IOException {
        String token = weChatAlarm.getToken();
        weChatAlarm.sendMessage(token, content);
    }

    public String getToken() throws IOException {
        Calendar nowDate = Calendar.getInstance();
        if (nowDate.getTime().getTime() < tokenExpiresTime - 60000) {
            if (access_token != null) {
                return access_token;
            }
        }
        access_token = null;
        String response = httpsRequest("https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=" + corpid + "&corpsecret=" + corpsecret, "GET", null);
        JSONArray jsonarrayBody = new JSONArray("[" + response + "]");
        boolean res = httpsResponseCheck(response, jsonarrayBody);
        if (res) {
            Calendar getTokenDate = Calendar.getInstance();
            if (!jsonarrayBody.getJSONObject(0).isNull("access_token")) {
                access_token = jsonarrayBody.getJSONObject(0).getString("access_token");
            }
            if (!jsonarrayBody.getJSONObject(0).isNull("expires_in")) {
                int expires_in = jsonarrayBody.getJSONObject(0).getInt("expires_in");
                tokenExpiresTime = getTokenDate.getTime().getTime() + expires_in * 1000;
            }
        } else {
            LOGGER.error("ErrorWeiXin getToken() is failed.");
        }
        return access_token;
    }

    public void sendMessage(String token, String msgStr) throws IOException {
        if (token == null) {
            LOGGER.error("ErrorWeiXin sendMessage() token is null");
            return;
        }
        // \"toparty\" : \"PartyID1|PartyID2\",\"totag\" : \"TagID1 | TagID2\",
        String body = "{\"touser\":\"@all\",\"msgtype\":\"text\",\"agentid\":" + agentId + ",\"text\":{\"content\":\"" + msgStr + "\"},\"safe\":0}";
        String response = httpsRequest("https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + token, "POST", body);
        JSONArray jsonarrayBody = new JSONArray("[" + response + "]");
        boolean res = httpsResponseCheck(response, jsonarrayBody);
        if (res) {
            LOGGER.info("send message success.");
        }
    }

    public void getAgentList(String token, String agentName) throws IOException {
        if (token == null) {
            LOGGER.error("ErrorWeiXin getAgentList() token is null");
            return;
        }
        String response = httpsRequest("https://qyapi.weixin.qq.com/cgi-bin/agent/list?access_token=" + token, "GET", null);
        JSONArray jsonarrayBody = new JSONArray("[" + response + "]");
        boolean res = httpsResponseCheck(response, jsonarrayBody);
        int agentid = -1;
        if (res) {
            JSONArray agentlist = jsonarrayBody.getJSONObject(0).getJSONArray("agentlist");
            for (int i = 0; i < agentlist.length(); i++) {
                JSONObject agent = agentlist.getJSONObject(i);
                String name = agent.getString("name");
                // String square_logo_url = agent.getString("square_logo_url");
                if (name.equals(agentName)) {
                    agentid = agent.getInt("agentid");
                    break;
                }
            }
        } else {
            LOGGER.error("ErrorWeiXin getAgentList() is failed.");
        }
        if (agentid != -1) {
        }
    }

    private boolean httpsResponseCheck(String response, JSONArray jsonarrayBody) {
        int errcode = -1;
        if (!jsonarrayBody.getJSONObject(0).isNull("errcode")) {
            errcode = jsonarrayBody.getJSONObject(0).getInt("errcode");
        }
        if (errcode != 0) {
            return false;
        }
        String errmsg = null;
        if (!jsonarrayBody.getJSONObject(0).isNull("errmsg")) {
            errmsg = jsonarrayBody.getJSONObject(0).getString("errmsg");
        }
        if (!"ok".equals(errmsg)) {
            return false;
        }
        return true;
    }

    private String httpsRequest(String requestUrl, String requestMethod, String outputStr) throws IOException {
        StringBuffer buffer = new StringBuffer();
        URL url = new URL(requestUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod(requestMethod);
        conn.connect();
        // 往服务器端写内容 也就是发起http请求需要带的参数
        if (null != outputStr) {
            OutputStream os = conn.getOutputStream();
            os.write(outputStr.getBytes("utf-8"));
            os.close();
        }

        // 读取服务器端返回的内容
        InputStream is = conn.getInputStream();
        InputStreamReader isr = new InputStreamReader(is, "utf-8");
        BufferedReader br = new BufferedReader(isr);
        String line;
        while ((line = br.readLine()) != null) {
            buffer.append(line);
        }
        return buffer.toString();
    }
}