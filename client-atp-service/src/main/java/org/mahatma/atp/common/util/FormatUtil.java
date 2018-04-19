package org.mahatma.atp.common.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.mahatma.atp.common.db.bean.Pkg;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * Created by JiYunfei on 17-10-12.
 */
public class FormatUtil {
    public static final long RUN_TC_TIMEOUT = 1000 * 60 * 5;

    public static final String ENV = "env";
    public static final int MAXSUCCESSCODE = 200;
    public static final String PKGCFGKEY = "name";
    public static final String PKGCFGVALUE = "value";

    public static final String saveJarRoot = "/data/atp/pkg/";
    public static final String libName = "lib";
    public static final String decompressionName = "decompression";
    public static final String sourcePostfix = ".jar";

    public static final String dbPropertiesName = "URCS_ATPDB";

    public static final String TASK_RESULT_SUCCESS = "200";
    public static final String TASK_RESULT_FAIL = "400";
    public static final String TASK_RESULT_SUCCESS_TEXT = "SUCCESS";
    public static final String TASK_RESULT_FAIL_TEXT = "FAIL";

    public static String temporaryDir = "/home/atp/AutoTestFiles/";
    public static String configFileName = "env.properties";

    public static String shellDir = "/data/atp/required/";
    public static String shellName = "run.sh";

    public static String logName = "log";

    public static int runMark = 1;
    public static int pauseMark = 2;

    public static String generatePkgSourcePath(Pkg pkg) {
        return saveJarRoot + pkg.getName() + File.separator + pkg.getVersion()
                + File.separator + libName + File.separator + pkg.getName() + sourcePostfix;
    }

    public static String generatePkgDir(Pkg pkg) {
        return saveJarRoot + pkg.getName() + File.separator + pkg.getVersion() + File.separator;
    }

    public static String logPath(Long taskResultId) {
        return shellDir + logName + File.separator + taskResultId + ".log";
    }

    public static String generateDeletePackageCommand(Pkg pkg) {
        return "rm -rf " + generatePkgDir(pkg.getName(), pkg.getVersion());
    }

    public static String generatePkgDir(String name, String version) {
        return saveJarRoot + name + File.separator + version;
    }

    public static String stringToJsonArrayString(Properties properties) {
        JsonArray jsonArray = new JsonArray();
        for (Object o : properties.keySet()) {
            String key = (String) o;
            String value;
            try {
                value = new String(((String) properties.get(key)).getBytes("ISO-8859-1"), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                value = (String) properties.get(key);
            }

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(PKGCFGKEY, key);
            jsonObject.addProperty(PKGCFGVALUE, value);
            jsonArray.add(jsonObject);
        }
        return jsonArray.toString();
    }

    public static String decompressionPath(Pkg pkg) {
        return saveJarRoot + pkg.getName() + File.separator + pkg.getVersion() +
                File.separator + decompressionName + File.separator + "*";
    }

    public static String libPath(Pkg pkg) {
        return saveJarRoot + pkg.getName() + File.separator + pkg.getVersion() +
                File.separator + libName + File.separator + "*";
    }
}
