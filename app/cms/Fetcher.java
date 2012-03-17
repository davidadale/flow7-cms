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
            Logger.debug("Fetch Provider is null initializing a fetch provider.");
            initializeProvider();
        }
        
        return fetchProvider.getResources( site );
    }
    
    protected static void initializeProvider(){
        if( Play.configuration.containsKey("fetchProvider.class") ){
            Logger.debug("fetchProvider.class found in application.conf creating a new provider.");
            try{
                Class provider = Class.forName( Play.configuration.getProperty("fetchProvider.class") );
                fetchProvider = (FetchProvider) provider.newInstance();
            }catch(Exception e){ Logger.error( e,"Unable to create fetch provider." ); }
        }else{
            Logger.debug("No fetchProvider.class specified creating a default GitHub provider.");
            fetchProvider = new Github();
        }
    }
    
}