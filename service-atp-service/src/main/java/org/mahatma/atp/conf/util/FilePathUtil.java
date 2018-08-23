package org.mahatma.atp.conf.util;

import org.mahatma.atp.conf.AtpEnvConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * @author JiYunfei
 * @date 17-9-27
 */
public class FilePathUtil {
    private static Logger LOGGER = LoggerFactory.getLogger(FilePathUtil.class);

    /**
     * 检查path对应的路径是否存在，存在返回true，不存在返回false
     *
     * @param path
     * @return
     */
    public static boolean checkPathExist(String path) {
        File file = new File(path);
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 检查path对应的路径是否存在，不存在就创建
     *
     * @param path
     */
    public static void checkPathAndCreate(String path) {
        File appPath = new File(path);
        if (!appPath.exists()) {
            // 若父路径不存在也会创建比mkdir好使
            appPath.mkdirs();
            LOGGER.info("dir:" + path + " doesn't exists,create!");
        } else {
            LOGGER.info("dir:" + path + " already exists");
        }
    }

    /**
     * 检查path对应的路径是否存在，不存在就创建
     *
     * @param file
     * @return
     */
    public static boolean checkFileAndCreate(String file) {
        File f = new File(file);
        if (!f.exists()) {
            try {
                f.createNewFile();
                return true;
            } catch (IOException e) {
                LOGGER.error("file:" + file + " create fail!");
                return false;
            }
        } else {
            LOGGER.info("file:" + file + " already exists");
            return true;
        }
    }

    public static String logPath(long taskResultId) {
        return AtpEnvConfiguration.getInstance().getLogPath() + taskResultId + ".log";
    }
}
