package cms;

import java.util.List;
import java.util.Map;

import com.mongodb.Mongo;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBCursor;
import com.mongodb.util.JSON;
import java.util.List;
import java.util.ArrayList;


public class MongoDb{

    /*Mongo m = new Mongo( "127.0.0.1" , 27017 );
    DB db = m.getDB( "clientData" );     
    Map<String, String> params = Maps.newHashMap();
    params.put("name","david");
    params.put("phone","000-000-0000");
    params.put("email","david2@d.com");
    DBCollection coll = db.getCollection("testCollection");
    
    BasicDBObject doc = new BasicDBObject( params );
    
    coll.save( doc );*/
    DB  db = null;
    
    public MongoDb(){
        try{
            //heroku_app2423536:g1gf1usm7it68qehbju2753f55@ds029277.mongolab.com:29277/heroku_app2423536
            //Mongo m = new Mongo( "127.0.0.1" , 27017 );
            Mongo m = new Mongo("ds029277.mongolab.com", 29277 );
            db = m.getDB("heroku_app2423536");            
            db.authenticate("heroku_app2423536","g1gf1usm7it68qehbju2753f55".toCharArray() );
        }catch(Exception e ){
            
        }

    }
    
    public void save( String collection, Map object ){
        BasicDBObject o = new BasicDBObject( object );
        DBCollection coll = db.getCollection( collection );
        coll.save( o );
    }
    
    public List<String> query(String collection, String query){
        List<String> result = new ArrayList<String>();
        DBCursor cursor = db.getCollection( collection ).find( (DBObject) JSON.parse( query ) );
        while( cursor.hasNext() ){
            //Map i = cursor.next().toMap();
            String object = JSON.serialize( cursor.next() );
            System.out.println( object );
            
            result.add ( object  );
        }
        return result;
    }
    
    public List<String> list( String query ){
        return null;
    }
    
    public String get( String id ){
        return null;
    }
}