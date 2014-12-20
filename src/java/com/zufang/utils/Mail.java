/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zufang.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Misaku
 */
public class Mail {

    private String host = "";
    private String user = "";
    private String password = "";

    private void setHost(String host) {
        this.host = host;
    }

    private void setAccount(String user, String password) {
        this.user = user;
        this.password = password;
    }

    public String send(String from, String to, String subject, String content) {
        String status = "ok";
        Properties props = new Properties();
        props.put("mail.smtp.host", host); // 指定SMTP服务器
        props.put("mail.smtp.auth", "true"); // 指定是否需要SMTP验证
        try {
            Session mailSession = Session.getDefaultInstance(props);
            Message message = new MimeMessage(mailSession);
            message.setFrom(new InternetAddress(from)); // 发件人
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to)); // 收件人
            message.setSubject(subject); // 邮件主题
            //指定邮箱内容及ContentType和编码方式
            message.setContent(content, "text/html;charset=utf-8");
            //指定邮件发送日期
            message.setSentDate(new Date());
            message.saveChanges();
            Transport transport = mailSession.getTransport("smtp");
            transport.connect(host, user, password);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (Exception e) {
            status = "failed: " + e.toString();
        }
        return status;
    }

    public String getMailContent(String username, String url) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        String mail
                = "亲爱的用户 <b>"
                + username + "</b>  您好:<br>您刚刚在租房神器注册了新账号，为确保您的账号正常使用，请点击以下链接激活您的账户：<br>"
                + url + "<br>（如果以上链接无法点击，请手工复制链接到浏览器地址栏中访问）<br><br><b>租房神器开发团队</b><br>"
                + format.format(new Date());
        return mail;
    }

    public static String sendVerifyMail(String reciver,String username,String url) {
        Mail sm = new Mail();
        sm.setHost("smtp.exmail.qq.com"); // 指定要使用的邮件服务器
        sm.setAccount("verify@sodaless.net", "zufang@2014"); // 指定帐号和密码
        /**
         * @param String 发件人的地址
         * @param String 收件人地址
         * @param String 邮件标题
         * @param String 邮件正文
         */
        return sm.send("verify@sodaless.net", reciver,"租房神器账户激活",sm.getMailContent(username,url));
    }
}
