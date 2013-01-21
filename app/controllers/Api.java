package controllers;


import org.apache.commons.mail.*; 
import play.*;
import play.mvc.*;
import play.libs.Mail;
import play.Logger;
import cms.*;
import models.*;

import java.util.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.*;
import java.lang.reflect.Type;

import static cms.Strings.*;
import play.test.Fixtures;

public class Api extends Controller{

    static Type type =
        new TypeToken<Map<String, Object>>(){}.getType();  

    /**
      * Create an authenticated session with teh current user.
      *
      */ 
    public static void authenticate(String username, String password){      
        println("Just because i need to see...." + Host.get());
        println("just want to print this " + Http.Request.current().host );
        User user = User.findByHostAndUsername( Host.get() , username );  
        println("User is null " + (user==null) );
        println( "User is any users " + User.findAll() );
        if( user!=null && user.password.equals( password ) ){
            AuthToken auth = UserSession.newSession( user.id );
            renderText( "{success:true,auth:"+auth.token+"}" );
        }else{
            renderText( "{success:false,msg:'Authentication failed.'}" );
        }
    }

    /**
     * Add a document to the database for the specified collection.
     *
     */
    public static void addObject(String collection, String obj){        
        AuthToken auth = UserSession.connect();

        if( auth==null || !auth.valid() ){
            renderText("{success:false,msg:'Not authenticated.'}");
        }
        Gson gson = new Gson();
        Map<String, Object> map =
            gson.fromJson( obj, type);     

        MongoDb.get( auth.host ).save( collection , map );
        renderText("{success:true}");

    }

    /**
     * This method will need to upload images and resize to some size.
     *
     */
    public static void uploadImage(){

    }
    public static void findAll(String collection){
      AuthToken auth = UserSession.connect();
      if(auth==null || !auth.valid() ){
        renderText("{success:false,msg:'Not authenticated'}");
      }
      println("This is a find all method with no query parameter ");
      List<Map> items = MongoDb.get(auth.host).with(collection).fetch();
      Map<String,Object> result = new HashMap<String,Object>();
      result.put("success",Boolean.TRUE);
      result.put("items",items);
println("items " + items );      
      renderJSON( result );

    }
    /**
     * Find all the documents in the databse for this collection 
     * filtered by given query.
     *
     */
    public static void findAll(String collection, String query){
        AuthToken auth = UserSession.connect();
println("find all is called ");
        if( auth==null || !auth.valid() ){        
            renderText("{success:false,msg:'Not authenticated.'}");
        }

        List<Map> items = MongoDb.get( auth.host )
                                 .with( collection )
                                 .find( query ).fetch();

println("items listed are: " + items);        
        renderJSON( items );
    }

    /**
     * New user will create a site user by calling this method.
     *
     */
    public static void register(String username, String password){
        User user = new User( Host.get(),username,password );
        user.save();
    }

    /**
     * Email service to send out emails to site ownders etc...
     *
     */
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