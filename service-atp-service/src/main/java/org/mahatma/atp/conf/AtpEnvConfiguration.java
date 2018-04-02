package org.mahatma.atp.conf;

import com.feinno.superpojo.SuperPojo;
import com.feinno.superpojo.annotation.Entity;
import com.feinno.superpojo.annotation.Field;

@Entity(name = "atpEnvConfig")
public class AtpEnvConfiguration extends SuperPojo {
    private static AtpEnvConfiguration INSTANCE;

    /**
     * 初始化服务来为这个配置类初始化
     *
     * @param config
     */
    public static void initRegConfiguration(AtpEnvConfiguration config) {
        INSTANCE = config;
    }

    public static AtpEnvConfiguration getInstance() {
        return INSTANCE;
    }

    @Field(id = 1)
    private String tempFolder;

    public String getTempFolder() {
        return tempFolder;
    }

    public void setTempFolder(String tempFolder) {
        this.tempFolder = tempFolder;
    }
}
