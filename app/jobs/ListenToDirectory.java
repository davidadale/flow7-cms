package jobs;

import play.jobs.*;
import play.Play;
import play.templates.*;
import org.apache.commons.vfs.*;
import org.apache.commons.vfs.impl.*;
import java.io.File;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import models.*;
import java.util.Date;
import cms.*;

@OnApplicationStart(async=true)
public class ListenToDirectory extends Job{
    
    public void doJob() {
        
        if( Play.mode.isDev() ){
            
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
              FileObject listendir = fsManager.resolveFile( siteLocation  );


              DefaultFileMonitor fm = new DefaultFileMonitor(new FileListener(){
                  public void fileChanged(FileChangeEvent event) {
                      System.out.println("File changed.");
                      try{
                          String path = event.getFile().getName().getPath();
                          path = path.substring( siteLocation.length() );
                          Key key = new Key( siteLocation, path );
                          Resource resource = ResourceCache.get( key );
                          if( resource!=null ){
                                ResourceCache.remove( resource );
                          }
                          //System.out.println("Clean like " + path);
                          //TemplateLoader.cleanCompiledCache( path );
                          resource = resource.findByKey( key );
                          
                          if( resource!=null ){
                              resource.data = toBytes( event.getFile().getContent().getInputStream() );
                          }
                          resource.save();
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