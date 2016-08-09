package net.muxi.huashiapp.common.util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * Created by ybao on 16/8/9.
 */
public class MailUtils {

    class MyAuthenticator extends javax.mail.Authenticator {
        private String strUser;
        private String strPwd;

        public MyAuthenticator(String user, String password) {
            this.strUser = user;
            this.strPwd = password;
        }

        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(strUser, strPwd);
        }
    }

    public void sendMail(String toMail, String fromMail, String server,
                         String username, String password, String title, String body
                         ) throws Exception {

        Properties props = System.getProperties();// Get system properties
        //添加邮箱地址。
        props.put("mail.smtp.host", server);// Setup mail server

        props.put("mail.smtp.auth", "true");
        //添加邮箱权限
        MyAuthenticator myauth = new MyAuthenticator(username, password);// Get

        Session session = Session.getDefaultInstance(props, myauth);

        MimeMessage message = new MimeMessage(session); // Define message
        //设置目的邮箱
        message.setFrom(new InternetAddress(fromMail)); // Set the from address

        message.addRecipient(Message.RecipientType.TO, new InternetAddress(
                toMail));// Set
        //设置邮件的标题
        message.setSubject(title);// Set the subject

        // message.setText(MimeUtility.encodeWord(body));// Set the content

        MimeMultipart allMultipart = new MimeMultipart("mixed");

        MimeBodyPart attachPart = new MimeBodyPart();
        //添加附件
//        FileDataSource fds = new FileDataSource(attachment);
//        attachPart.setDataHandler(new DataHandler(fds));//附件
//        attachPart.setFileName(MimeUtility.encodeWord(fds.getName()));

        MimeBodyPart textBodyPart = new MimeBodyPart();
        //添加邮件内容
        textBodyPart.setText(body);

        allMultipart.addBodyPart(attachPart);
        allMultipart.addBodyPart(textBodyPart);
        message.setContent(allMultipart);
        message.saveChanges();
        Transport.send(message);//发送邮件
    }
}
