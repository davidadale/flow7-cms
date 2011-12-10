package cms;

import play.cache.Cache;
import models.Resource;

public class ResourceCache{
    
    public static Resource get( Key key ){
        Resource r = (Resource) Cache.get( key.toString() );
        if( r!=null ){
            r.stale = !r.etag.equals( key.etag );
        }
        return r;
    }
    
    public static void add( Resource resource ){
        Cache.add( resource.getKey().toString(), resource );
    }
    
    public static void remove(Resource resource){
        Cache.delete( resource.getKey().toString() );
    }
    
    
}