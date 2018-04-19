package test.org.mahatma.atp.common;


import org.mahatma.atp.common.engine.ModuleSummary;
import org.mahatma.atp.common.engine.ModuleSummaryManager;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

public class TestClassLoader {


    public static void main(String[] args) {
        String path = "/media/ji/AE94AAC094AA8A83/schedule-apps-1.0.0.1709221719.jar";

//        ModulClassLoader loader = new ModulClassLoader();

        File classpath = new File(path);
        URL[] urls = new URL[1];
        try {
            URL url = classpath.toURI().toURL();//将File类型转为URL类型，file为jar包路径

            urls[0] = url;
            URLClassLoader loader = new URLClassLoader(urls);

            Class c = loader.loadClass("com.feinno.schedule.apps.SimpleTest");

            ModuleSummaryManager moduleManager = new ModuleSummaryManager("", null, loader, "jar", path);

            moduleManager.assemble(false);

            for (String s : moduleManager.getMapping().keySet()) {

                ModuleSummary module = moduleManager.getMapping().get(s);
                System.out.println("name=" + s + " module" + module.getClassName() + module.getClassSimpleName() + module.getClassType());
            }
            loader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
