package org.mahatma.atp.common.util;

import com.feinno.superpojo.io.XmlInputStream;
import com.feinno.util.Combo2;
import com.feinno.util.StringUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.helium.database.Database;
import org.mahatma.atp.common.Test;
import org.mahatma.atp.common.alarm.AlarmTool;
import org.mahatma.atp.common.bean.PkgCfgArgs;
import org.mahatma.atp.common.bean.Result;
import org.mahatma.atp.common.bean.Summary;
import org.mahatma.atp.common.bean.Summarys;
import org.mahatma.atp.common.bean.superclassbuilder.SummaryBuilder;
import org.mahatma.atp.common.bean.superclassbuilder.SummarysBuilder;
import org.mahatma.atp.common.db.bean.*;
import org.mahatma.atp.common.db.dao.*;
import org.mahatma.atp.common.db.impl.*;
import org.mahatma.atp.common.engine.AutoTestEngine;
import org.mahatma.atp.common.engine.ModuleSummary;
import org.mahatma.atp.common.engine.spi.AnnotationResolver;
import org.mahatma.atp.common.param.StartupOptionEnum;
import org.mahatma.atp.common.util.entity.ControlPkg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * 在线上运行时走的类
 * 每运行一次task就new一个这个类,都是一个shell
 * <p>
 * Created by JiYunfei on 17-10-10.
 */
public class RunUtil {
    private static Logger LOGGER = LoggerFactory.getLogger(RunUtil.class);

    private Database database;
    private AutoTestEngine autoTestEngine;
    private boolean isSuccess;
    private Long taskId;
    private long planId;
    private Long taskResultId;
    private Task task;
    private int runType;
    private Plan plan;

    public RunUtil(Database database, AutoTestEngine autoTestEngine) {
        this.database = database;
        this.autoTestEngine = autoTestEngine;
        isSuccess = true;
        taskId = Long.parseLong(StartupOptionEnum.TASKID.getArgs().get(0));
        planId = Long.parseLong(StartupOptionEnum.PLANID.getArgs().get(0));
        taskResultId = Long.parseLong(StartupOptionEnum.TASKRESULTID.getArgs().get(0));
        runType = Integer.parseInt(StartupOptionEnum.RUNTYPE.getArgs().get(0));
        PlanDao planDao = new PlanDaoImpl(database);
        plan = planDao.get(planId);
        try {
            notifyATPStart();
        } catch (IOException e) {
            LOGGER.error("Notify ATP start error", e);
        }

        // 从taskId中读取到所有的和这次任务有关的pkg，把源jar和jar中jar放入
//        SelectClassPathUtil selectClassPathUtil = new SelectClassPathUtil(database);
//        ExtClasspathLoader.loadClasspath(selectClassPathUtil.getClassPathsByTaskId(taskId));
    }

    private void notifyATPStart() throws IOException {
        LOGGER.info("Connect ATP for start");
        AtpControlClient atpControlClient = new AtpControlClient();
        atpControlClient.connection("localhost", 6688);

        ControlPkg controlPkg = new ControlPkg();
        controlPkg.setPid(ProcessUtil.getProcessID());
        controlPkg.setStatus(1);
        controlPkg.setTaskResultId(taskResultId);

        controlPkg.setTaskId(taskId);
        controlPkg.setPlanId(planId);
        controlPkg.setOperationTime(System.currentTimeMillis());

        atpControlClient.send(controlPkg.toPbByteArray());
        atpControlClient.disconnect();
    }

    private void notifyATPStop() throws IOException {
        LOGGER.info("Connect ATP for stop");
        AtpControlClient atpControlClient = new AtpControlClient();
        atpControlClient.connection("localhost", 6688);

        ControlPkg controlPkg = new ControlPkg();
        controlPkg.setPid(ProcessUtil.getProcessID());
        controlPkg.setStatus(0);
        controlPkg.setTaskResultId(taskResultId);
        controlPkg.setTaskId(taskId);
        controlPkg.setPlanId(planId);
        controlPkg.setOperationTime(System.currentTimeMillis());

        atpControlClient.send(controlPkg.toPbByteArray());
        atpControlClient.disconnect();
    }

