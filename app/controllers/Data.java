package controllers;

import play.mvc.*;

import com.mongodb.Mongo;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBCursor;
import com.mongodb.*;
import java.util.List;

public class Data extends Controller{
    
    public static void put(String name,String country, String email, String letter) throws Exception{
        //Set<Map.Entry<String,JsonElement>> properties = body.entrySet();
        //for(Map.Entry prop: properties ){
        //}
        //Gson gson = new Gson();
        //String json = gson.toJson( body );
        
        Mongo m = new Mongo( "ds029277.mongolab.com" , 29277 );
        DB db = m.getDB( "heroku_app2423536" );
        db.authenticate("david@flow7.net", "pauline".toCharArray() );
        DBCollection members = db.getCollection("imagine1_members");
        BasicDBObject doc = new BasicDBObject();
        
        doc.put("name", name);
        doc.put("country", country);
        doc.put("email", email);
        doc.put("letter", letter);
        
        members.save( doc ); 
    }
    //JSON.serialize( items )
    public static void get() throws Exception{
        
        Mongo m = new Mongo( "ds029277.mongolab.com" , 29277 );
        //heroku_app2423536
        DB db = m.getDB( "heroku_app2423536" );
        db.authenticate("david@flow7.net","pauline".toCharArray());        
        DBCollection members = db.getCollection("imagine1_members");
        List items = members.find().toArray();
        renderTemplate("Data/form.html", items );
        
    }

    
}