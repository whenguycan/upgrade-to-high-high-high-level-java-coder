package com.czdx.parkingorder.utils;

import com.czdx.parkingorder.utils.spring.SpringUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.annotation.PostConstruct;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.net.URL;
import java.util.Properties;

/**
 * <p>
 * 发送邮件工具类
 * </p>
 *
 * @author 琴声何来
 * @since 2023/4/13 9:42
 */
@Component
public class MailUtil {


    private static String DEFAULT_SMTP_HOST;

    private static String DEFAULT_FROM_MAIL;

    private static String DEFAULT_PASSWORD;

    private static final String DEFAULT_TITLE = "开具发票成功";

    private static final String DEFAULT_CONTENT = "尊敬的用户：\n\r感谢您使用停车服务并选择开票业务，查看发票请点击附件。\n\r祝您生活愉快！";

    @PostConstruct
    static void init() {
        Environment environment = SpringUtils.getBean("environment");
        DEFAULT_SMTP_HOST = environment.getProperty("mail.smtpHost");
        DEFAULT_FROM_MAIL = environment.getProperty("mail.fromMail");
        DEFAULT_PASSWORD = environment.getProperty("mail.password");
    }

    public static void send(String toMail, URL url) throws Exception {
        send(DEFAULT_SMTP_HOST, DEFAULT_FROM_MAIL, DEFAULT_PASSWORD, toMail, DEFAULT_TITLE, DEFAULT_CONTENT, url);
    }

    public static void send(String toMail, String title, String content, URL url) throws Exception {
        send(DEFAULT_SMTP_HOST, DEFAULT_FROM_MAIL, DEFAULT_PASSWORD, toMail, title, content, url);
    }

    public static void send(String toMail, String title, String content, File... files) throws Exception {
        send(DEFAULT_SMTP_HOST, DEFAULT_FROM_MAIL, DEFAULT_PASSWORD, toMail, title, content, files);
    }

    public static void send(String toMail, String title, String content, String filePath) throws Exception {
        send(DEFAULT_SMTP_HOST, DEFAULT_FROM_MAIL, DEFAULT_PASSWORD, toMail, title, content, new File(filePath));
    }

    public static void send(String smtpHost, String fromMail, String password, String toMail, String title, String content, String filePath) throws Exception {
        send(smtpHost, password, fromMail, toMail, title, content, new File(filePath));
    }

    public static void send(String smtpHost, String fromMail, String password, String toMail, String title, String content, File... files) throws Exception {
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "SMTP");
        props.setProperty("mail.host", smtpHost);
        props.setProperty("mail.smtp.auth", "true");

        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromMail, password);
            }
        };

        Session session = Session.getInstance(props, auth);
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromMail));
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(toMail));
        message.setSubject(title);

        // 邮件正文
        MimeBodyPart body = new MimeBodyPart();
        body.setContent(content, "text/html;charset=utf-8");

        // 将正文和附件放入multipart
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(body);

        // 准备附件
        for (File file : files) {
            DataSource source = new FileDataSource(file);
            DataHandler handler = new DataHandler(source);

            // 添加附件
            MimeBodyPart attachment = new MimeBodyPart();
            attachment.setDataHandler(handler);
            attachment.setFileName(MimeUtility.encodeText(handler.getName()));
            multipart.addBodyPart(attachment);
        }

        message.setContent(multipart);
        // 发送邮件
        Transport.send(message);
    }

    public static void send(String smtpHost, String fromMail, String password, String toMail, String title, String content, URL url) throws Exception {
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "SMTP");
        props.setProperty("mail.host", smtpHost);
        props.setProperty("mail.smtp.auth", "true");

        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromMail, password);
            }
        };

        Session session = Session.getInstance(props, auth);
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromMail));
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(toMail));
        message.setSubject(title);

        // 邮件正文
        MimeBodyPart body = new MimeBodyPart();
        body.setContent(content, "text/html;charset=utf-8");

        // 将正文和附件放入multipart
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(body);

        // 准备附件
        DataHandler handler = new DataHandler(url);

        // 添加附件
        MimeBodyPart attachment = new MimeBodyPart();
        attachment.setDataHandler(handler);
        attachment.setFileName(MimeUtility.encodeText(handler.getName()));
        multipart.addBodyPart(attachment);

        message.setContent(multipart);
        // 发送邮件
        Transport.send(message);
    }
}
