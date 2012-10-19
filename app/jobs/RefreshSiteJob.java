package jobs;

import play.jobs.*;
import play.Logger;
import cms.*;

import java.util.List;

import models.Site;
import models.Resource;


public class RefreshSiteJob extends Job{
    
    Long id = null;
    
    public RefreshSiteJob(Long siteId ){
        this.id = siteId;
    }
    
    
    public void doJob(){
        
        Logger.info("Site is being refreshed.");
        
        Site site = Site.findById( id );
        
        if( site==null ){
            Logger.error("Site doesn't exist. Exiting refresh job.");
            return;
        }
        
        if(  site.isBusy() ){ 
            Logger.warn("Site is currently busy. Exiting refresh job.");
            return; 
        }
        
        site.startRefresh();
		
		List<Resource> remote = Fetcher.getResources( site );
		List<Resource> files = Resource.findAllByHost( site.host );
		
		Recorder recorder = RecorderFactory.get( site.host );
		recorder.startRecording();
		
		for(Resource r: remote){
                    if( files.contains( r ) ){
                        Resource file = files.get( files.indexOf( r ) );
                        if( file.isOld( r ) ){
                            recorder.recordUpdate( file, r );
                            file.refresh( r );
                            file.save();
                            ResourceCache.remove( file );
                        }
                    }else{
                        recorder.recordNew( r );
                        r.save();
                        files.add( r );
                    }
		}
		// remove all remote items from current list
		for( Resource temp: remote ){
		    files.remove( temp );
		}
		// remove all files that are no longer in remote.
		for( Resource file: files ){
		    recorder.recordDelete( file );
		    file.delete();
		}
		
		recorder.stopRecording();
		site.finishRefresh();
		
		recorder.flush();
				
        Logger.info("Finished refreshing site.");        
    }
    
}