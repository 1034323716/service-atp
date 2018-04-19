package org.mahatma.atp.common.util;

import com.feinno.util.Combo3;
import com.feinno.util.StringUtils;
import org.mahatma.atp.common.annotations.TestVarSetter;
import org.mahatma.atp.common.engine.ModuleSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * Created by JiYunfei on 17-9-25.
 */
public class TestInjector {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestInjector.class);

    public static Object injectTestByModuleSummary(ModuleSummary summary) {
        if (summary != null) {
            Map<String, String> defaultValueMapping = summary.getDefaultValueMapping();
            List<Combo3<String, String, String>> varList = summary.getVarList();

            Object obj = null;
            Class<?> clazz = null;

            try {
                obj = summary.getClazz().newInstance();
            } catch (Exception e) {
                LOGGER.warn(e.getMessage());
            }

            TestInjector.injectTest(obj, varList, defaultValueMapping);

            return obj;
        } else {
            return null;
        }
    }

    /**
     * @param obj
     * @param vars
     */
    public static void injectTest(Object obj, List<Combo3<String, String, String>> vars, Map<String, String> config) {
        Class clazz = obj.getClass();
        if (clazz != null && obj != null) {
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                String fieldName = declaredField.getName();
                Combo3<String, String, String> var = TestInjector.getCombo3ByFirstString(fieldName, vars);
                if (var != null) {
                    TestInjector.injectFieldVar(obj, var, config);
                }
            }
        }
    }

    /**
     * 使用var完成一个field的注入
     *
     * @param obj
     * @param var,config
     * @return true表示成功, false表示Field类型未知
     */
    public static boolean injectFieldVar(Object obj, Combo3<String, String, String> var, Map<String, String> config) {
        String name;
        String value = null;

        String fieldName = var.getV1();
        Field field = getField(obj.getClass(), fieldName);
        if (field == null) {
            throw new IllegalArgumentException("Field not found: " + fieldName);
        }

        Class<?> fieldClazz = field.getType();
        TestVarSetter anno = field.getAnnotation(TestVarSetter.class);
        if (anno != null) {
            name = anno.name();
            value = config.get(name);
        }

        //
        // 处理已知类型
        TestFieldType fieldType = TestFieldType.valueOf(fieldClazz);
        if (fieldType != null && !StringUtils.isNullOrEmpty(value)) {
            LOGGER.debug("setKnowndType:<{}> field=" + fieldName, fieldType);
            Object fieldlValue = fieldType.convertFrom(fieldClazz, value);
            setField(obj, field, fieldlValue);
            return true;
        }

        // 未知类型
        return false;
    }

    /**
     * 通过反射设置字段值
     *
     * @param obj
     * @param field
     * @param value
     */
    public static void setField(Object obj, String field, Object value) {
        Field f = getField(obj.getClass(), field);
        if (f == null) {
            throw new IllegalArgumentException("Field not found:" + field);
        }
        setField(obj, f, value);
    }


    /**
     * 通过反射设置字段值
     *
     * @param obj
     * @param field
     * @param value
     */
    public static void setField(Object obj, Field field, Object value) {
        try {
            field.setAccessible(true);
            field.set(obj, value);
            field.setAccessible(false);
        } catch (IllegalAccessException e) {
            LOGGER.error("Invoke failed.", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据名称从object对象中取出Field,因为允许object中布存在这个Field，所以异常均被忽略
     *
     * @param clazz
     * @param fieldName
     * @return
     */
    public static Field getField(Class<?> clazz, String fieldName) {
        Field field = null;
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(fieldName);
                return field;
            } catch (NoSuchFieldException ex) {
            } catch (Exception ex) {
                LOGGER.error("getField failed:<" + fieldName + "> {}", ex);
            }
        }
        return null;
    }

    public static Combo3<String, String, String> getCombo3ByFirstString(String fisrt, List<Combo3<String, String, String>> combo3List) {
        for (Combo3<String, String, String> combo3 : combo3List) {
            if (combo3.getV1().equals(fisrt))
                return combo3;
        }
        return null;
    }
}
