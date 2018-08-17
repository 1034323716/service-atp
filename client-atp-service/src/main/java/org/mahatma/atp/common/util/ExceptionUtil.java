package org.mahatma.atp.common.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author JiYunfei
 * @date 18-8-17
 */
public class ExceptionUtil {

    public static String getErrorInfoFromException(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        try {
            e.printStackTrace(pw);
            return sw.toString();
        } catch (Exception exception) {
            return "ErrorInfoFromException";
        } finally {
            try {
                sw.close();
                pw.close();
            } catch (IOException ioe) {

            }
        }

    }

}
