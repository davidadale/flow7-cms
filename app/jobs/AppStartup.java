package jobs;

import play.jobs.*;
import play.cache.*;
import play.*;
import models.*;

@OnApplicationStart
public class AppStartup extends Job{

    public void doJob(){
        System.out.println("***************************** AppStartup Fired. ");
        
        /*if( User.count()==0 ){
        	new User( "admin@flow7.net","changeme" ).save();
        	new User( "localhost","tester@flow7.net", "password").save();
        }*/

        Cache.forcedCacheImpl = cms.EHCacheExtension.newInstance();
        Cache.init();



    }
    
    
}