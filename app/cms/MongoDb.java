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

public class MongoDb{

    DB  db = null;
    String collection = "";
    Integer limit = null;
    Integer skip = null;
    DBObject query = null;
    List<Map> result = new ArrayList<Map>();
    
    public MongoDb(){
        try{
            
            /*if( Play.mode.isDev() ){
                Mongo m = new Mongo();
                db = m.getDB("clientData");
            }else{*/
                //String uri = System.getenv("MONGOLAB_URI");
                //            mongodb://heroku_app2423536:g1gf1usm7it68qehbju2753f55@ds029277.mongolab.com:29277/heroku_app2423536
                String uri = "mongodb://heroku_app2423536:g1gf1usm7it68qehbju2753f55@ds029277.mongolab.com:29277/heroku_app2423536";
                DbURI muri = new DbURI( uri );
                Mongo mongo = new Mongo( muri.host, muri.port );
                db = mongo.getDB( muri.database );
                db.authenticate( muri.username, muri.password.toCharArray() );
                //db = m.connectDB();
            //}

        }catch(Exception e ){
            Logger.error(e, "Error creating a connection to mongodb");
        }

    }
    
    public void save( String collection, Map object ){
        BasicDBObject o = new BasicDBObject( object );
        DBCollection coll = db.getCollection( collection );
        coll.save( o );
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
        DBCursor cursor = db.getCollection( collection ).find();
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