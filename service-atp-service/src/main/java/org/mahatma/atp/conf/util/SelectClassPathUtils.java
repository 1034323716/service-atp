package org.mahatma.atp.conf.util;

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
import org.mahatma.atp.common.db.daoImpl.PkgDaoImpl;
import org.mahatma.atp.common.db.daoImpl.TaskDaoImpl;
import org.mahatma.atp.common.db.daoImpl.TcsDaoImpl;
import org.mahatma.atp.common.util.FormatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLStreamException;
import java.util.HashSet;
import java.util.List;

/**
 * 原来的是// :/data/atp/atp-apps_allJar/2017-11-0211-36-28/decompression/*:/data/atp/atp-apps_allJar/2017-11-0211-36-28/lib/*
 * <p>
 * 全部没有进行null的判断
 * <p>
 * Created by JiYunfei on 17-10-17.
 */
public class SelectClassPathUtils {
    private static Logger LOGGER = LoggerFactory.getLogger(SelectClassPathUtils.class);

    private Database atpDB;

    public SelectClassPathUtils(Database atpDB) {
        this.atpDB = atpDB;
    }

    public String getClassPathsByTaskId(Long taskId) {
        StringBuilder stringBuilder = new StringBuilder();
        HashSet<Long> allPkgIdSet = getAllPkgIdSetByTaskId(taskId);
        for (Long pkgId : allPkgIdSet) {
            PkgDao pkgDao = new PkgDaoImpl(atpDB);
            Pkg pkg = pkgDao.get(pkgId);
            if (pkg != null) {
                String decompressionPath = FormatUtil.decompressionPath(pkg);
                String libPath = FormatUtil.libPath(pkg);
                stringBuilder.append(":" + decompressionPath);
                stringBuilder.append(":" + libPath);
                // todo 动态生成
                stringBuilder.append(":" + "/home/urcs/service-atp/lib/*");
            }
        }
        return stringBuilder.toString();
    }

    public HashSet<Long> getAllPkgIdSetByTaskId(Long taskId) {
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
