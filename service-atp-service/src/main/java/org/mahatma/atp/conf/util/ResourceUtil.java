package org.mahatma.atp.conf.util;

import com.feinno.superpojo.util.StringUtils;
import com.feinno.util.Combo3;
import com.feinno.util.DateUtil;
import com.google.gson.JsonObject;
import org.mahatma.atp.common.engine.ModuleSummary;
import org.mahatma.atp.common.engine.ModuleSummaryManager;
import org.mahatma.atp.common.util.FormatUtil;
import org.mahatma.atp.conf.arg.PackageSetting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by JiYunfei on 17-9-11.
 */
public class ResourceUtil {
    public static Logger LOGGER = LoggerFactory.getLogger(ResourceUtil.class);

    //前端给的上传上来的文件的路径
    private String location;

    private PackageSetting packageSetting = new PackageSetting();

    //源压缩包名称
    private String sourceName;
    //最终的源文件的路径
    private String sourcePath;
    //依赖的jar包最终位置
    private String libPath;
    private String decompressionPath;

    //设置location，并得到压缩包名称
    public ResourceUtil(String location) {
        this.location = location;
        String[] splits = location.split("/");
        if (!StringUtils.isNullOrEmpty(splits[splits.length - 1])) {
            sourceName = splits[splits.length - 1];
        } else {
            sourceName = splits[splits.length - 2];
        }
        packageSetting.setName(sourceName.substring(0, sourceName.lastIndexOf('.')));
        packageSetting.setVersion(DateUtil.getSystemCurrentDate("yyyyMMddHHmmss"));
    }

    //其中几个方法的顺序不可变
    public void init() {
        moveSourceAndDeCompress();
        packageSetting = createPackageSetting();
    }

    public PackageSetting createPackageSetting() {
        analysisEntriesAndConfigName();
        packageSetting.setProperties(analysisProperties());
        return packageSetting;
    }

    //jarPath中解析出包裹params.properties的PackageSetting.Properties
    public void analysisEntriesAndConfigName() {

        URLClassLoader loader = ClassLoaderUtil.createClassLoader(decompressionPath);
        ModuleSummaryManager moduleSummaryManager = new ModuleSummaryManager("",
                null, loader, "class");
        File file = new File(decompressionPath);
        for (File jar : file.listFiles()) {
            if (jar.getName().endsWith(".jar"))
                moduleSummaryManager.addPathClazz("", loader, "jar", jar.getAbsolutePath());
        }
        moduleSummaryManager.addPathClazz("", loader, "jar", sourcePath);

        moduleSummaryManager.assemble(true);
        Map<String, ModuleSummary> moduleSummaryMap = moduleSummaryManager.getMapping();
        JsonObject jsonObject = new JsonObject();
        for (Map.Entry<String, ModuleSummary> moduleSummaryEntry : moduleSummaryMap.entrySet()) {
            ModuleSummary moduleSummary = moduleSummaryEntry.getValue();

            for (Combo3<String, String, String> combo3 : moduleSummary.getVarList()) {
                jsonObject.addProperty(combo3.getV2(), combo3.getV3());
            }

            PackageSetting.PackageEntry packageEntry = new PackageSetting.PackageEntry();
            packageEntry.setClassPath(moduleSummary.getClassName());
            packageEntry.setNickname(moduleSummary.getNickname());
            packageEntry.setName(moduleSummary.getClassSimpleName());

            packageSetting.getEntries().add(packageEntry);
        }
        packageSetting.setConfigName(jsonObject.toString());
        try {
            loader.close();
        } catch (IOException e) {
            LOGGER.warn(e.getMessage());
        }
    }

    // jarPath中解析出包裹params.properties的PackageSetting.Properties
    public PackageSetting.Properties analysisProperties() {
        PackageSetting.Properties pksProperties = new PackageSetting.Properties();
        pksProperties.setName("默认");

        File testPackageFile = new File(sourcePath);
        if (testPackageFile.exists()) {
            JarFile jarFile;
            try {
                jarFile = new JarFile(libPath + sourceName);
                Enumeration enu = jarFile.entries();
                while (enu.hasMoreElements()) {
                    JarEntry entry = (JarEntry) enu.nextElement();
                    String name = entry.getName();
                    if (name.endsWith(FormatUtil.configFileName)) {
                        InputStream input = jarFile.getInputStream(entry);
                        Properties properties = new Properties();
                        properties.load(input);
                        pksProperties.setContent(FormatUtil.stringToJsonArrayString(properties));
                    }
                }
            } catch (IOException e) {
                LOGGER.warn(e.getMessage());
            }
        } else {
            LOGGER.info(location + "unExist");
        }
        if (pksProperties.getContent() == null) {
            pksProperties.setContent("");
        }
        return pksProperties;
    }

    public PackageSetting getPackageSetting() {
        return packageSetting;
    }

    /**
     * 移动源文件到/data/schedule/xml文件中的name/用户id/版本/下
     * 并将lib，conf的文件的路径拼好并创建
     * package表中的name就是xml文件中的name
     */
    public void moveSourceAndDeCompress() {
        FilePathUtil.checkPathAndCreate(FormatUtil.generatePkgDir(packageSetting.getName(), packageSetting.getVersion()));
        String finalPath = FormatUtil.generatePkgDir(packageSetting.getName(), packageSetting.getVersion())
                + File.separator;
        sourcePath = finalPath + FormatUtil.libName + File.separator + sourceName;
        libPath = finalPath + FormatUtil.libName + File.separator;
        decompressionPath = finalPath + FormatUtil.decompressionName + File.separator;
        FilePathUtil.checkPathAndCreate(libPath);
//        String commandMove = "cp" + " " + location + " " + libPath;
        String commandMove = "mv" + " " + location + " " + libPath;
        RunShellUtil.runShell(commandMove);

        /**
         * 解压,将移动过来的压缩包解压到lib中
         * todo 需要考虑根据打包类型
         */
        String commandDeCompress = "unzip " + libPath + sourceName + " -d " + decompressionPath;
        RunShellUtil.runShellAndRead(commandDeCompress);
    }
}
