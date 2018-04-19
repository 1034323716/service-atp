package org.mahatma.atp.common.util;

/**
 * Created by lyfx on 17-9-15.
 */

public class FileReaderUtil {

//    private static DeafaultPropReader defaultProp;
//
//    static {
//        defaultProp = new DeafaultPropReader("config/params.properties");
//    }
//
//
//    private static class DeafaultPropReader{
//        private Properties defaultProp;
//
//        DeafaultPropReader(String defaultPropPath){
//            InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(defaultPropPath);
//            Properties properties = new Properties();
//            try {
//                properties.load(resourceAsStream);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            defaultProp = properties;
//        }
//
//        private String getValue(String key){
//            return defaultProp.getProperty(key);
//        }
//    }
//
//    public static String getDefaultPropValue(String key){
//        return defaultProp.getValue(key);
//    }



    /**
     * 检查给出的路径是否是绝对路径
     * @param path 要判断的路径
     * @return 绝对路径返回 true 否则返回 false
     */
    public static boolean isAbsolutePath(String path){
        return path.startsWith("/") || path.matches("^[a-zA-Z]:\\\\.*");
    }


//    public static void main(String[] args) {
//        String mobileNos = FileReaderUtil.getDefaultPropValue("mobileNos");
//        System.out.println("mobileNos is " + mobileNos);
//    }

}
