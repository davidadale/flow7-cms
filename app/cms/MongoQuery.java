package cms;

import com.mongodb.DBObject;
import com.mongodb.BasicDBObject;
import groovy.lang.GroovyShell;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Iterator;
public class MongoQuery{

    GroovyShell shell = null;
    DBObject object = null;
    
    public MongoQuery(String expr ){
        shell = new GroovyShell();
        String queryString = expr.replace( "$", "_" );
        Map query = (Map)  shell.evaluate( queryString );
        object = buildDBObject( query );
    }
    
    public DBObject getDBObject(){
        return object;
    }
    
    protected DBObject buildDBObject( Map object ){
        Map result = new LinkedHashMap();
        
        Iterator it = object.keySet().iterator();
        while( it.hasNext() ){
            String key = (String)it.next();
            Object value = object.get( key );
            if( value instanceof Map ){
                result.put( buildKey(key), buildDBObject( (Map) value) );
            }else{
                result.put( buildKey(key), value );
            }
        }
        
        return new BasicDBObject( result );
        
    }
    
    protected String buildKey(String key ){
        if( key.startsWith("_") ){
            return "$" + key.substring(1);
        }else{
            return key;
        }
    }
    
}