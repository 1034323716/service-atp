import org.mahatma.atp.conf.arg.PackageSetting;
import org.mahatma.atp.conf.util.ResourceUtil;

/**
 * @author JiYunfei
 * @date 18-8-22
 */
public class TestAnalysisJar {

    public static void main(String[] args) {
        ResourceUtil resourceUtils = new ResourceUtil("/home/ji/下载/sims-test2.jar");
        resourceUtils.init();
        PackageSetting packageSetting = resourceUtils.getPackageSetting();
        System.out.println(packageSetting);
    }

}
