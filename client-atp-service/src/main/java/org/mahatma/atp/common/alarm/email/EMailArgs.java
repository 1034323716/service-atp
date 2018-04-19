package org.mahatma.atp.common.alarm.email;

public class EMailArgs {
    // 发件人的 邮箱 和 密码（替换为自己的邮箱和密码）
    // PS: 某些邮箱服务器为了增加邮箱本身密码的安全性，给 SMTP 客户端设置了独立密码（有的邮箱称为“授权码”）,
    // 对于开启了独立密码的邮箱, 这里的邮箱密码必需使用这个独立密码（授权码）。
    private String emailSender;
    private String emailPassword;
    // 发件人邮箱的 SMTP 服务器地址, 必须准确, 不同邮件服务器地址不同, 一般(只是一般, 绝非绝对)格式为: smtp.xxx.com
    // 网易163邮箱的 SMTP 服务器地址为: smtp.163.com
    private String smtpAddress;
    // 收件人邮箱（替换为自己知道的有效邮箱）
    private String emailReceives;
    // private String subject;// 邮件主题
    // private String emailMessageText;// 邮件内容
    private String emailMessageType;

    public String getEmailSender() {
        return emailSender;
    }

    public void setEmailSender(String emailSender) {
        this.emailSender = emailSender;
    }

    public String getEmailPassword() {
        return emailPassword;
    }

    public void setEmailPassword(String emailPassword) {
        this.emailPassword = emailPassword;
    }

    public String getSmtpAddress() {
        return smtpAddress;
    }

    public void setSmtpAddress(String smtpAddress) {
        this.smtpAddress = smtpAddress;
    }

    // public String getSubject() {
    // return subject;
    // }
    //
    // public void setSubject(String subject) {
    // this.subject = subject;
    // }
    //
    // public String getEmailMessageText() {
    // return emailMessageText;
    // }
    //
    // public void setEmailMessageText(String emailMessageText) {
    // this.emailMessageText = emailMessageText;
    // }

    public String getEmailReceives() {
        return emailReceives;
    }

    public void setEmailReceives(String emailReceives) {
        this.emailReceives = emailReceives;
    }

    public String getEmailMessageType() {
        return emailMessageType;
    }

    public void setEmailMessageType(String emailMessageType) {
        this.emailMessageType = emailMessageType;
    }
}
