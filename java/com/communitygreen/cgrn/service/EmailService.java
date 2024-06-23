package com.communitygreen.cgrn.service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class EmailService {
    public boolean sendEmail(String subject,String message,String to){
        boolean f=false;
        String from="tousifsaqlain1160@gmail.com";
        String host="smtp.gmail.com";
        Properties properties=System.getProperties();
        System.out.println("Properties"+properties);
        properties.put("mail.smtp.host",host);
        properties.put("mail.smtp.port","465");
        properties.put("mail.smtp.ssl.enable","true");
        properties.put("mail.smtp.auth","true");
//        communitygreenofficial@gmail.com cybs vwer qjnd zpol
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("communitygreenofficial@gmail.com","hbfl zpmr albd cjkg");
            }
        });
        session.setDebug(true);
        MimeMessage m=new MimeMessage(session);
        try{
            m.setFrom(from);
            m.addRecipients(Message.RecipientType.TO, String.valueOf(new InternetAddress(to)));
            m.setSubject(subject);
//            m.setText(message);
            m.setContent(message,"text/html");
            Transport.send(m);
            System.out.println("sent successs.....");
            f=true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return f;
    }
}
