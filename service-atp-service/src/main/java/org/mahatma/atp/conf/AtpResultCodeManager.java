package org.mahatma.atp.conf;

import com.feinno.superpojo.SuperPojo;
import com.feinno.superpojo.annotation.Childs;
import com.feinno.superpojo.annotation.Entity;
import com.feinno.superpojo.annotation.Field;

import java.util.HashMap;
import java.util.Map;

@Entity(name = "resultCode")
public class AtpResultCodeManager extends SuperPojo {

    private static AtpResultCodeManager INSTANCE;

    /**
     * 初始化服务来为这个配置类初始化
     *
     * @param config
     */
    public static void initResultCodeManager(AtpResultCodeManager config) {
        INSTANCE = config;
    }

    public static ResultCode getReturnCodeDoc(ResultCodeParam resultCodeParam) {
        return INSTANCE.getResultCodeDocMap().get(resultCodeParam.toString());
    }

    @Childs(id = 1, parent = "resultCodeDocs", useKeyName = true)
    private Map<String, ResultCode> resultCodeDocMap;

    public Map<String, ResultCode> getResultCodeDocMap() {
        return resultCodeDocMap;
    }

    public void setResultCodeDocMap(Map<String, ResultCode> resultCodeDocMap) {
        this.resultCodeDocMap = resultCodeDocMap;
    }

    public static class ResultCode extends SuperPojo {
        @Field(id = 1)
        private int code;

        @Field(id = 2)
        private String doc;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDoc() {
            return doc;
        }

        public void setDoc(String doc) {
            this.doc = doc;
        }
    }

    public static void main(String[] args) {
        AtpResultCodeManager atpResultCodeManager = new AtpResultCodeManager();
        Map<String, ResultCode> test = new HashMap();
        ResultCode resultCode = new ResultCode();
        resultCode.setCode(100);
        resultCode.setDoc("123");
        test.put("test", resultCode);
        atpResultCodeManager.setResultCodeDocMap(test);
        System.out.println(atpResultCodeManager.toXmlString());
    }
}
