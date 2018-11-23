package org.mahatma.atp.common.alarm;

import com.feinno.util.Combo3;
import org.helium.database.Database;
import org.mahatma.atp.common.alarm.email.ErrorEMail;
import org.mahatma.atp.common.alarm.wechat.WeChatAlarm;
import org.mahatma.atp.common.bean.Result;
import org.mahatma.atp.common.db.bean.*;
import org.mahatma.atp.common.db.dao.AtpEmailDao;
import org.mahatma.atp.common.db.impl.AtpEmailDaoImpl;
import org.mahatma.atp.common.engine.AutoTestEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author JiYunfei
 * @date 18-2-5
 */
public class AlarmTool {
    private static final Logger LOGGER = LoggerFactory.getLogger(AutoTestEngine.class);
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final List<String> ALARM_TOKENS = Arrays.asList("失败", "fail", "error", "exception", "异常");

    public static void sendErrorEmail(Database atpDatabase, Result result, Tc tc, Task task,
                                      TaskResultDetail taskResultDetail, int time, int runType, Plan plan) {
        if (runType == 1) {
            if (plan.isAlarm()) {
                AtpEmailDao atpEmailDao = new AtpEmailDaoImpl(atpDatabase);
                List<AtpEmail> atpEmails = atpEmailDao.listReceive();
                StringBuffer receives = new StringBuffer();
                for (int i = 0; i < atpEmails.size(); i++) {
                    receives.append(atpEmails.get(i).getEmailAddress());
                    if (i != (atpEmails.size() - 1)) {
                        receives.append(",");
                    }
                }

                StringBuffer emailMessageSubject = new StringBuffer();
                emailMessageSubject.append("【ATP告警】");
                emailMessageSubject.append(tc.getNickname());
                emailMessageSubject.append("：");
                emailMessageSubject.append("第" + time + "次测试报错");

                StringBuffer content = new StringBuffer();
                content.append("测试结果ID：" + taskResultDetail.getTaskResultId());
                content.append("\n第" + time + "次测试报错");
                content.append("\n测试开始时间：" + format.format(taskResultDetail.getCreateTime()));
                content.append("\n用例信息：\n");
                content.append("      昵称：" + tc.getNickname() + "\n");
                content.append("      类名：" + tc.getName() + "\n");
                content.append("用例所在任务名：" + task.getName() + "\n");
                content.append("结果信息：\n");
                content.append("      结果码：" + result.getCode() + "\n");
                content.append("      结果描述：" + result.getDesc() + "\n");
                content.append("      错误步骤：\n" + getFailStep(result));

                try {
                    boolean send = WeChatAlarm.send(content.toString());
                    if (!send) {
                        ErrorEMail.send(receives.toString(), emailMessageSubject.toString(), content.toString());
                    }
                } catch (IOException e) {
                    LOGGER.error("WeChatAlarm Send Exception", e);
                }
                LOGGER.info("send success");
            }
        }
    }

    public static void sendRecoveryEmail(Database atpDatabase, Result result, Tc tc, Task task,
                                         TaskResultDetail taskResultDetail, int time, int runType, Plan plan) {
        if (runType == 1) {
            if (plan.isAlarm()) {
                AtpEmailDao atpEmailDao = new AtpEmailDaoImpl(atpDatabase);
                List<AtpEmail> atpEmails = atpEmailDao.listReceive();
                StringBuffer receives = new StringBuffer();
                for (int i = 0; i < atpEmails.size(); i++) {
                    receives.append(atpEmails.get(i).getEmailAddress());
                    if (i != (atpEmails.size() - 1)) {
                        receives.append(",");
                    }
                }

                StringBuffer emailMessageSubject = new StringBuffer();
                emailMessageSubject.append("【ATP告警】");
                emailMessageSubject.append(tc.getNickname());
                emailMessageSubject.append("：");
                emailMessageSubject.append("第" + time + "次测试恢复");

                StringBuffer content = new StringBuffer();
                content.append("测试结果ID：" + taskResultDetail.getTaskResultId());
                content.append("\n第" + time + "次测试恢复");
                content.append("\n测试开始时间：" + format.format(taskResultDetail.getCreateTime()));
                content.append("\n用例信息：\n");
                content.append("      昵称：" + tc.getNickname() + "\n");
                content.append("      类名：" + tc.getName() + "\n");
                content.append("用例所在任务名：" + task.getName() + "\n");
                content.append("结果信息：\n");
                content.append("      结果码：" + result.getCode() + "\n");
                content.append("      结果描述：" + result.getDesc() + "\n");
                content.append("      错误步骤：\n" + getFailStep(result));

                ErrorEMail.send(receives.toString(), emailMessageSubject.toString(), content.toString());
                try {
                    WeChatAlarm.send(content.toString());
                } catch (IOException e) {
                    LOGGER.error("WeChatAlarm Send Exception", e);
                }
                LOGGER.info("send success");
            }
        }
    }

    private static String getFailStep(Result result) {
        StringBuffer stringBuffer = new StringBuffer();
        List<Combo3<Integer, byte[], byte[]>> stepData = result.getStepData();
        Iterator<Combo3<Integer, byte[], byte[]>> iterator = stepData.iterator();
        while (iterator.hasNext()) {
            Combo3<Integer, byte[], byte[]> date = iterator.next();
            String request = new String(date.getV2()).toLowerCase();
            String response = new String(date.getV3()).toLowerCase();
            if (biuAlarmToken(request, response)) {
                String s = Result.formatStep(date);
                stringBuffer.append(s);
            }
        }
        return stringBuffer.toString();
    }

    private static boolean biuAlarmToken(String... strs) {
        for (String alarmToken : ALARM_TOKENS) {
            for (String str : strs) {
                if (str.contains(alarmToken)) {
                    return true;
                }
            }
        }
        return false;
    }
}