package org.mahatma.atp.common.bean;

import com.feinno.superpojo.annotation.Entity;
import com.feinno.superpojo.annotation.Field;
import com.feinno.superpojo.annotation.NodeType;

import java.util.List;

/**
 * Created by lyfx on 17-10-10.
 */
@Entity(name = "package")
public class PkgCfgArgs extends SuperClass {

    /**
     * pkgId
     */
    @Field(id = 1, name = "id", type = NodeType.ATTR)
    private long pkgId;

    /**
     * cfgId
     */
    @Field(id = 2, name = "config", type = NodeType.ATTR)
    private long cfgId;

    @Field(id = 3, name = "testCase")
    private List<TestCaseCfgArgs> testCaseCfgArgs;

    public long getPkgId() {
        return pkgId;
    }

    public void setPkgId(long pkgId) {
        this.pkgId = pkgId;
    }

    public long getCfgId() {
        return cfgId;
    }

    public void setCfgId(long cfgId) {
        this.cfgId = cfgId;
    }

    public List<TestCaseCfgArgs> getTestCaseCfgArgs() {
        return testCaseCfgArgs;
    }

    public void setTestCaseCfgArgs(List<TestCaseCfgArgs> testCaseCfgArgs) {
        this.testCaseCfgArgs = testCaseCfgArgs;
    }

    public static class TestCaseCfgArgs extends SuperClass {
        @Field(id = 1, name = "id", type = NodeType.ATTR)
        private long tcId;

        /**
         * 配置细化到用例级别，如果为0，默认拿上层的配置
         */
        @Field(id = 2, name = "config", type = NodeType.ATTR)
        private long cfgId;


        public long getTcId() {
            return tcId;
        }

        public void setTcId(long tcId) {
            this.tcId = tcId;
        }

        public long getCfgId() {
            return cfgId;
        }

        public void setCfgId(long cfgId) {
            this.cfgId = cfgId;
        }
    }
}
