package controllers;

import play.mvc.*;

import com.mongodb.Mongo;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBCursor;
import com.mongodb.*;
import java.util.*;

import cms.*;


public class Data extends Controller{
    
    public static void put(String collection) {
        Map object = params.all();
        System.out.println("Printing out the params list " + object );
        System.out.println("Collection that was parsed is " + collection);
        object.remove("body");
        
        CMSForm form = CMSForm.createFromMap( params.all() );
        
        MongoDb db = new MongoDb();
        db.save( collection, form.fields );        
    }

    public static void get(String collection) {
        renderTemplate("Data/form.html");//, items );
    }

    public static void query( String collection, String query ){
        MongoDb db = new MongoDb();
        List<String> items = Collections.EMPTY_LIST; //db.query( collection, query );
        renderText( items );
    }
    
}

/**
//Set<Map.Entry<String,JsonElement>> properties = body.entrySet();
//for(Map.Entry prop: properties ){
//}
//Gson gson = new Gson();
//String json = gson.toJson( body );

//heroku_app2423536:g1gf1usm7it68qehbju2753f55
//Mongo m = new Mongo( "ds029277.mongolab.com" , 29277 );
//DB db = m.getDB( "heroku_app2423536" );
//db.authenticate("heroku_app2423536", "g1gf1usm7it68qehbju2753f55".toCharArray() );
//DBCollection members = db.getCollection("imagine1_members");

//doc.put("name", name);
//doc.put("country", country);
//doc.put("email", email);
//doc.put("letter", letter);

System.out.println("Collection " + collection + " is getting " + doc);
get( collection );
//members.save( doc ); 


//Mongo m = new Mongo( "ds029277.mongolab.com" , 29277 );
//heroku_app2423536
//DB db = m.getDB( "heroku_app2423536" );
//db.authenticate("heroku_app2423536", "g1gf1usm7it68qehbju2753f55".toCharArray() );
//DBCollection members = db.getCollection("imagine1_members");
//List items = members.find().toArray();




**/