package cms;

import java.util.List;
import java.util.Map;

import com.mongodb.Mongo;
import com.mongodb.MongoURI;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBCursor;
import com.mongodb.util.JSON;
import java.util.List;
import java.util.ArrayList;

import play.Play;
import play.Logger;

import java.util.Set;
import static cms.Strings.*;

import com.google.gson.reflect.TypeToken;
import com.google.gson.*;
import java.lang.reflect.Type;

import java.io.*;
import java.net.*;

import models.Site;

public class MongoDb{

    static Type type =
        new TypeToken<Map<String, Object>>(){}.getType();

    DB  db = null;
    String collection = "";
    Integer limit = null;
    Integer skip = null;
    DBObject query = null;
    
    List<Map> result = new ArrayList<Map>();

    String site;
    
    private MongoDb(String site){
        
        this.site = site;

        try{
            String uri = System.getenv("MONGOLAB_URI");
            MongoDbURI muri = new MongoDbURI( uri );
            Mongo mongo = new Mongo( muri.host, muri.port ); 
            if( muri.hasDb() ){ db = mongo.getDB( muri.database ); }
            if( muri.hasUserPass() ){ db.authenticate( muri.username, muri.password.toCharArray() ); }

        }catch(Exception e ){
            Logger.error(e, "Error creating a connection to mongodb");
        }

    }

    /**
     * not for sure why i started going down this path
     * i remember trying to match up the tested site with the local
     * mongo db collections
     */
    public static MongoDb get(){
        String host = Host.get();
        Site site = Site.findBySiteHost( host );
        return new MongoDb( site.getCollPrefix() );

    }

    public static MongoDb get( String host ){   
        return new MongoDb( host );
    }

    

    public MongoDb load(String collection, String file){
        InputStream is = null;
        try{
            URL url = this.getClass().getClassLoader().getResource( file );
            is = url.openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader( is ));
            
            String line = null;
            while( (line= br.readLine() )!=null ){
                save( collection, line );
            }

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{is.close();}catch(Exception e){}
        }

        return this;
    }   

    public MongoDb drop(){
        if( collection!=null ){            
            getCollection().drop();
        }
        return this;
    }

    protected DBCollection getCollection(){
        return db.getCollection( getSiteCollection( collection ) );
    }

    public MongoDb deleteDatabase(){
        if( db!=null ){ db.dropDatabase(); }
        return this;
    }

    protected String getSiteCollection( String coll ){
        return this.site + "_" + coll;
    }
    
    public MongoDb save( String collection, String obj ){
        Gson gson = new Gson();
        Map<String, Object> object =
            gson.fromJson( obj, type);     
        BasicDBObject o = new BasicDBObject( object );
        DBCollection coll = db.getCollection( getSiteCollection( collection ) );
        coll.save( o );
        return this;
    }

    public MongoDb save( String collection, Map object ){
        BasicDBObject o = new BasicDBObject( object );
        DBCollection coll = db.getCollection( getSiteCollection( collection ) );
        coll.save( o );
        return this;
    }

    public MongoDb with( String coll ){
        this.collection = coll;
        return this;
    }

    public MongoDb collection( String name ){
        this.collection = name;
        return this;
    }
    
    public MongoDb find( String query ){
        if( query!=null ){
            MongoQuery q = new MongoQuery( query );
            this.query = q.getDBObject();
        }
        return this;
    }
    
    public MongoDb limit(Integer limit){
        if( limit!=null ){
            this.limit = limit;
        }
        return this;
    }
    
    public MongoDb skip(Integer skip){
        if( skip!=null ){
            this.skip = skip;
        }
        return this;
    }
    
    public List<Map> fetch(){
        if( query!=null && limit!=null ){
            queryWithLimit();
        }else if( limit!=null ){
            allWithLimit();
        }else if( query!=null ){
            query();
        }else{
            all();
        }
        return result;
    }
    protected void all(){
        DBCursor cursor = db.getCollection( getSiteCollection(collection) ).find();
        if( skip!=null ){ cursor.skip( skip ); }
        while( cursor.hasNext() ){
            Map record = cursor.next().toMap();
            result.add ( record  );
        }        
    }
    
    protected void allWithLimit(){
        DBCursor cursor = db.getCollection( collection ).find().limit(limit);
        if( skip!=null ){ cursor.skip( skip ); }
        while( cursor.hasNext() ){
            Map record = cursor.next().toMap();
            result.add ( record  );
        }        
    }    
    
    protected void query(){
        DBCursor cursor = db.getCollection( collection ).find( query );
        if( skip!=null ){ cursor.skip( skip ); }        
        while( cursor.hasNext() ){
            Map record = cursor.next().toMap();
            result.add ( record  );
        }        
    }
    
    protected void queryWithLimit(){
        DBCursor cursor = db.getCollection( collection ).find( query ).limit( limit );
        if( skip!=null ){ cursor.skip( skip ); }        
        while( cursor.hasNext() ){
            Map record = cursor.next().toMap();
            result.add ( record  );
        }
    }
    

}