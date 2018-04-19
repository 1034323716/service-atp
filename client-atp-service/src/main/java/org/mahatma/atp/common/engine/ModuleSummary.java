package org.mahatma.atp.common.engine;

import com.feinno.util.Combo3;
import org.mahatma.atp.common.bean.ClassType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 一个可运行的用例描述
 */
public class ModuleSummary {

    /**
     * 类全名
     */
    private String className;
    /**
     * 类短名
     */
    private String classSimpleName;
    /**
     * 用例类型
     */
    private ClassType classType;
    /**
     * 参数1=Class内部变量
     * 参数2=注解依赖名
     * 参数3=注解别名
     */
    private List<Combo3<String, String, String>> varList = new ArrayList<>();
    /**
     * 顺便构造的依赖
     */
    private Map<String, String> defaultValueMapping = new HashMap<>();
    private String nickname;

    private Class clazz;

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassSimpleName() {
        return classSimpleName;
    }

    public void setClassSimpleName(String classSimpleName) {
        this.classSimpleName = classSimpleName;
    }

    public ClassType getClassType() {
        return classType;
    }

    public void setClassType(ClassType classType) {
        this.classType = classType;
    }

    public void varSetter(Combo3<String, String, String> combo3) {
        varList.add(combo3);
    }

    public void addDefault(String name, String defValue) {

        defaultValueMapping.put(name, defValue);
    }

    public List<Combo3<String, String, String>> getVarList() {
        return varList;
    }

    public void setVarList(List<Combo3<String, String, String>> varList) {
        this.varList = varList;
    }

    public Map<String, String> getDefaultValueMapping() {
        return defaultValueMapping;
    }

    public void setDefaultValueMapping(Map<String, String> defaultValueMapping) {
        this.defaultValueMapping = defaultValueMapping;
    }
}
