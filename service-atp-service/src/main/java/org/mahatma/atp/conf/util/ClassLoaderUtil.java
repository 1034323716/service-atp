package org.mahatma.atp.conf.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JiYunfei
 * @date 17-9-25
 */
public class ClassLoaderUtil {
    public static URLClassLoader createClassLoader(String decompressionPath) {
        List<URL> urlList = new ArrayList<>();
        File file = new File(decompressionPath);
        putInLoader(urlList, file);
        URL[] urls = urlList.toArray(new URL[]{});
        return new URLClassLoader(urls);
    }

    private static void putInLoader(List<URL> urlList, File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                putInLoader(urlList, f);
            }
        }
        try {
            urlList.add(file.toURI().toURL());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
