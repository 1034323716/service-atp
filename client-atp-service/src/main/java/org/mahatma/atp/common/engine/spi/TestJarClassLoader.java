package org.mahatma.atp.common.engine.spi;

import org.mahatma.atp.common.engine.ModuleSummary;
import org.mahatma.atp.common.engine.ModuleSummaryManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;
import java.util.Map;

/**
 * 将多个测试路径传入，得到解析出来的Map<String, ModuleSummary>
 * <p>
 * 测试Jar包加载器,用于加载全量Jar包解析注解类
 */
public class TestJarClassLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestJarClassLoader.class);

    private URLClassLoader loader = null;

    private ModuleSummaryManager moduleManager;

    private String[] paths;

    /**
     * @param paths
     */
    public TestJarClassLoader(String... paths) {
        try {
            this.paths = paths;
            URL[] urls = new URL[paths.length];

            for (int i = 0; i < paths.length; i++) {
                String path = paths[i];
                File file = new File(path);

                URL url = file.toURI().toURL();//将File类型转为URL类型，file为jar包路径

                urls[i] = url;
            }
            loader = new URLClassLoader(urls);
        } catch (Exception e) {
            LOGGER.error("", e);
        }
    }

    /**
     * 执行装配工作 返回ModuleSummary集合
     *
     * @return
     */
    public Map<String, ModuleSummary> assemble() {
        moduleManager = new ModuleSummaryManager("", null, loader, "jar", paths);

        //从loader中获取所有得到classList，并进行注释解析
        moduleManager.assemble(false);

        return moduleManager.getMapping();
    }

    /**
     * 获取一个已经装配好的ModuleSummary集合,如果未执行装配assemble则报错,可别傻逼哦~
     *
     * @return
     */
    public Map<String, ModuleSummary> get() {
        return moduleManager.getMapping();
    }

    /**
     * 将URLClassLoader关闭
     * <p>
     * 释放ClassLoader
     */
    public void close() {
        try {
            if (loader != null) {
                loader.close();
            }
        } catch (Exception e) {
            LOGGER.error("what a fuck exception!", e);
        }
    }
}
