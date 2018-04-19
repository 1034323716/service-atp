package org.mahatma.atp.common.engine.spi;

import com.feinno.util.Combo3;
import com.feinno.util.StringUtils;
import org.mahatma.atp.common.annotations.TestModule;
import org.mahatma.atp.common.annotations.TestStack;
import org.mahatma.atp.common.annotations.TestVarSetter;
import org.mahatma.atp.common.bean.ClassType;
import org.mahatma.atp.common.engine.ModuleSummary;
import org.mahatma.atp.common.engine.StackSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

/**
 * 传入ConfigProvider和clazz得到该类的ModuleSummary
 * <p>
 * <p>
 * 注释解析器
 */
public class AnnotationResolver {
    private static final Logger LOGGER = LoggerFactory.getLogger(AnnotationResolver.class);

    /**
     * 一个class上要不注TestModule，要不注TestStack
     * 通过反射读取Annotation, 创建ModuleSummary,StackSummary
     *
     * @param clazz,contextProvider
     * @return
     */
    public static ModuleSummary resolveInstance(Class<?> clazz, ConfigProvider contextProvider) {
        if (clazz != null) {
            TestModule module = clazz.getAnnotation(TestModule.class);
            if (module != null) {
                LOGGER.debug("find " + module + " in " + clazz.toString());
                ModuleSummary moduleSummary = new ModuleSummary();

                moduleSummary.setClassName(clazz.getName());
                moduleSummary.setClassSimpleName(clazz.getSimpleName());
                moduleSummary.setNickname(module.cname());
                moduleSummary.setClassType(ClassType.TestModule);
                moduleSummary.setClazz(clazz);
//                if (contextProvider != null && contextProvider.getConfig(FormatUtil.ENV) != null) {
                resolveVar(clazz, moduleSummary, contextProvider);
//                }
                return moduleSummary;
            }
            TestStack stack = clazz.getAnnotation(TestStack.class);
            if (stack != null) {
                StackSummary stackSummary = new StackSummary();
                stackSummary.setClassName(clazz.getName());
                stackSummary.setClassSimpleName(clazz.getSimpleName());
                stackSummary.setClassType(ClassType.TestStack);
//                if (contextProvider != null && contextProvider.getConfig(FormatUtil.ENV) != null) {
                resolveVar(clazz, stackSummary, contextProvider);
//                }
                return stackSummary;
            }
        }
        return null;

    }

    /**
     * 通过反射读取Annotation, 写入defaultValueMapping和varList
     *
     * @param clazz
     * @param moduleBean
     * @param contextProvider
     */
    private static void resolveVar(Class<?> clazz, ModuleSummary moduleBean, ConfigProvider contextProvider) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            TestVarSetter varSetter = field.getAnnotation(TestVarSetter.class);
            if (varSetter != null) {
                String name = varSetter.name();
                String cname = varSetter.cname();
                moduleBean.varSetter(new Combo3<>(field.getName(), name, cname));
                if (contextProvider != null) {
                    if (contextProvider.getEnv() != null) {
                        String defValue = contextProvider.getEnv(name);
                        if (!StringUtils.isNullOrEmpty(defValue)) {
                            moduleBean.addDefault(name, defValue);
                        }
                    }
                }
            }
        }
    }
}
