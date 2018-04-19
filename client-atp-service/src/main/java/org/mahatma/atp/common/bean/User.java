package org.mahatma.atp.common.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户实体类,内部将用户信息封装成KV结构。
 */
public class User {

    private String name;

    /**
     * 用户的唯一标识，手机号或者userId?
     */
    private String id;

    private int status;

    private Map<String, String> datas = new HashMap<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Map<String, String> getDatas() {
        return datas;
    }

    public void setDatas(Map<String, String> datas) {
        this.datas = datas;
    }

    public String getData(String key) {
        return datas.get(key);
    }

    public void setData(String key, String value) {
        this.datas.put(key, value);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
