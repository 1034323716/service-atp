package org.mahatma.atp.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public final class ExtClasspathLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExtClasspathLoader.class);

    private static Method addURL = initAddMethod();

    // 获取当前线程的classLoader
    private static URLClassLoader classloader = (URLClassLoader) Thread.currentThread().getContextClassLoader();

    /**
     * 初始化addUrl 方法.
     *
     * @return 可访问addUrl方法的Method对象
     */
    private static Method initAddMethod() {
        Method add = null;
        try {
            add = URLClassLoader.class.getDeclaredMethod("addURL", new Class[]{URL.class});
            add.setAccessible(true);
        } catch (Exception e) {
            LOGGER.error("initAddMethod Error", e);
        }
        return add;
    }


    /**
     * 加载jar classpath。
     */
    public static void loadClasspath(List<String> jarFiles) {
        if (jarFiles != null) {
            ExtClasspathLoader.jarFiles = jarFiles;
        }

        List<String> files = getJarFiles();
        for (String f : files) {
            loadClasspath(f);
        }
    }

    private static void loadClasspath(String filepath) {
        File file = new File(filepath);
        loopFiles(file);
    }

    /**
     * 循环遍历目录，找出所有的jar包。
     *
     * @param file 当前遍历文件
     */
    private static void loopFiles(File file) {
        if (file.isDirectory()) {
            File[] tmps = file.listFiles();
            for (File tmp : tmps) {
                loopFiles(tmp);
            }
        } else {
            if (file.getAbsolutePath().endsWith(".jar") ||
                    file.getAbsolutePath().endsWith(".zip") ||
                    file.getAbsolutePath().endsWith(".class")) {
                addURL(file);
            }
        }
    }

    /**
     * 通过filepath加载文件到classpath。
     *
     * @param file 文件路径
     * @return URL
     * @throws Exception 异常
     */
    private static void addURL(File file) {
        try {
            addURL.invoke(classloader, new Object[]{file.toURI().toURL()});
        } catch (Exception e) {
            LOGGER.error("addURL error", e);
        }
    }


    private static List<String> jarFiles = new ArrayList<>();

    /***
     * 从配置文件中得到配置的需要加载到classpath里的路径集合。
     * @return
     */
    private static List<String> getJarFiles() {
        return jarFiles;
    }
}