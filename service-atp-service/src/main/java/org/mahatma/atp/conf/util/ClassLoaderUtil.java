package org.mahatma.atp.conf.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JiYunfei on 17-9-25.
 */
public class ClassLoaderUtil {
    public static URLClassLoader createClassLoader(String... paths) {
        List<URL> urlList = new ArrayList<>();
        for (String path : paths) {
            File file = new File(path);
            try {
                urlList.add(file.toURI().toURL());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        URL[] urls = urlList.toArray(new URL[]{});
        return new URLClassLoader(urls);
    }
}
