package org.mahatma.atp.common.engine.spi;

import com.feinno.util.StringUtils;
import org.mahatma.atp.common.Test;
import org.mahatma.atp.common.engine.AutoTestEngine;
import org.mahatma.atp.common.param.StartupOptionEnum;
import org.mahatma.atp.common.util.FileReaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * ----xxxx-1.0.0
 * -lib
 * -xxx.jar
 * -run-id.sh
 * -env.p
 * -e.xml
 * -resource
 * -db.p
 * -redis.p
 * <p>
 * <p>
 * <p>
 * 放AutoTestEngine和Properties，Properties只能放一个
 * <p>
 * 运行时配置管理
 */
public class ConfigProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigProvider.class);

    public static final String ENV = "env";

    private String root;
    /**
     * 自动化测试引擎索引
     */
    private AutoTestEngine engine;

    private List<Class<? extends Test>> moduleList = new ArrayList<>();

    private Map<String, Properties> configMapping = new HashMap<>();

    public ConfigProvider(AutoTestEngine engine) {
        this.engine = engine;
        root = System.getProperty("user.dir");
    }

    public void addConfigPath(String path) {
        if (!FileReaderUtil.isAbsolutePath(path)) {
            path = root + File.separator + path;
        }
        try {
            Properties properties = new Properties();
            File file = new File(path);

            FileInputStream fileInputStream = new FileInputStream(file);

            properties.load(fileInputStream);
            String fileName = file.getName();

            putConfig(fileName.substring(0, fileName.indexOf(".")), properties);
        } catch (IOException e) {
            LOGGER.error("load prop file error.", e);
            throw new IllegalArgumentException("can't load " + path);
        }
    }

    public void addConfig(String name, Properties properties) {
        putConfig(name, properties);
    }

    public String getEnv(String name) {

        try {
            if (getEnv() != null && getEnv().getProperty(name) != null) {
                // 大于0说明在线上
                if (StartupOptionEnum.TASKID.getArgs().size() > 0) {
                    return new String(getEnv().getProperty(name).getBytes(), "UTF-8");
                } else {
                    return new String(getEnv().getProperty(name).getBytes("ISO-8859-1"), "UTF-8");
                }
            } else {
                return StringUtils.EMPTY;
            }
        } catch (UnsupportedEncodingException e) {
            return getEnv().getProperty(name);
        }
    }

    public Properties getEnv() {
        return configMapping.get(ENV);
    }

    public List<Class<? extends Test>> getTestModuleList() {
        return moduleList;
    }

    public void addTestModule(Class<? extends Test> test) {
        this.moduleList.add(test);
    }


    public void putConfig(String name, Properties properties) {
        configMapping.put(name, properties);
    }

    public Properties getConfig(String name) {
        return configMapping.get(name);
    }
}
