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
    public static void initAtpConfiguration(AtpEnvConfiguration config) {
        INSTANCE = config;
    }

    public static AtpEnvConfiguration getInstance() {
        return INSTANCE;
    }

    @Field(id = 1)
    private String tempFolder;
    @Field(id = 2)
    private int serverSocketPort;
    @Field(id = 3)
    private boolean capturePkg;

    public boolean isCapturePkg() {
        return capturePkg;
    }

    public void setCapturePkg(boolean capturePkg) {
        this.capturePkg = capturePkg;
    }

    public int getServerSocketPort() {
        return serverSocketPort;
    }

    public void setServerSocketPort(int serverSocketPort) {
        this.serverSocketPort = serverSocketPort;
    }

    public String getTempFolder() {
        return tempFolder;
    }

    public void setTempFolder(String tempFolder) {
        this.tempFolder = tempFolder;
    }
}
