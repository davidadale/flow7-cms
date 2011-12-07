package cms;

import play.Play;

public class ResourceStoreFactory{
    
    private static ResourceStore instance = null;
    
    public static ResourceStore get(){
        if( instance == null ){
            if( "cloudfoundry".equals( Play.id ) ){
                instance = new CloudFoundryResourceStore();
            }
        }
        return instance;
    }
    

    
}