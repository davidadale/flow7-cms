package controllers;

import play.mvc.*;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import play.Logger;
import play.Play;
import play.exceptions.MailException;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.*;
import org.apache.commons.mail.SimpleEmail;

import models.*;
import cms.*;

public class Mailer extends Controller{
    
    public static void send(String name, String from, String next, String subject, String message){
        
        SimpleEmail email = new SimpleEmail();

        try{

            email.addTo( "info@imagine1.org" );
            email.setFrom( "info@imagine1.org" );
            email.setSubject( subject );
            
            StringBuilder msg = new StringBuilder( message );
            msg.append("\n\nName: " + name  + "\n");
            msg.append("Email: " + from);
            
            email.setMsg( msg.toString() );
        
        }catch(Exception e){
            e.printStackTrace();
        }
        
        System.out.println("Print out the host " + Host.get() );
        
        Site site = Site.findBySiteHost( Host.get() );
        Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", site.emailHost);
		props.put("mail.smtp.port", "587");
        try{
            
            Session session = Session.getInstance(props, new SMTPAuthenticator( site.emailUsername, site.emailPassword) );    
            email.setMailSession( session );
            email.setSentDate(new Date());
            email.send();
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        redirect( next );
        
    }
    
    public static class SMTPAuthenticator extends Authenticator {

            private String user;
            private String password;

            public SMTPAuthenticator(String user, String password) {
                this.user = user;
                this.password = password;
            }

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        }    
    
}