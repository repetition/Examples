package MainTest.cmd;

import java.util.Date;
import java.util.Properties;
import javax.mail.*;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class JavaMailSendMain {
/*    public static final String SMTPSERVER = "smtp.exmail.qq.com";
    public static final String SMTPPORT = "465";
    public static final String ACCOUT = "thinkwin-service@thinkwin.com.cn";
    public static final String PWD = "3Athinkwin";
    public static final String TOUser = "zhangbo@thinkwin.com.cn";*/
    public static final String SMTPSERVER = "218.26.224.150";
    public static final String SMTPPORT = "25";
    public static final String ACCOUT = "test1@sxzq.com";
    public static final String PWD = "xxjsb12345611";
    public static final String TOUser = "zhangbo@thinkwin.com.cn";



    public static void main(String[] args) throws Exception {
        System.out.println("SMTPSERVER:"+SMTPSERVER);
        System.out.println("SMTPPORT:"+SMTPPORT);
        System.out.println("ACCOUT:"+ACCOUT);
        System.out.println("PWD:"+PWD);
        System.out.println("TOUser:"+TOUser);


        testSendEmail();
    }

    public static void testSendEmail()  {

        // 创建邮件配置
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp"); // 使用的协议（JavaMail规范要求）
        props.setProperty("mail.smtp.host", SMTPSERVER); // 发件人的邮箱的 SMTP 服务器地址
        props.setProperty("mail.smtp.port", SMTPPORT);
        props.put("mail.smtp.auth", "true");

        boolean isSsl = false;
        if (isSsl) {
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.setProperty("mail.smtp.auth", "true"); // 需要请求认证
            props.setProperty("mail.smtp.ssl.enable", "true");// 开启ssl
        }

        // 根据邮件配置创建会话，注意session别导错包
        Session session = Session.getDefaultInstance(props);
    //    Session session = Session.getInstance(props, new MyAuthenricator(ACCOUT,PWD));
        // 开启debug模式，可以看到更多详细的输入日志
        session.setDebug(true);

        Transport transport1 = null;
        try {
          /*  transport1 = session.getTransport("smtp");
            transport1.connect(SMTPSERVER,ACCOUT, PWD);
            transport1.sendMessage(createEmail(session),createEmail(session).getAllRecipients());
            transport1.close();*/
            //创建邮件
            MimeMessage message = createEmail(session);
            //获取传输通道
            Transport transport = session.getTransport();
            transport.connect(SMTPSERVER,ACCOUT, PWD);
            //连接，并发送邮件
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static MimeMessage createEmail(Session session) throws Exception {
        // 根据会话创建邮件
        MimeMessage msg = new MimeMessage(session);
        // address邮件地址, personal邮件昵称, charset编码方式
        InternetAddress fromAddress = new InternetAddress(ACCOUT,
                "kimi", "utf-8");
        // 设置发送邮件方
        msg.setFrom(fromAddress);
        InternetAddress receiveAddress = new InternetAddress(
                TOUser, "test", "utf-8");
        // 设置邮件接收方
        msg.setRecipient(RecipientType.TO, receiveAddress);
        // 设置邮件标题
        msg.setSubject("测试标题", "utf-8");
        msg.setText("我是个程序员，一天我坐在路边一边喝水一边苦苦检查程序。 这时一个乞丐在我边上坐下了，开始要饭，我觉得可怜，就给了他1块钱。 然后接着调试程序。他可能生意不好，就无聊的看看我在干什么，然后过了一会，他缓缓地指着我的屏幕说，这里少了个分号");
        // 设置显示的发件时间
        msg.setSentDate(new Date());
        // 保存设置
        msg.saveChanges();
        return msg;
    }

    /**
     * 客户端程序自己实现Authenticator子类用于用户认证
     */
    static class MyAuthenricator extends Authenticator{
        String user="";
        String pass="";
        public MyAuthenricator(String user,String pass){
            this.user=user;
            this.pass=pass;
        }
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(user,pass);
        }

    }
}
