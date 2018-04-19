package test.org.mahatma.atp.common;

import com.feinno.threading.Future;
import com.feinno.urcs.http.HttpClientApi;
import org.asynchttpclient.AsyncHttpClient;
import org.helium.http.client.HttpClient;
import org.helium.http.client.HttpClientRequest;
import org.helium.http.client.HttpClientResponse;
import org.mahatma.atp.common.Test;
import org.mahatma.atp.common.annotations.TestModule;
import org.mahatma.atp.common.annotations.TestVarSetter;
import org.mahatma.atp.common.annotations.UserSetter;
import org.mahatma.atp.common.bean.Result;
import org.mahatma.atp.common.bean.Session;
import org.mahatma.atp.common.bean.User;
import org.mahatma.atp.common.type.StackType;

import java.util.HashMap;
import java.util.Map;

@TestModule
public class FirstTest implements Test {

    @UserSetter(name = "user1")
    private User user1;

    @UserSetter(name = "user2")
    private User user2;

    //IP(接入地址配置)
    @TestVarSetter(name = "IP", cname = "接入地址配置")
    private String ip;

    @TestVarSetter(name = "IS", cname = "是否成功")
    private boolean isSuccess = true;

    @Override
    public Result process(Session session) {
        System.out.println(ip);
        System.out.println(isSuccess);
        //导航+登陆
        HttpClient httpClient = new HttpClient();
        try {
            String reqUrl = "http://192.168.143.25:8088/register/v1/tel:+8618640898426/Provisioning";

            HttpClientRequest request = HttpClient.createHttpRequest("POST", reqUrl);
            request.setContent("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                    "\n" +
                    "<args>\n" +
                    "  <epid>PC;version=6.1.8;uuid=ebe014c1af4480f8da42484e5093b4c9;os=win;type=1;provider=1.5</epid>\n" +
                    "  <loginType>3</loginType>\n" +
                    "  <msgid>e24bad6e-c41a-4241-ac24-e432b46eafbd</msgid>\n" +
                    "  <responseTime/>\n" +
                    "  <sourceid>2</sourceid>\n" +
                    "  <appid>4</appid>\n" +
                    "  <cnonce>25244</cnonce>\n" +
                    "  <presenceBasicDesc>400</presenceBasicDesc>\n" +
                    "  <random>NGY3ZDdiOWU0YzBlNGRkMw==</random>\n" +
                    "  <oemTag>xmcx.fxgw.pco1</oemTag>\n" +
                    "  <quickLogin>2</quickLogin>\n" +
                    "  <requestTime>1500887194000</requestTime>\n" +
                    "  <token/>\n" +
                    "  <computerName>CHENMINGHUIFX</computerName>\n" +
                    "</args>");
            Map<String, String> headerMap = new HashMap<>();
            headerMap.put("Content-Type", "application/xml;charset=UTF-8");

            for (String key : headerMap.keySet()) {
                request.setHeader(key, headerMap.get(key));
            }

            Future<HttpClientResponse> httpClientResponseFuture = httpClient.sendData(request);

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }


//        HttpClientApi httpClientApi = session.getStack(HttpClientApi.class);
//        AsyncHttpClient asyncHttpClient = httpClientApi.currentAsyncHttpClient();


//        CyclicBarrier barrier = new CyclicBarrier();

//        session.createUAS(user2, new TestLister() {
//
//            @Override
//            public Result process(User user, Session session) {
//
//                Result result = new Result();
//                return result;
//            }
//        });

//        http.setLister(new TestLister(){
//            @Override
//            public Result process(User user, Session session) {
//
//                barrier.go();
//                return null;
//            }
//        });

//        Result result = new Result();

//        result.putStep(1, "RequestData", "ResponseData");

//        result.setCode(200);

//        barrier.wait();

//        return result;

        return null;
    }
}
