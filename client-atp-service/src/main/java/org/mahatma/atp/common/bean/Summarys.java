package org.mahatma.atp.common.bean;

import com.feinno.superpojo.SuperPojo;
import com.feinno.superpojo.annotation.Entity;
import com.feinno.superpojo.annotation.Field;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lyfx on 17-10-10.
 */
@Entity(name = "summarys")
public class Summarys extends SuperClass{
    @Field(id = 1)
    private List<Summary> summary = new ArrayList<>();

    public List<Summary> getSummary() {
        return summary;
    }

    public void setSummary(List<Summary> summary) {
        this.summary = summary;
    }

    public static void main(String[] args) {
        Summarys summarys = new Summarys();
        List<Summary> summaries = new ArrayList<>();
        Summary summary = new Summary();
        summary.setId(1);
        summary.setType(1);
        summaries.add(summary);
        Summary summary1 = new Summary();
        List<PkgCfgArgs> pkgCfgArgsList = new ArrayList<>();
        PkgCfgArgs pkgCfgArgs = new PkgCfgArgs();
        pkgCfgArgsList.add(pkgCfgArgs);
        summary1.setPkgCfgArgsList(pkgCfgArgsList);
        pkgCfgArgs.setCfgId(1);
        pkgCfgArgs.setPkgId(1);
        PkgCfgArgs.TestCaseCfgArgs testCaseCfgArgs = new PkgCfgArgs.TestCaseCfgArgs();
        testCaseCfgArgs.setCfgId(1);
        testCaseCfgArgs.setTcId(1);

        PkgCfgArgs.TestCaseCfgArgs testCaseCfgArgs1 = new PkgCfgArgs.TestCaseCfgArgs();
        testCaseCfgArgs1.setCfgId(2);
        testCaseCfgArgs1.setTcId(2);

        List<PkgCfgArgs.TestCaseCfgArgs> testCaseCfgArgsList = new ArrayList<>();
        testCaseCfgArgsList.add(testCaseCfgArgs);
        testCaseCfgArgsList.add(testCaseCfgArgs1);

        pkgCfgArgs.setTestCaseCfgArgs(testCaseCfgArgsList);
        summaries.add(summary1);

        summarys.setSummary(summaries);

        System.out.println(summarys.toXmlString());
        System.out.println(summary1.toXmlString());
    }

}
