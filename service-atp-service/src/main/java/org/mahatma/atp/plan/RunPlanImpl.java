package org.mahatma.atp.plan;

import com.feinno.superpojo.io.XmlInputStream;
import com.feinno.threading.ExecutorFactory;
import com.feinno.threading.FixedObservableExecutor;
import org.helium.database.Database;
import org.helium.framework.annotations.FieldSetter;
import org.helium.framework.annotations.ServiceImplementation;
import org.helium.framework.annotations.ServiceSetter;
import org.helium.framework.tag.Initializer;
import org.mahatma.atp.common.bean.PkgCfgArgs;
import org.mahatma.atp.common.bean.Summary;
import org.mahatma.atp.common.bean.Summarys;
import org.mahatma.atp.common.bean.superClassBuilder.SummaryBuilder;
import org.mahatma.atp.common.bean.superClassBuilder.SummarysBuilder;
import org.mahatma.atp.common.db.bean.Plan;
import org.mahatma.atp.common.db.bean.Task;
import org.mahatma.atp.common.db.bean.Tcs;
import org.mahatma.atp.common.db.dao.PlanDao;
import org.mahatma.atp.common.db.dao.TaskDao;
import org.mahatma.atp.common.db.dao.TcsDao;
import org.mahatma.atp.common.db.daoImpl.PlanDaoImpl;
import org.mahatma.atp.common.db.daoImpl.TaskDaoImpl;
import org.mahatma.atp.common.db.daoImpl.TcsDaoImpl;
import org.mahatma.atp.common.util.FormatUtil;
import org.mahatma.atp.conf.AtpEnvConfiguration;
import org.mahatma.atp.service.ControlTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLStreamException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by JiYunfei on 17-10-18.
 */
@ServiceImplementation
public class RunPlanImpl implements RunPlan {
    public static Logger LOGGER = LoggerFactory.getLogger(RunPlanImpl.class);

    private static final int TASK_EXECUTOR_SIZE = 16;

    private static FixedObservableExecutor executor;

    private static List<PlanArgs> atpPlans;

    private static PlanDao planDao;

    @FieldSetter("URCS_ATPDB")
    private Database atpDB;
    @ServiceSetter
    private Action action;
    @ServiceSetter
    private ControlTest controlTest;

