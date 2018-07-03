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

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


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

        CountDownLatch countDownLatch = new CountDownLatch(1);
        new Thread(() -> {
            try {
                if (test != null) {
                    LOGGER.info(test.getClass().getSimpleName() + " test begin");
                    result = test.process(session);
                    LOGGER.info(test.getClass().getSimpleName() + " test finish");
                } else {
                    result = session.createResult();
                    result.setCode(500);
                    result.setDesc(summary.getClassName() + "用例加载失败");
                }
            } catch (Exception e) {
                // 如果用例中发生了异常,则result不会正常返回,所以重新创建一个
                if (result == null) {
                    result = session.createResult();
                }
                result.setCode(501);
                result.setDesc(summary.getClassName() + "用例发生未捕获的异常");
                result.putStep("exception", getErrorInfoFromException(e));

                isSuccess = false;
                LOGGER.error(test.getClass().getSimpleName() + "run happen exception", e);
            }
            countDownLatch.countDown();
        }).start();
        try {
            countDownLatch.await(10, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            LOGGER.error("countDownLatch.await(10, TimeUnit.MINUTES); error : {}", e);
        }

        if (result == null) {
            result = session.createResult();
        }
        if (result.getCode() > FormatUtil.MAXSUCCESSCODE || result.getCode() < FormatUtil.MINSUCCESSCODE) {
            isSuccess = false;
        }

        return new Combo2<>(isSuccess, result);
    }

    public String getErrorInfoFromException(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        try {
            e.printStackTrace(pw);
            return sw.toString();
        } catch (Exception exception) {
            return "ErrorInfoFromException";
        } finally {
            try {
                sw.close();
                pw.close();
            } catch (IOException ioe) {
                return "ErrorInfoFromException";
            }
        }

    }
}
