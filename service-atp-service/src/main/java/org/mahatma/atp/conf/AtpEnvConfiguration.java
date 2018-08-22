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
    @Field(id = 4)
    private String capturePkgPath;
    @Field(id = 5)
    private boolean localStart;
    @Field(id = 6)
    private boolean retest;
    @Field(id = 7)
    private String runShPath;
    @Field(id = 8)
    private String logPath;
    @Field(id = 9)
    private String runTaskShellTemplate;

    public String getRunTaskShellTemplate() {
        return runTaskShellTemplate;
    }

    public void setRunTaskShellTemplate(String runTaskShellTemplate) {
        this.runTaskShellTemplate = runTaskShellTemplate;
    }

    public String getLogPath() {
        return logPath;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }

    public String getRunShPath() {
        return runShPath;
    }

    public void setRunShPath(String runShPath) {
        this.runShPath = runShPath;
    }

    public String getCapturePkgPath() {
        return capturePkgPath;
    }

    public void setCapturePkgPath(String capturePkgPath) {
        this.capturePkgPath = capturePkgPath;
    }

    public boolean isRetest() {
        return retest;
    }

    public void setRetest(boolean retest) {
        this.retest = retest;
    }

    public boolean isLocalStart() {
        return localStart;
    }

    public void setLocalStart(boolean localStart) {
        this.localStart = localStart;
    }

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