    @Initializer
    public void init() {
        planDao = new PlanDaoImpl(atpDB);
        atpPlans = new ArrayList<>();
        startActionPlan();
        executor = (FixedObservableExecutor) ExecutorFactory.newFixedExecutor("Plan-Executor", TASK_EXECUTOR_SIZE, TASK_EXECUTOR_SIZE * 64);
        AtpPlanConsumer atpPlanConsumer = new AtpPlanConsumer();
        Thread thread = new Thread(atpPlanConsumer, "Select-Plan");
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void registerPlan(PlanBean planBean) {
        PlanArgs planArgs = new PlanArgs();
        planArgs.planBean = planBean;
        if (!existPlan(planBean)) {
            synchronized (atpPlans) {
                atpPlans.add(planArgs);
            }
        }
    }

    @Override
    public void removePlan(PlanBean planBean) {
        synchronized (atpPlans) {
            for (PlanArgs planArgs : atpPlans) {
                if (planArgs.planBean.getPlan().getId().equals(planBean.getPlan().getId())) {
                    atpPlans.remove(planArgs);
                }
            }
        }
    }

    public void removePlans(List<PlanArgs> willRemove) {
        synchronized (atpPlans) {
            for (PlanArgs planArgs : willRemove) {
                Long id = planArgs.planBean.getPlan().getId();
                controlTest.stopPlan(id);

            }
            atpPlans.removeAll(willRemove);
        }
    }

    @Override
    public boolean existPlan(PlanBean planBean) {
        synchronized (atpPlans) {
            for (PlanArgs planArgs : atpPlans) {
                if (planArgs.planBean.getPlan().getId().equals(planBean.getPlan().getId())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean pkgInPlan(Long pkgId) {
        for (PlanArgs atpPlan : atpPlans) {
            Plan plan = atpPlan.planBean.getPlan();
            Long taskId = plan.getTaskId();
            TaskDao taskDao = new TaskDaoImpl(atpDB);
            Task task = taskDao.get(taskId);
            String summarysString = task.getSummarys();
            Summarys summarys = new Summarys();
            SummarysBuilder summarysBuilder = new SummarysBuilder(summarys);
            try {
                summarysBuilder.parseXmlFrom(XmlInputStream.newInstance(summarysString.getBytes()));
            } catch (XMLStreamException e) {
                e.printStackTrace();
            }
            summarys = summarysBuilder.getData();
            for (Summary summary : summarys.getSummary()) {
                List<PkgCfgArgs> pkgCfgArgsList = null;
                if (summary.getType() == 1) {
                    TcsDao tcsDao = new TcsDaoImpl(atpDB);
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
                        pkgCfgArgsList = summary1.getPkgCfgArgsList();
                    }
                } else if (summary.getType() == 0) {
                    pkgCfgArgsList = summary.getPkgCfgArgsList();
                }
                if (pkgCfgArgsList != null) {
                    for (PkgCfgArgs pkgCfgArgs : pkgCfgArgsList) {
                        if (pkgId.equals(pkgCfgArgs.getPkgId())) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private static class PlanArgs {
        PlanBean planBean;
        String lastRunTime = "";
    }

    /**
     * 若是线上服务运行，则每次系统重启都从数据库中搂state是1,2的记录进list中执行，若是本地运行服务则不走这一步
     */
    private void startActionPlan() {
        if (!AtpEnvConfiguration.getInstance().isLocalStart()) {
            List<Plan> actionPlans = planDao.getActionPlan();
            for (Plan plan : actionPlans) {
                try {
                    PlanBean planBean = new PlanBean(plan, action);
                    registerPlan(planBean);
                } catch (ParseException e) {
                    LOGGER.error("start action plan error" + e);
                }
            }
        }
    }

    private class AtpPlanConsumer implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {

                    List<PlanArgs> willRemove = new ArrayList<>();

                    Date nowDate = new Date();
                    String nowText = DateFormatter.getScheduleDate(nowDate);
                    synchronized (atpPlans) {
                        for (PlanArgs planArgs : atpPlans) {
                            if (nowText.equals(planArgs.lastRunTime)) {
                                continue;
                            }
                            //为了应对在计划执行的过程中修改了cron所以在每次while（true）都重搂所有plan
                            Plan plan = reload(planArgs.planBean.getPlan());
                            Action action = planArgs.planBean.getAction();
                            if (plan != null) {
                                planArgs.planBean = new PlanBean(plan, action);
                                if (planArgs.planBean.getPlan() != null) {
                                    if (planArgs.planBean.getPlan().getState() == FormatUtil.runMark) {
                                        if (planArgs.planBean.getTriggerTime().isSatisfiedBy(nowDate)) {
                                            planArgs.lastRunTime = nowText;
                                            PlanBean planBean = planArgs.planBean;
                                            runPlan(planBean);
                                        }
                                    } else if (planArgs.planBean.getPlan().getState() == FormatUtil.pauseMark) {
                                    } else {
                                        willRemove.add(planArgs);
                                    }
                                } else {
                                    willRemove.add(planArgs);
                                }
                            } else {
                                willRemove.add(planArgs);
                            }
                        }
                        removePlans(willRemove);
                    }
                    Thread.sleep(200);
                }
            } catch (Throwable e) {
                LOGGER.error("AtpPlanConsumer run error!", e);
            }

        }
    }

    private void runPlan(PlanBean planBean) throws Exception {
        try {
            if (!controlTest.planIsRun(planBean.getPlan().getId())) {
                executor.execute(() -> planBean.getAction().process(planBean, controlTest));
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    private Plan reload(Plan plan) {
        return planDao.get(plan.getId());
    }
}
