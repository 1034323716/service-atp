package org.mahatma.atp.common.util;

import com.feinno.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Class帮助类
 */
public class ClassHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClassHelper.class);

    /**
     * 获取一个ClassLoader下全部Class集合
     *
     * @param loader
     * @param pkgName
     * @param type
     * @param paths
     * @return
     */
    public static List<Class<?>> getClassList(ClassLoader loader, String pkgName, String type, String... paths) {
        List<Class<?>> classList = new ArrayList<>();
        try {
            if ("class".equals(type)) {
                // 按文件的形式去查找
                Enumeration<URL> urls = loader.getResources(pkgName);
                while (urls.hasMoreElements()) {
                    URL url = urls.nextElement();
                    if (url != null) {
                        LOGGER.debug("protocol:" + url.getProtocol() + " path:" + url.getPath());
                        if ("file".equals(url.getProtocol())) {
                            // 本地自己可见的代码
                            findClassName(loader, classList, pkgName, url.getPath());
                        }
                    }
                }
            } else if ("jar".equals(type)) {
                // 引用第三方jar的代码
                for (String path : paths) {
                    findClassNameByJar(loader, classList, pkgName, path);
                }
            }
        } catch (IOException e) {
            LOGGER.error("", e);
        }
        LOGGER.info("classList size:" + classList.size());
        return classList;
    }

    public static void findClassName(ClassLoader loader, List<Class<?>> clazzList, String pkgName, String pkgPath) {
        if (clazzList == null) {
            return;
        }
        if (pkgPath.contains("javaeval")) {
            return;
        }

        File[] files = filterClassFiles(pkgPath);// 过滤出.class文件及文件夹
        LOGGER.debug("files:" + ((files == null) ? "null" : "length=" + files.length));
        if (files != null) {
            for (File f : files) {
                String fileName = f.getName();
                if (f.isFile()) {
                    // .class 文件的情况
                    String clazzName = getClassName(pkgName, fileName);
                    LOGGER.debug("clazzName:" + clazzName);
                    addClassName(loader, clazzList, clazzName);
                } else {
                    // 文件夹的情况
                    // 需要继续查找该文件夹/包名下的类
                    String subPkgName;
                    if (!StringUtils.isNullOrEmpty(pkgName)) {
                        subPkgName = pkgName + "." + fileName;
                    } else {
                        subPkgName = fileName;
                    }
                    String subPkgPath = pkgPath + "/" + fileName;
                    findClassName(loader, clazzList, subPkgName, subPkgPath);
                }
            }
        }
    }

    /**
     * 第三方Jar类库的引用。<br/>
     *
     * @throws IOException
     */
    public static void findClassNameByJar(ClassLoader loader, List<Class<?>> clazzList, String pkgName, String path) {
        JarFile jarFile = null;
        try {
            jarFile = new JarFile(path);

            LOGGER.debug("jarFile:" + jarFile.getName());
            Enumeration<JarEntry> jarEntries = jarFile.entries();
            while (jarEntries.hasMoreElements()) {
                JarEntry jarEntry = jarEntries.nextElement();
                String jarEntryName = jarEntry.getName(); // 类似：sun/security/internal/interfaces/TlsMasterSecret.class
                String clazzName = jarEntryName.replace("/", ".");
                int endIndex = clazzName.lastIndexOf(".");
                String prefix = null;
                String prefix_name = null;
                if (endIndex > 0) {
                    prefix_name = clazzName.substring(0, endIndex);
                    endIndex = prefix_name.lastIndexOf(".");
                    if (endIndex > 0) {
                        prefix = prefix_name.substring(0, endIndex);
                    }
                }
                if (jarEntryName.endsWith(".class")) {
                    if(prefix != null) {
                        if (prefix.equals(pkgName)) {
                            LOGGER.debug("jar entryName:" + jarEntryName);
                            addClassName(loader, clazzList, prefix_name);
                        } else if (prefix.startsWith(pkgName)) {
                            // 遍历子包名：子类
                            LOGGER.debug("jar entryName:" + jarEntryName);
                            addClassName(loader, clazzList, prefix_name);
                        }
                    }else{
                        LOGGER.debug("jar entryName:" + jarEntryName);
                        addClassName(loader, clazzList, prefix_name);
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("", e);
        } finally {
            if (jarFile != null) {
                try {
                    jarFile.close();
                } catch (IOException e) {
                    LOGGER.error("", e);
                }
            }
        }

    }

    private static File[] filterClassFiles(String pkgPath) {
        if (pkgPath == null) {
            return null;
        }
        // 接收 .class 文件 或 类文件夹
        return new File(pkgPath).listFiles(file -> (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory());
    }

    private static String getClassName(String pkgName, String fileName) {
        int endIndex = fileName.lastIndexOf(".");
        String clazz = null;
        if (endIndex >= 0) {
            clazz = fileName.substring(0, endIndex);
        }
        String clazzName = null;
        if (clazz != null) {
            clazzName = pkgName + "." + clazz;
        }
        return clazzName;
    }

    private static void addClassName(ClassLoader classLoader, List<Class<?>> clazzList, String clazzName) {
        if (clazzList != null && clazzName != null) {
            Class<?> clazz = null;
            try {
                LOGGER.debug("add Class:" + clazzName);
                clazz = classLoader.loadClass(clazzName);
            } catch (Throwable e) {
//                LOGGER.error("", e);
            }
//            String basePath = System.getProperty("user.dir");
//            String path = basePath + File.separator;
//            try {
//                path = path.substring(1).replace(File.separator, ".");
//                clazz = classLoader.loadClass(path + clazzName);
//            } catch (ClassNotFoundException e1) {
//                LOGGER.error("basePath + clazzName = {}", path + clazzName, e1);
//            }
            if (clazz != null) {
                clazzList.add(clazz);
                LOGGER.debug("add annotation:" + clazz);
            }
        }
    }
}
