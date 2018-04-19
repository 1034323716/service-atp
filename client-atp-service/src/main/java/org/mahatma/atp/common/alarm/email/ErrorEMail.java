package org.mahatma.atp.common.alarm.email;

import javax.mail.Authenticator;
import javax.mail.Message.RecipientType;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Calendar;
import java.util.Properties;

public class ErrorEMail {
    private EMailArgs emailArgs;

    public ErrorEMail(EMailArgs emailArgs) {
        this.emailArgs = emailArgs;
    }

    public void sendEMail(String emailMessageSubject, String emailMessageText) {
        try {
            // 1. 创建参数配置, 用于连接邮件服务器的参数配置
            Properties props = new Properties();                    // 参数配置
            props.setProperty("mail.smtp.host", emailArgs.getEmailSender());   // 发件人的邮箱的 SMTP 服务器地址
            props.put("mail.smtp.starttls.enable", "true");
            // SMTP 服务器的端口 (非 SSL 连接的端口一般默认为 25, 可以不添加, 如果开启了 SSL 连接,
            // 需要改为对应邮箱的 SMTP 服务器的端口, 具体可查看对应邮箱服务的帮助,
            // QQ邮箱的SMTP(SLL)端口为465或587, 其他邮箱自行去查看) final
            String smtpPort = "587";
            props.setProperty("mail.smtp.port", smtpPort);
            props.setProperty("mail.smtp.auth", "true");            // 需要请求认证
            // props.put("mail.debug", "true");//是否开启邮件发送调试日志

            // 2. 根据配置创建会话对象, 用于和邮件服务器交互
            Session session = Session.getInstance(props, new MyAuthenticator(emailArgs.getEmailSender(), emailArgs.getEmailPassword()));

            // 3. 创建一封邮件
            MimeMessage message = createMimeMessage(session, emailMessageSubject, emailMessageText);

            // 4. 根据 Session 获取邮件传输对象
            Transport transport = session.getTransport("smtp");

            // 5. 使用 邮箱账号 和 密码 连接邮件服务器, 这里认证的邮箱必须与 message 中的发件人邮箱一致, 否则报错
            //
            // PS_01: 成败的判断关键在此一句, 如果连接服务器失败, 都会在控制台输出相应失败原因的 log,
            // 仔细查看失败原因, 有些邮箱服务器会返回错误码或查看错误类型的链接, 根据给出的错误
            // 类型到对应邮件服务器的帮助网站上查看具体失败原因。
            //
            // PS_02: 连接失败的原因通常为以下几点, 仔细检查代码:
            // (1) 邮箱没有开启 SMTP 服务;
            // (2) 邮箱密码错误, 例如某些邮箱开启了独立密码;
            // (3) 邮箱服务器要求必须要使用 SSL 安全连接;
            // (4) 请求过于频繁或其他原因, 被邮件服务器拒绝服务;
            // (5) 如果以上几点都确定无误, 到邮件服务器网站查找帮助。
            //
            // PS_03: 仔细看log, 认真看log, 看懂log, 错误原因都在log已说明。
            transport.connect(emailArgs.getSmtpAddress(), emailArgs.getEmailSender(), emailArgs.getEmailPassword());

            // 6. 发送邮件, 发到所有的收件地址, message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
            transport.sendMessage(message, message.getRecipients(RecipientType.TO));

            // 7. 关闭连接
            transport.close();
        } catch (Exception e) {

        }
    }

    /**
     * 创建一封只包含文本的简单邮件
     *
     * @param session 和服务器交互的会话
     * @return
     * @throws Exception
     */
    public MimeMessage createMimeMessage(Session session, String emailMessageSubject, String emailMessageText) throws Exception {
        // 1. 创建一封邮件
        MimeMessage message = new MimeMessage(session);
        InternetAddress fromAddress = new InternetAddress(emailArgs.getEmailSender());
        String[] emailReceives = emailArgs.getEmailReceives().split(",");
        InternetAddress[] toAddresses = new InternetAddress[emailReceives.length];
        for (int i = 0; i < toAddresses.length; i++) {
            InternetAddress toAddress = new InternetAddress(emailReceives[i]);
            toAddresses[i] = toAddress;
        }

        // 2. From: 发件人（昵称有广告嫌疑，避免被邮件服务器误认为是滥发广告以至返回失败，请修改昵称）
        message.setFrom(fromAddress);

        // 3. To: 收件人（可以增加多个收件人、抄送、密送）
        message.setRecipients(RecipientType.TO, toAddresses);

        // 4. Subject: 邮件主题（标题有广告嫌疑，避免被邮件服务器误认为是滥发广告以至返回失败，请修改标题）
        message.setSubject(emailMessageSubject, "UTF-8");

        // 6. 设置发件时间
        message.setSentDate(Calendar.getInstance().getTime());

        message.setText(emailMessageText, emailArgs.getEmailMessageType());

        // 7. 保存设置
        message.saveChanges();
        return message;
    }

    public static void main(String[] args) throws Exception {
        EMailArgs emailArgs = new EMailArgs();
        emailArgs.setSmtpAddress("smtp.feinno.com");
        emailArgs.setEmailSender("jiyunfei@feinno.com");
        emailArgs.setEmailPassword("Zxcv321");
        // duanyongjian@feinno.com, "jiaxiaoxin@feinno.com", "wangshen@feinno.com","lilijc@feinno.com" });
        emailArgs.setEmailReceives("jiyunfei@feinno.com");
        // emailArgs.setSubject("【ATP】业务问题告警");
        // emailArgs.setEmailMessageType("text/html;charset=UTF-8");
        emailArgs.setEmailMessageType("GB2312");// UTF-8
        // emailArgs.setEmailMessageText("各位好：\n      用户你好, 今天全场5折, 快来抢购, 错过今天再等一年。。。");

        ErrorEMail email = new ErrorEMail(emailArgs);
        email.sendEMail("【ATP告警】", "各位好：\n      Test");
    }

    public static void send(String receives, String emailMessageSubject, String content) {
        EMailArgs emailArgs = new EMailArgs();
        emailArgs.setSmtpAddress("smtp.feinno.com");
        emailArgs.setEmailSender("jiyunfei@feinno.com");
        emailArgs.setEmailPassword("Zxcv321");
        emailArgs.setEmailReceives(receives);
        emailArgs.setEmailMessageType("GB2312");// UTF-8

        ErrorEMail email = new ErrorEMail(emailArgs);
        email.sendEMail(emailMessageSubject, content);
    }
}

class MyAuthenticator extends Authenticator {
    String userName = "";
    String password = "";

    public MyAuthenticator(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(userName, password);
    }
}