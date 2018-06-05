package org.mahatma.atp.common.alarm.email;

import org.helium.database.Database;
import org.mahatma.atp.common.alarm.weixin.WeiXinAlarm;
import org.mahatma.atp.common.bean.Result;
import org.mahatma.atp.common.db.bean.ATP_email;
import org.mahatma.atp.common.db.bean.Task;
import org.mahatma.atp.common.db.bean.TaskResultDetail;
import org.mahatma.atp.common.db.bean.Tc;
import org.mahatma.atp.common.db.dao.ATP_emailDao;
import org.mahatma.atp.common.db.daoImpl.ATP_emailDaoImpl;
import org.mahatma.atp.common.engine.AutoTestEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by JiYunfei on 18-2-5.
 */
public class AlarmTool {
    private static final Logger LOGGER = LoggerFactory.getLogger(AutoTestEngine.class);
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void sendErrorEmail(Database atpDatabase, Result result, Tc tc, Task task,
                                      TaskResultDetail taskResultDetail, int time, int runType) {
        if (runType == 1) {
            ATP_emailDao atp_emailDao = new ATP_emailDaoImpl(atpDatabase);
            List<ATP_email> atp_emails = atp_emailDao.listReceive();
            StringBuffer receives = new StringBuffer();
            for (int i = 0; i < atp_emails.size(); i++) {
                receives.append(atp_emails.get(i).getEmailAddress());
                if (i != (atp_emails.size() - 1)) {
                    receives.append(",");
                }
            }

            StringBuffer emailMessageSubject = new StringBuffer();
            emailMessageSubject.append("【ATP告警】");
            emailMessageSubject.append(tc.getNickname());
            emailMessageSubject.append("：");
            emailMessageSubject.append("第" + time + "次测试报错");

            StringBuffer content = new StringBuffer();
            content.append("各位好：");
            content.append("\n第" + time + "次测试报错");
            content.append("\n测试开始时间：" + format.format(taskResultDetail.getCreateTime()));
            content.append("\n用例信息：\n");
            content.append("      昵称：" + tc.getNickname() + "\n");
            content.append("      类名：" + tc.getName() + "\n");
            content.append("用例所在任务信息：\n");
            content.append("      名称：" + task.getName() + "\n");
            content.append("      描述：" + task.getDesc() + "\n");
            content.append("结果信息：\n");
            content.append("      结果码：" + result.getCode() + "\n");
            content.append("      结果描述：" + result.getDesc() + "\n");

            ErrorEMail.send(receives.toString(), emailMessageSubject.toString(), content.toString());
            try {
                WeiXinAlarm.send(content.toString());
            } catch (IOException e) {
                LOGGER.error("WeiXinAlarm Send Exception", e);
            }
            LOGGER.info("send success");
        }
    }

    public static void sendRecoveryEmail(Database atpDatabase, Result result, Tc tc, Task task,
                                         TaskResultDetail taskResultDetail, int time, int runType) {
        if (runType == 1) {
            ATP_emailDao atp_emailDao = new ATP_emailDaoImpl(atpDatabase);
            List<ATP_email> atp_emails = atp_emailDao.listReceive();
            StringBuffer receives = new StringBuffer();
            for (int i = 0; i < atp_emails.size(); i++) {
                receives.append(atp_emails.get(i).getEmailAddress());
                if (i != (atp_emails.size() - 1)) {
                    receives.append(",");
                }
            }

            StringBuffer emailMessageSubject = new StringBuffer();
            emailMessageSubject.append("【ATP告警】");
            emailMessageSubject.append(tc.getNickname());
            emailMessageSubject.append("：");
            emailMessageSubject.append("第" + time + "次测试恢复");

            StringBuffer content = new StringBuffer();
            content.append("各位好：");
            content.append("\n第" + time + "次测试恢复");
            content.append("\n测试开始时间：" + format.format(taskResultDetail.getCreateTime()));
            content.append("\n用例信息：\n");
            content.append("      昵称：" + tc.getNickname() + "\n");
            content.append("      类名：" + tc.getName() + "\n");
            content.append("用例所在任务信息：\n");
            content.append("      名称：" + task.getName() + "\n");
            content.append("      描述：" + task.getDesc() + "\n");
            content.append("结果信息：\n");
            content.append("      结果码：" + result.getCode() + "\n");
            content.append("      结果描述：" + result.getDesc() + "\n");

            ErrorEMail.send(receives.toString(), emailMessageSubject.toString(), content.toString());
            try {
                WeiXinAlarm.send(content.toString());
            } catch (IOException e) {
                LOGGER.error("WeiXinAlarm Send Exception", e);
            }
            LOGGER.info("send success");
        }
    }
}