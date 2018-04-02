package org.mahatma.atp.conf.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Created by JiYunfei on 17-9-27.
 */
public class FilePathUtil {
    private static Logger LOGGER = LoggerFactory.getLogger(FilePathUtil.class);

    //检查path对应的路径是否存在，存在返回true，不存在返回false
    public static boolean checkFileExist(String path) {
        File file = new File(path);
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    //检查path对应的路径是否存在，不存在就创建
    public static void checkDirAndCreate(String path) {
        File appPath = new File(path);
        if (!appPath.exists()) {
            //若父路径不存在也会创建比mkdir好使
            appPath.mkdirs();
            LOGGER.info("dir:" + path + " doesn't exists,create!");
        } else {
            LOGGER.info("dir:" + path + " already exists");
        }
    }
}
