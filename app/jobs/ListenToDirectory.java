package jobs;

import cms.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Date;
import models.*;
import org.apache.commons.vfs.*;
import org.apache.commons.vfs.impl.*;
import play.Play;
import play.jobs.*;

/**
 * This job is only used for live development. By setting the
 * id to 'live' it will allow the system to watch a directory 
 * and refresh the site as the files change. If the site is not
 * being developed locally this job will have no effect on the 
 * system.
 */
@OnApplicationStart(async=true)
public class ListenToDirectory extends Job{
    
    /**
     * Check to see if the site is in live mode and then set the 
     * Fetcther to use a development version pulling from the local
     * file system.
     */
    public void doJob() {
        
        if( "live".equals( Play.id ) ){
            
            String siteLocation = System.getProperty("site");
            if( siteLocation==null || siteLocation.length() == 0 ){
                throw new RuntimeException("Site location must be provided as a parameter 'site' when running in dev mode.");
            }
            
            File file = new File( siteLocation );
            
            if( !file.exists() ){
                throw new RuntimeException("Site provided is not a valid location. Invalid location provided -> " + siteLocation );
            }
            
            // setup directory listener.
            System.out.println("SYSTEM IS IN DEV MODE SO THE DEVELOPER FETCHER SHOULD BE USED AND THIS JOB IS GOING TO LISTEN TO DIRECTORY.");
            System.out.println("Path to use " +  siteLocation );
            Fetcher.fetchProvider = new DevelopmentFetchProvider( siteLocation );
            
            Site site = createSite( siteLocation );
            watchDirectory( siteLocation );
            refreshSite( site );
        }
        
     } 
     
     protected void watchDirectory(final String siteLocation ){
         try{
             FileSystemManager fsManager = VFS.getManager();
             final FileObject listendir = fsManager.resolveFile( (new File(siteLocation)).getAbsolutePath()  );

              DefaultFileMonitor fm = new DefaultFileMonitor(new FileListener(){
                  public void fileChanged(FileChangeEvent event) {

                      try{
                          String path = event.getFile().getName().getPath();
                          File siteDir = new File( siteLocation );
                          path  = path.replaceAll( siteDir.getCanonicalPath() , "" );
                          
                          Key key = new Key( siteLocation, path );
                          Resource resource = ResourceCache.get( key );
                          if( resource!=null ){
                                ResourceCache.remove( resource );
                          }

                          //System.out.println("Clean like " + path);
                          //TemplateLoader.cleanCompiledCache( path );
                          resource = Resource.findByKey( key );
                                                    
                          if( resource!=null ){
                              resource.data = toBytes( event.getFile().getContent().getInputStream() );
                              resource.save();
                          }
                          
                      }catch(Exception e){
                          e.printStackTrace();
                      }

                  }
                  public void fileDeleted(FileChangeEvent event) {
                      System.out.println("file deleted");                     
                  }
                  public void fileCreated(FileChangeEvent event) {
                      try{
                          

                      System.out.println("file created");    
                      String path = event.getFile().getName().getPath();
                      path = path.substring( siteLocation.length() );
                      Resource r = new Resource( siteLocation, path , toBytes( event.getFile().getContent().getInputStream()  ),
                       Etag.get( siteLocation , path, event.getFile().getContent().getLastModifiedTime() )  );    
                      r.lastUpdate = new Date( event.getFile().getContent().getLastModifiedTime() );
                      r.save();             
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                  }


              });
              fm.setRecursive(true);
              fm.addFile(listendir);
              fm.start();            
          }catch(Exception e){
              e.printStackTrace();
          }        
     }
     
     protected Site createSite(String siteLocation){
         Site site = new Site();
         site.host = siteLocation;
         site.save();
         return site;
     }   
     
     protected void refreshSite(Site site){
        new RefreshSiteJob( site.id ).now();         
     }
    
     /**
     * Duplicated method wiht DevelopmentFetchProvider, fix this later.
     */
    protected byte[] toBytes( InputStream is ){
        try{
         ByteArrayOutputStream out = new ByteArrayOutputStream();
         byte[] b = new byte[1024];
         int len = 0;
         while ((len = is.read(b)) != -1) {
             out.write(b, 0, len);
         }
            out.close();
 		    return out.toByteArray();
 		}catch(Exception e){
 		    
 		}
 		return new byte[0];
     } 
    
}