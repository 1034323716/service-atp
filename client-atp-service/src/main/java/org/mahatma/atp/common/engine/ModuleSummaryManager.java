package org.mahatma.atp.common.engine;

import org.mahatma.atp.common.bean.ClassType;
import org.mahatma.atp.common.engine.spi.AnnotationResolver;
import org.mahatma.atp.common.util.ClassHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 运行用例集合管理
 */
public class ModuleSummaryManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModuleSummaryManager.class);

    private AutoTestEngine engine;

    private List<Class<?>> classList;

    private Map<String, ModuleSummary> moduleMapping = new HashMap<>();

    private Map<String, StackSummary> stackMapping = new HashMap<>();

    /**
     * @param name   用力集名字
     * @param engine 引擎
     * @param loader 含有用例的ClassLoader,可以是一个本地内存使用的当前ClassLoader,也可以是一个初始化的加载Jar的URLClassLoader
     * @param type   类型 包括Jar、class
     * @param paths  如果是Jar类型的需要填写本地jar路径集
     */
    public ModuleSummaryManager(String name, AutoTestEngine engine, ClassLoader loader, String type, String... paths) {
        this.engine = engine;
        classList = ClassHelper.getClassList(loader, name, type, paths);
    }

    public void addStackClassOnLocation() {
        LOGGER.info("local run add stack");
        try {
            classList.add(Class.forName("org.mahatma.atp.common.stack.spi.HttpStack"));
            classList.add(Class.forName("org.mahatma.atp.common.stack.spi.MqttStack"));
            classList.add(Class.forName("org.mahatma.atp.common.stack.spi.SipStack"));
        } catch (ClassNotFoundException e) {
            LOGGER.error("Stack Class Not Found");
        }
    }


    public void addPathClazz(String path, ClassLoader loader, String type, String... paths) {

        List<Class<?>> classList = ClassHelper.getClassList(loader, path, type, paths);
        this.classList.addAll(classList);
    }

    public void assemble(boolean build) {
        for (Class<?> aClass : classList) {
            ModuleSummary runningModule = AnnotationResolver.resolveInstance(aClass, engine != null ? engine.getConfigProvider() : null);
            if (runningModule != null) {
                if (ClassType.TestModule == runningModule.getClassType()) {
                    moduleMapping.put(runningModule.getClassSimpleName(), runningModule);
                } else if (ClassType.TestStack == runningModule.getClassType()) {
                    stackMapping.put(runningModule.getClassSimpleName(), (StackSummary) runningModule);
                }

            }
        }

    }

    public Map<String, ModuleSummary> getMapping() {
        return moduleMapping;
    }

    public Map<String, StackSummary> getStackMapping() {
        return stackMapping;
    }
}
