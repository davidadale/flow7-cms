package cms;

import models.Site;
import models.Resource;

import java.util.List;
import java.io.File;
import java.util.Collections;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.io.FileInputStream;
import java.io.ByteArrayOutputStream;

public class DevelopmentFetchProvider implements FetchProvider{
    
    public String location = null;
    
    public DevelopmentFetchProvider(){
        
    }
    
    public DevelopmentFetchProvider(String location){
        this.location =  location;
    }
    
    
    public List<Resource> getResources( Site site ){
        List<Resource> resources = new ArrayList<Resource>();
        
        File root = new File( site.host );
        if( !root.exists() || !root.isDirectory() ){
            throw new RuntimeException("Site is not a valid directory. Can't fetch resources.");
        }

        walkDirectory( root , site, resources );
        return resources;
    }

    protected void walkDirectory( File file, Site site, List<Resource> resources ){
        
        if( file.isDirectory() ){
            if(".git".equals( file.getName() ) ){ return; }
            for(File temp: file.listFiles() ){
                walkDirectory( temp, site, resources );    
            }
        }else{
        
            try{
                String path = file.getPath().substring( site.host.length() );
        	Resource r = new Resource( site.host, path , toBytes( file ), Etag.get( site.host , path, file.lastModified() )  );
        	r.lastUpdate = new Date( file.lastModified() );
        	resources.add(  r );
            }catch(Exception e){
                // log problem
                e.printStackTrace();
            }   
        }

    }
    
    protected byte[] toBytes( File file )
            throws IOException, SecurityException, FileNotFoundException {
        FileInputStream fis = new FileInputStream( file );
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] b = new byte[1024];
        int len = 0;
        while ((len = fis.read(b)) != -1) {
            out.write(b, 0, len);
        }
        out.close();
		return out.toByteArray();
    }    
    
}