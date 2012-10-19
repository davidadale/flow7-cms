package controllers;


import org.apache.commons.mail.*; 
import play.*;
import play.mvc.*;
import play.libs.Mail;
import play.Logger;
import cms.*;
import models.*;


public class Api extends Controller{

	public static void sendMessage(String from, String subject, String message ){
		
		String to = "Flow7 Publishing<publishing@flow7.net>";

		Key key = Key.get();
		Site site = Site.findBySiteHost( key.host );
		if( site != null && site.emailUsername!=null ){
			to = site.emailUsername;
		}
	
		try{
			
			SimpleEmail email = new SimpleEmail();
			email.setFrom( from );
			email.addTo( to );
			email.setSubject( subject );
			email.setMsg( message );
			Mail.send(email);	

		}catch(Exception e){
			Logger.error( e, "Error sending email" );
		}
 

	}



}