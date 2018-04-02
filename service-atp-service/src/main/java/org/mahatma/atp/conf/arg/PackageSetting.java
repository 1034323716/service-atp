package org.mahatma.atp.conf.arg;

import com.feinno.superpojo.SuperPojo;
import com.feinno.superpojo.annotation.Entity;
import com.feinno.superpojo.annotation.Field;
import com.feinno.superpojo.annotation.FieldExtensions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lyfx on 17-9-10.
 */
@Entity(name = "package")
public class PackageSetting extends SuperPojo {
    /**
     * 测试包名
     */
    @Field(id = 1)
    private String name;
    /**
     * 测试服务版本号
     */
    @Field(id = 2)
    private String version;
    /**
     * 估计放的是各个配置项的昵称对应关系
     */
    @Field(id = 3)
    private String configName;
    /**
     * 入口列表
     */
    @Field(id = 4, name = "entry")
    @FieldExtensions(newParentNode = "entries")
    private List<PackageEntry> entries = new ArrayList<>();
    /**
     * 配置
     */
    @Field(id = 5)
    private Properties properties;

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<PackageEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<PackageEntry> entries) {
        this.entries = entries;
    }

    @Entity(name = "entry")
    public static class PackageEntry extends SuperPojo {
        /**
         * 入口测试类名称
         */
        @Field(id = 1)
        private String name;
        /**
         * 该测试类路径
         */
        @Field(id = 2)
        private String classPath;
        /**
         * 描述
         */
        @Field(id = 3)
        private String desc;
        /**
         * 昵称
         */
        @Field(id = 4)
        private String nickname;

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getClassPath() {
            return classPath;
        }

        public void setClassPath(String classPath) {
            this.classPath = classPath;
        }
    }

    public static class Properties extends SuperPojo {
        @Field(id = 1)
        private String name;
        @Field(id = 2)
        private String content;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
