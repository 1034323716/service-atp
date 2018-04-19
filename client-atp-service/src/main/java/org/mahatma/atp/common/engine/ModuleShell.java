package org.mahatma.atp.common.engine;

import com.feinno.util.Combo2;
import org.mahatma.atp.common.Test;
import org.mahatma.atp.common.bean.Result;
import org.mahatma.atp.common.bean.Session;
import org.mahatma.atp.common.param.StartupOptionEnum;
import org.mahatma.atp.common.util.FormatUtil;
import org.mahatma.atp.common.util.TestInjector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;


/**
 * 用例的执行器
 */
public class ModuleShell {
    private static Logger LOGGER = LoggerFactory.getLogger(ModuleShell.class);
    private ModuleSummary summary;
    private Session session;
    private Test test;

    public ModuleShell(ModuleSummary summary) {
        this.summary = summary;
    }

    public void prepare(AutoTestEngine engine) {
        session = new Session();
        session.setStackManager(engine.getStackManager());
        session.setRunWatch(combo3 -> {
            if (StartupOptionEnum.TASKRESULTID.getArgs().size() != 0) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(combo3.getV1());
                stringBuilder.append("\n");
                stringBuilder.append(new String(combo3.getV2()));
                stringBuilder.append("\n");
                stringBuilder.append(new String(combo3.getV3()));
                stringBuilder.append("\n");
                LOGGER.info(stringBuilder.toString());
            }
        });
        Object o = TestInjector.injectTestByModuleSummary(summary);
        if (o != null) {
            test = (Test) o;
        } else {
            test = null;
        }
    }


    private boolean isSuccess;
    private Result result = null;

    public Combo2<Boolean, Result> run() {
        isSuccess = true;
        result = null;

        CountDownLatch countDownLatch = new CountDownLatch(2);
        final long startTime = System.currentTimeMillis();
        Thread runTC = new Thread(() -> {
            try {
                if (test != null) {
                    LOGGER.info(test.getClass().getSimpleName() + " test begin");
                    result = test.process(session);
                    LOGGER.info(test.getClass().getSimpleName() + " test finish");
                } else {
                    result = null;
                }
            } catch (Exception e) {
                // 如果用例中发生了异常,则result不会正常返回,所以重新创建一个
                if (result == null) {
                    result = session.createResult();
                }
                result.setCode(400);
                result.setDesc("happen exception");
                result.putStep("exception", e.getMessage());

                isSuccess = false;
                LOGGER.error(test.getClass().getSimpleName() + "run happen exception", e);
            }
            countDownLatch.countDown();
        });
        runTC.start();
        // 此线程用于监视这次tc测试
        new Thread(() -> {
            while (true) {
                if (countDownLatch.getCount() == 1) {
                    // 说明用例执行完了countDownLatch.countDown();会使主线程不再等待
                    countDownLatch.countDown();
                    break;
                } else if ((System.currentTimeMillis() - startTime) > FormatUtil.RUN_TC_TIMEOUT) {
                    // 这时没有运行完但超时了
                    countDownLatch.countDown();
                    countDownLatch.countDown();
                    runTC.interrupt();
                    isSuccess = false;
                    result = session.createResult();
                    result.setDesc("run test case : " + test.getClass().getName() + " time out");
                    result.setCode(400);
                    break;
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    LOGGER.error("此线程用于监视这次tc测试 sleep exception", e);
                }
            }
        }).start();
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            LOGGER.error("countDownLatch.await(); error : {}", e);
        }

        if (result == null) {
            result = session.createResult();
        }
        if (result.getCode() > FormatUtil.MAXSUCCESSCODE) {
            isSuccess = false;
        }

        return new Combo2<>(isSuccess, result);
    }
}
