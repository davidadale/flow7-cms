package cms;

import play.Play;
import play.Logger;

import java.util.List;

import models.Site;
import models.Resource;

public abstract class Fetcher{
    
    public static FetchProvider fetchProvider = null;
    
    
    public static List<Resource> getResources ( Site site ){
        
        if( fetchProvider == null ){
            initializeProvider();
        }
        
        return fetchProvider.getResources( site );
    }
    
    protected static void initializeProvider(){
        if( Play.configuration.containsKey("fetchProvider.class") ){
            try{
                Class provider = Class.forName( Play.configuration.getProperty("fetchProvider.class") );
                fetchProvider = (FetchProvider) provider.newInstance();
            }catch(Exception e){ /*Logger.error( e );*/ }
        }else{
            fetchProvider = new Github();
        }
    }
    
}