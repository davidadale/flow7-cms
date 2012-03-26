package jobs;

import play.jobs.*;
import play.cache.*;

@OnApplicationStart
public class AppStartup extends Job{

    public void doJob(){
        System.out.println("***************************** AppStartup Fired. ");
        Cache.forcedCacheImpl = cms.EHCacheExtension.newInstance();
        Cache.init();
    }
    
    
}