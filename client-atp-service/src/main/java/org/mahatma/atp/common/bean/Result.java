package org.mahatma.atp.common.bean;


import com.feinno.util.Combo3;
import org.mahatma.atp.common.engine.spi.RuntimeWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 测试结果输出
 */
public class Result {

    /**
     * 结果返回码
     */
    private int code;

    /**
     * 中间值存储表
     */
    private List<Combo3<Integer, byte[], byte[]>> stepData = new ArrayList<>();

    /**
     * 结果描述
     */
    private String desc = "no set desc";

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
        for (Combo3<Integer, byte[], byte[]> data : stepData) {
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

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Combo3<Integer, byte[], byte[]> data : stepData) {
            stringBuilder.append("    " + data.getV1() + ":");
            stringBuilder.append(new String(data.getV2()) + ":");
            stringBuilder.append(new String(data.getV3()));
            stringBuilder.append("\n");
        }
        return "Result{" + "\n" +
                "  code=" + code + "\n" +
                "  desc='" + desc + "\n" +
                "  stepData:" + "\n" +
                stringBuilder.toString() +
                "}";
    }
}
