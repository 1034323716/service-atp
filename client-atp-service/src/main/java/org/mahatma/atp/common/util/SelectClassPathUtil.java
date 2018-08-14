package org.mahatma.atp.common.util;

import com.feinno.superpojo.io.XmlInputStream;
import org.helium.database.Database;
import org.mahatma.atp.common.bean.PkgCfgArgs;
import org.mahatma.atp.common.bean.Summary;
import org.mahatma.atp.common.bean.Summarys;
import org.mahatma.atp.common.bean.superClassBuilder.SummaryBuilder;
import org.mahatma.atp.common.bean.superClassBuilder.SummarysBuilder;
import org.mahatma.atp.common.db.bean.Pkg;
import org.mahatma.atp.common.db.bean.Task;
import org.mahatma.atp.common.db.bean.Tcs;
import org.mahatma.atp.common.db.dao.PkgDao;
import org.mahatma.atp.common.db.dao.TaskDao;
import org.mahatma.atp.common.db.dao.TcsDao;
import org.mahatma.atp.common.db.impl.PkgDaoImpl;
import org.mahatma.atp.common.db.impl.TaskDaoImpl;
import org.mahatma.atp.common.db.impl.TcsDaoImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLStreamException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by JiYunfei on 17-10-17.
 */
public class SelectClassPathUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(SelectClassPathUtil.class);
    private Database atpDB;

    public SelectClassPathUtil(Database atpDB) {
        this.atpDB = atpDB;
    }

    public List<String> getClassPathsByTaskId(Long taskId) {
        List<String> jarStringList = new ArrayList<>();
        HashSet<Long> allPkgIdSet = getAllPkgIdSetByTaskId(taskId);
        for (Long pkgId : allPkgIdSet) {
            PkgDao pkgDao = new PkgDaoImpl(atpDB);
            Pkg pkg = pkgDao.get(pkgId);
            if (pkg != null) {
                jarStringList.add(FormatUtil.generatePkgDir(pkg));
            } else {
                LOGGER.error("pkg not in db , pkgId = " + pkgId);
            }

        }
        return jarStringList;
    }

    private HashSet<Long> getAllPkgIdSetByTaskId(Long taskId) {
        HashSet<Long> hashSet = new HashSet();
        TcsDao tcsDao = new TcsDaoImpl(atpDB);
        TaskDao taskDao = new TaskDaoImpl(atpDB);
        Task task = taskDao.get(taskId);
        if (task != null) {
            Summarys summarys = new Summarys();

            SummarysBuilder summarysBuilder = new SummarysBuilder(summarys);
            try {
                summarysBuilder.parseXmlFrom(XmlInputStream.newInstance(task.getSummarys().getBytes()));
            } catch (XMLStreamException e) {
                e.printStackTrace();
            }
            summarys = summarysBuilder.getData();
//            summarys.parseXmlFrom(task.getSummarys());

            for (Summary summary : summarys.getSummary()) {
                /*  <summary type = 1 id = 5/>
                    <summary type = 0>
                          <package id = "package主键" config = "config配置主键">
                              <entry id="entry用例主键"/>
                          </package>
                    </summary>
                */
                List<PkgCfgArgs> pkgCfgArgsList;
                if (summary.getType() == 1) {
                    Tcs tcs = tcsDao.get(summary.getId());
                    Summary summary1 = new Summary();

                    SummaryBuilder summaryBuilder = new SummaryBuilder(summary1);
                    try {
                        summaryBuilder.parseXmlFrom(XmlInputStream.newInstance(tcs.getSummary().getBytes()));
                    } catch (XMLStreamException e) {
                        e.printStackTrace();
                    }
                    summary1 = summaryBuilder.getData();
//                    summary1.parseXmlFrom(tcs.getSummary());

                    pkgCfgArgsList = summary1.getPkgCfgArgsList();
                } else {
                    pkgCfgArgsList = summary.getPkgCfgArgsList();
                }
                for (PkgCfgArgs pkgCfgArgs : pkgCfgArgsList) {
                    long pkgId = pkgCfgArgs.getPkgId();
                    hashSet.add(pkgId);
                }
            }
        } else {
            LOGGER.error("task not in db , taskId = " + taskId);
        }
        return hashSet;
    }
}
