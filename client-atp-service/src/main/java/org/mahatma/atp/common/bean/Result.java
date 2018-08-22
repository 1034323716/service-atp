package org.mahatma.atp.common.bean;

import com.feinno.util.Combo3;
import org.mahatma.atp.common.engine.spi.RuntimeWatch;
import org.mahatma.atp.common.util.ThreadLocalResultDateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 测试结果输出
 *
 * @author JiYunfei
 */
public class Result {

    /**
     * 结果返回码
     */
    private int code = 4004;

    /**
     * 中间值存储表
     */
    private List<Combo3<Integer, byte[], byte[]>> stepData = new ArrayList<>();

    /**
     * 结果描述
     */
    private String desc = "默认描述，用例中未添加描述";

    private RuntimeWatch watch;

    private AtomicInteger putStep;

    protected Result(RuntimeWatch watch) {
        this.watch = watch;
        putStep = new AtomicInteger(0);
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void putStep(int i, String request, String response) {
        request = "【Time:" + ThreadLocalResultDateUtil.formatDate(new Date()) + "】" + request;
        putStep.incrementAndGet();
        if (request == null) {
            request = "";
        }
        if (response == null) {
            response = "";
        }
        putStep(i, request.getBytes(), response.getBytes());
    }

    public void putStep(int i, byte[] request, byte[] response) {
        putStep.incrementAndGet();
        Combo3<Integer, byte[], byte[]> combo3 = new Combo3<>(i, request, response);
        watch.into(combo3);
        stepData.add(combo3);
    }

    public void putStep(byte[] request, byte[] response) {
        Combo3<Integer, byte[], byte[]> combo3 = new Combo3<>(putStep.incrementAndGet(), request, response);
        watch.into(combo3);
        stepData.add(combo3);
    }

    public void putStep(String request, String response) {
        request = "【Time:" + ThreadLocalResultDateUtil.formatDate(new Date()) + "】" + request;
        if (request == null) {
            request = "";
        }
        if (response == null) {
            response = "";
        }
        putStep(putStep.incrementAndGet(), request.getBytes(), response.getBytes());
    }

    public String getStepDataString() {
        StringBuilder stringBuilder = new StringBuilder();
        List<Combo3<Integer, byte[], byte[]>> temp = new ArrayList<>();
        temp.addAll(stepData);
        for (Combo3<Integer, byte[], byte[]> data : temp) {
            stringBuilder.append("◇步骤 : " + data.getV1());
            stringBuilder.append("\n\n");
            stringBuilder.append("\t>请求 : " + new String(data.getV2()));
            stringBuilder.append("\n\n");
            stringBuilder.append("\t>响应 : " + new String(data.getV3()));
            stringBuilder.append("\n\n");
        }
        return stringBuilder.toString();
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public int getCode() {
        return code;
    }

    public List<Combo3<Integer, byte[], byte[]>> getStepData() {
        return stepData;
    }

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        List<Combo3<Integer, byte[], byte[]>> temp = new ArrayList<>();
        temp.addAll(stepData);
        for (Combo3<Integer, byte[], byte[]> data : temp) {
            stringBuffer.append("    " + formatStep(data));
        }
        return "Result{" + "\n" +
                "  code=" + code + "\n" +
                "  desc='" + desc + "\n" +
                "  stepData:" + "\n" +
                stringBuffer.toString() +
                "}";
    }

    public static String formatStep(Combo3<Integer, byte[], byte[]> data) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(data.getV1() + ":");
        stringBuffer.append(new String(data.getV2()) + ":");
        stringBuffer.append(new String(data.getV3()));
        stringBuffer.append("\n");
        return stringBuffer.toString();
    }
}
