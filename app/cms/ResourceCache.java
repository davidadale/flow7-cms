package cms;

import play.cache.*;
import models.Resource;
import java.util.List;


public class ResourceCache {
    
    public static Resource get( Key key ){
        Resource r = (Resource) Cache.get( key.toString() );
        if( r!=null ){
            r.stale = !r.etag.equals( key.etag );
        }
        return r;
    }
    
    public static void dump(){
        Cache.clear();
    }
    
    public static void add( Resource resource ){
        Cache.add( resource.getKey().toString(), resource );
    }
    
    public static void remove(Resource resource){
        remove( resource.getKey().toString() );
    }
    
    public static void remove(String key){
        Cache.delete( key );
    }
    
    public static List keys(){
        return ((EHCacheExtension)Cache.cacheImpl).getKeys();
    }
    
}