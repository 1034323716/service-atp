package org.mahatma.atp.common.util;

/**
 * @author lyfx
 * @date 17-9-15
 */
public class FileReaderUtil {

    /**
     * 检查给出的路径是否是绝对路径
     *
     * @param path 要判断的路径
     * @return 绝对路径返回 true 否则返回 false
     */
    public static boolean isAbsolutePath(String path) {
        return path.startsWith("/") || path.matches("^[a-zA-Z]:\\\\.*");
    }

}
