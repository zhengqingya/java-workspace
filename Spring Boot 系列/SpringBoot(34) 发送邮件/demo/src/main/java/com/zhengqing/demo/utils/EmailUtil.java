package com.zhengqing.demo.utils;

import java.io.File;

import javax.annotation.PostConstruct;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import lombok.SneakyThrows;

/**
 * <p>
 * Email 邮件工具类
 * </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2020/8/14 17:31
 */
@Component
public class EmailUtil {

    private static JavaMailSender javaMailSender;

    private static String formEmail;

    @Value("${spring.mail.form}")
    private String formEmail2;

    @Autowired
    private JavaMailSender javaMailSender2;

    @PostConstruct
    public void beforeInit() {
        javaMailSender = javaMailSender2;
        formEmail = formEmail2;
    }

    /**
     * 发送邮件 - 不带附件
     *
     * @param title：邮件标题
     * @param content：
     *            邮件内容
     * @param sendTo:
     *            收件人
     * @return: void
     * @author : zhengqing
     * @date : 2020/8/14 19:28
     */
    @SneakyThrows(Exception.class)
    public static void sendMail(String title, String content, String... sendTo) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        // 发送人邮件地址
        helper.setFrom(formEmail);
        // 接收人邮件地址
        helper.setTo(sendTo);
        // 主题
        helper.setSubject(title);

        // html内容 (设置true标识发送html邮件)
        helper.setText(content, true);

        javaMailSender.send(message);
    }

    /**
     * 发送邮件 - 带附件
     *
     * @param title：邮件标题
     * @param content：
     *            邮件内容
     * @param attachmentFilename：附件文件名
     * @param file：附件
     * @param sendTo:
     *            收件人
     * @return: void
     * @author : zhengqing
     * @date : 2020/8/14 19:28
     */
    @SneakyThrows(Exception.class)
    public static void sendMail(String title, String content, String attachmentFilename, File file, String... sendTo) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        // 发送人邮件地址
        helper.setFrom(formEmail);
        // 接收人邮件地址
        helper.setTo(sendTo);
        // 主题
        helper.setSubject(title);

        // ① html内容 (设置true标识发送html邮件)
        helper.setText(content, true);

        // ② 附件
        if (StringUtils.isNotBlank(attachmentFilename)) {
            helper.addAttachment(attachmentFilename, file);
        }

        javaMailSender.send(message);
    }

}