    /**
     * 将ATP_task的summarys字段中的summary放到List中并遍历，依次传到下个方法中
     */
    public void startOnLine() {
        TaskDao taskDao = new TaskDaoImpl(database);
        task = taskDao.get(taskId);
        if (task != null) {
            if (task.getSummarys() != null && !("".equals(task.getSummarys()))) {
                Summarys summarys = new Summarys();

                SummarysBuilder summarysBuilder = new SummarysBuilder(summarys);
                try {
                    summarysBuilder.parseXmlFrom(XmlInputStream.newInstance(task.getSummarys().getBytes()));
                } catch (XMLStreamException e) {
                    e.printStackTrace();
                }
                summarys = summarysBuilder.getData();
//                summarys.parseXmlFrom(task.getSummarys());

                List<Summary> summaryList = summarys.getSummary();
                for (Summary summary : summaryList) {
                    startSummarys(summary);
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                isSuccess = false;
                LOGGER.error("task : " + taskId + " summarys : " + task.getSummarys());
            }
        } else {
            isSuccess = false;
            LOGGER.error("task not in db , taskId = " + taskId);
        }
        TaskResultDao taskResultDao = new TaskResultDaoImpl(database);
        TaskResult taskResult = new TaskResult();
        taskResult.setId(Long.parseLong(StartupOptionEnum.TASKRESULTID.getArgs().get(0)));
        taskResult.setFinishTime(new Date());
        taskResult.setState(0);
        if (isSuccess) {
            taskResult.setCode(FormatUtil.TASK_RESULT_SUCCESS);
            taskResult.setDesc(FormatUtil.TASK_RESULT_SUCCESS_TEXT);
        } else {
            taskResult.setCode(FormatUtil.TASK_RESULT_FAIL);
            taskResult.setDesc(FormatUtil.TASK_RESULT_FAIL_TEXT);
        }
        try {
            notifyATPStop();
        } catch (IOException e) {
            LOGGER.error("Notify ATP stop error", e);
        }
        taskResultDao.updateTaskResult(taskResult);
    }

    /**
     * 将summary中的package那层抛出来传入下个方法
     *
     * @param summary
     */
    private void startSummarys(Summary summary) {
        List<PkgCfgArgs> pkgCfgArgsList = null;
        TaskResultDetail taskResultDetail = new TaskResultDetail();
        taskResultDetail.setTaskResultId(Long.parseLong(StartupOptionEnum.TASKRESULTID.getArgs().get(0)));
        if (summary.getType() == 1) {
            taskResultDetail.setTcsId(summary.getId());
            TcsDao tcsDao = new TcsDaoImpl(database);
            Tcs tcs = tcsDao.get(summary.getId());
            if (tcs != null) {
                Summary summary1 = new Summary();

                SummaryBuilder summaryBuilder = new SummaryBuilder(summary1);
                try {
                    summaryBuilder.parseXmlFrom(XmlInputStream.newInstance(tcs.getSummary().getBytes()));
                } catch (XMLStreamException e) {
                    e.printStackTrace();
                }
                summary1 = summaryBuilder.getData();
//                summary1.parseXmlFrom(tcs.getSummary());

                pkgCfgArgsList = summary1.getPkgCfgArgsList();
            }
        } else if (summary.getType() == 0) {
            pkgCfgArgsList = summary.getPkgCfgArgsList();
        }
        if (pkgCfgArgsList != null) {
            for (PkgCfgArgs pkgCfgArgs : pkgCfgArgsList) {
                startSummary(pkgCfgArgs, taskResultDetail);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else {
            LOGGER.error("summary not hava <package>, summary : " + summary.toXmlString());
        }
    }

    /**
     * 将package中的<testCase id="28" config="18"/>遍历出来，一次传入下个方法
     *
     * @param pkgCfgArgs
     * @param taskResultDetail
     */
    private void startSummary(PkgCfgArgs pkgCfgArgs, TaskResultDetail taskResultDetail) {
        long cfgId = pkgCfgArgs.getCfgId();
        List<PkgCfgArgs.TestCaseCfgArgs> testCaseCfgArgs = pkgCfgArgs.getTestCaseCfgArgs();

        if (testCaseCfgArgs != null) {
            for (PkgCfgArgs.TestCaseCfgArgs testCaseCfgArg : testCaseCfgArgs) {
                testTCWrap(testCaseCfgArg, taskResultDetail, cfgId);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 加一层，用于控制tc重复执行
     *
     * @param testCaseCfgArg
     * @param taskResultDetail
     * @param cfgId
     */
    private void testTCWrap(PkgCfgArgs.TestCaseCfgArgs testCaseCfgArg, TaskResultDetail taskResultDetail, long cfgId) {
        PkgCfgDao pkgCfgDao = new PkgCfgDaoImpl(database);
        PkgCfg pkgCfg = pkgCfgDao.get(cfgId);

        long taskResultDetailId;
        TaskResultDetailDao taskResultDetailDao = new TaskResultDetailDaoImpl(database);

        Long tcCfgId = testCaseCfgArg.getCfgId();
        Long tcId = testCaseCfgArg.getTcId();
        if (tcCfgId != null && tcCfgId > 0 && pkgCfgDao.get(tcCfgId) != null) {
            autoTestEngine.addProperties(FormatUtil.ENV, jsonArrayStringToProperties(pkgCfgDao.get(tcCfgId).getProperties()));
        } else if (pkgCfg != null) {
            autoTestEngine.addProperties(FormatUtil.ENV, jsonArrayStringToProperties(pkgCfg.getProperties()));
        } else {
            autoTestEngine.addProperties(FormatUtil.ENV, null);
        }
        TcDao tcDao = new TcDaoImpl(database);
        Tc tc = tcDao.get(tcId);
        Class<? extends Test> clazz = null;
        if (tc != null) {
            try {
                clazz = (Class<? extends Test>) Thread.currentThread().getContextClassLoader().loadClass(tc.getClassPath());
            } catch (Exception e) {
                LOGGER.error("Get Test Class Exception : " + e.getMessage());
            }
        }

        taskResultDetail.setTcId(testCaseCfgArg.getTcId());
        taskResultDetail.setState(0);
        taskResultDetail.setCreateTime(new Date());
        taskResultDetailId = taskResultDetailDao.beginTestCase(taskResultDetail);
        Combo2<Boolean, Result> resultCombo2 = testTC(clazz);
        autoTestEngine.getReportManager().input(resultCombo2.getV2(), taskResultDetailId);
        if (resultCombo2.getV1() == false) {
            // 若运行失败
            // step1.发送报警邮件
            AlarmTool.sendErrorEmail(database, resultCombo2.getV2(), tc, task, taskResultDetail, 1, runType, plan);
            if (Integer.parseInt(StartupOptionEnum.RETEST.getArgs().get(0)) == 1) {
                // step2.再次测试
                LOGGER.error("线上运行失败，再次测试该用例。用例昵称{" + tc.getNickname() + "}，用例类名{" + tc.getClassPath() + "}");
                taskResultDetail.setTcId(testCaseCfgArg.getTcId());
                taskResultDetail.setState(0);
                taskResultDetail.setCreateTime(new Date());
                taskResultDetailId = taskResultDetailDao.beginTestCase(taskResultDetail);
                resultCombo2 = testTC(clazz);
                resultCombo2.getV2().setDesc(resultCombo2.getV2().getDesc() + "(Second Test)");
                autoTestEngine.getReportManager().input(resultCombo2.getV2(), taskResultDetailId);
                if (resultCombo2.getV1() == false) {
                    // 若再次测试还是失败,发送报警邮件
                    AlarmTool.sendErrorEmail(database, resultCombo2.getV2(), tc, task, taskResultDetail, 2, runType, plan);
                } else {
                    AlarmTool.sendRecoveryEmail(database, resultCombo2.getV2(), tc, task, taskResultDetail, 2, runType, plan);
                }
            }
        }
        if (isSuccess == true) {
            // 若还是失败则确定整个任务都失败
            isSuccess = resultCombo2.getV1();
        }
    }

    /**
     * 运行一个<testCase id="28" config="18"/>
     *
     * @param clazz
     * @return
     */
    private Combo2<Boolean, Result> testTC(Class<? extends Test> clazz) {
        return start(clazz);
    }

    private Combo2<Boolean, Result> start(Class<? extends Test> clazz) {
        ModuleSummary summary = AnnotationResolver.resolveInstance(clazz, autoTestEngine.getConfigProvider());
        Combo2<Boolean, Result> execute = autoTestEngine.execute(summary);
        return execute;
    }

    private Properties jsonArrayStringToProperties(String jsonArrayString) {
        Properties properties = new Properties();
        if (jsonArrayString != null && !StringUtils.EMPTY.equals(jsonArrayString)) {
            JsonParser jsonParser = new JsonParser();
            JsonArray jsonArray = (JsonArray) jsonParser.parse(jsonArrayString);
            Iterator<JsonElement> iterator = jsonArray.iterator();
            while (iterator.hasNext()) {
                JsonObject jsonObject = (JsonObject) iterator.next();
                if (jsonObject.get(FormatUtil.PKGCFGKEY) != null && jsonObject.get(FormatUtil.PKGCFGVALUE) != null) {
                    properties.setProperty(jsonObject.get(FormatUtil.PKGCFGKEY).getAsString()
                            , jsonObject.get(FormatUtil.PKGCFGVALUE).getAsString());
                }
            }
        }
        return properties;
    }
}
