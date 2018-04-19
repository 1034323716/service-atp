package org.mahatma.atp.common.engine;

import com.feinno.util.Action;
import com.feinno.util.Combo2;
import org.helium.database.Database;
import org.mahatma.atp.common.Test;
import org.mahatma.atp.common.bean.Result;
import org.mahatma.atp.common.engine.spi.AnnotationResolver;
import org.mahatma.atp.common.engine.spi.ConfigProvider;
import org.mahatma.atp.common.exception.AutoTestRuntimeException;
import org.mahatma.atp.common.util.RunUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Properties;

/**
 * 自动化测试引擎,Main入口
 */
public class AutoTestEngine {
    private static final Logger LOGGER = LoggerFactory.getLogger(AutoTestEngine.class);
    public static final AutoTestEngine INSTANCE = new AutoTestEngine();

    private String root;
    //存放jar包中要运行的用例的集合
    private ConfigProvider configProvider;
    //jar包中全量的module
    private ModuleSummaryManager moduleManager;
    //运行之后的报告集合
    private ModuleReportManager reportManager;
    //协议栈集合
    private StackManager stackManager;

    private ResourcesManager resourcesManager;
    private ClassLoader loader;

    public StackManager getStackManager() {
        return stackManager;
    }

    /**
     * 手动加入一个测试类
     *
     * @param clazz
     */
    public void addModule(Class<? extends Test> clazz) {
        configProvider.addTestModule(clazz);
    }

    public void addProperties(String name, Properties properties) {
        configProvider.addConfig(name, properties);
    }

    /**
     * @param path
     */
    public void addPropertiesPath(String path) {
        configProvider.addConfigPath(path);
    }

    private AutoTestEngine() {
        root = System.getProperty("user.dir");
        loader = Thread.currentThread().getContextClassLoader();
        configProvider = new ConfigProvider(this);
        moduleManager = new ModuleSummaryManager("", this, null, "");
        reportManager = new ModuleReportManager(this);
        stackManager = new StackManager(this);
    }

    /**
     * 本地测试的start
     */
    public void start() {
        moduleManager = new ModuleSummaryManager("", this, loader, "class", root);
        moduleManager.addStackClassOnLocation();

        //step1.class组装:解析全部内存内的class对象,提取用例注解,构造用例描述集
        moduleManager.assemble(true);
        //step2.协议栈初始化
        stackManager.init();
        //step3.顺序执行测试用例
        if (configProvider.getTestModuleList().size() > 0) {
            for (Class<? extends Test> clazz : configProvider.getTestModuleList()) {
                ModuleSummary summary = AnnotationResolver.resolveInstance(clazz, configProvider);
                if (summary != null) {
                    execute(summary);
                }
            }
        } else {
            Map<String, ModuleSummary> mapping = moduleManager.getMapping();
            for (String s : mapping.keySet()) {
                ModuleSummary summary = mapping.get(s);
                if (summary != null) {
                    execute(summary);
                }
            }
        }

    }

    public Combo2<Boolean, Result> execute(ModuleSummary summary) {
        Combo2<Boolean, Result> resultCombo2 = moduleShellRun(summary);
        LOGGER.info(resultCombo2.getV2().toString());
        return resultCombo2;
    }

    private Combo2<Boolean, Result> moduleShellRun(ModuleSummary summary) {
        ModuleShell shell = new ModuleShell(summary);
        shell.prepare(this);
        Combo2<Boolean, Result> resultCombo2 = shell.run();
        return resultCombo2;
    }

    public void runTask() throws AutoTestRuntimeException {
        moduleManager.addStackClassOnLocation();
        getModuleManager().assemble(true);
        getStackManager().init();

        Database database = getResourcesManager().getDatabaseBydbPro();
        RunUtil runUtil = new RunUtil(database, INSTANCE);
        runUtil.startOnLine();
    }

    public ConfigProvider getConfigProvider() {
        return configProvider;
    }

    public ModuleSummaryManager getModuleManager() {
        return moduleManager;
    }

    public ResourcesManager getResourcesManager() {
        return resourcesManager;
    }

    public ModuleReportManager getReportManager() {
        return reportManager;
    }

    public void setResourcesManager(ResourcesManager resourcesManager) {
        this.resourcesManager = resourcesManager;
        resourcesManager.setConfigProvider(configProvider);
    }
}