package controllers;

import play.mvc.*;
import play.Logger;
import play.cache.Cache;
import org.apache.commons.lang.StringUtils;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import cms.*;
import models.Resource;
import java.util.Date;

// added to handle template files.
import play.templates.TemplateLoader;
import play.templates.BaseTemplate;

import play.Play;


public class Resources extends Controller{
    
    private static final String FORMAT = "%v %h %u [%t] \"%r\" %s %b \"%ref\" \"%ua\" %rt";
    
    public static void serve() throws IOException{
        
        Key key = Key.get();
        
        Logger.info("Request for resource " + key.toString() );
        
        Resource resource = ResourceCache.get( key );
        
        Logger.info("Resource retrieved from cache: " + (resource!=null) ); 
        if( resource!=null ){ Logger.info("Resource is stale: " + resource.stale ); }

        
        if( resource==null || resource.stale || Play.mode.isDev() ){
            resource = Resource.findByKey( key );
            
            Logger.info("Resource retrieved from storage: " + (resource!=null) );
            
            if( resource!=null ){ 
                
                if( resource.isBinary() ){
                    ResourceCache.add( resource );
                }
                
                response.contentType = resource.type;
                
                Logger.debug("Resource type is: " + response.contentType +
                             " resource is binary " + resource.isBinary() +
                             " resource data size is " + resource.data.length  );
                
                if( resource.isBinary() ){
                    response.cacheFor( resource.etag  , "1d" , resource.lastUpdate.getTime() );	
                    renderBinary( new ByteArrayInputStream( resource.data ) );
                }else if( resource.isHtml() ){
                    //response.cacheFor( resource.etag  , "1d" , resource.lastUpdate.getTime() );	
                    BaseTemplate bt = TemplateLoader.load(resource.path, new String( resource.data,"utf-8" ) );
                    response.print( bt.render() );
                    response.out.flush();
                }else{
                    response.out.write( resource.data );    
                    response.out.flush();                    
                }
                
            }
        
        }else{
            
            if( resource == null ){ notFound(); }
            
            notModified();            
        }
        

    }
    
    @Catch(IOException.class)
    public static void bad(){
        
    }
    
    
    @Finally
    public static void log(){
        
        Http.Request request = Http.Request.current();
        Http.Response response = Http.Response.current();
        
        long requestProcessingTime = System.currentTimeMillis() - request.date.getTime();
        
        Http.Header referrer = request.headers.get( HttpHeaders.Names.REFERER.toLowerCase() );
        Http.Header userAgent =  request.headers.get(HttpHeaders.Names.USER_AGENT.toLowerCase() );
        
        String bytes = "-";
        String status = "-";
        
        if( request.action!=null && response.out.size()>0){
            bytes = String.valueOf( response.out.size() );
            status = response.status.toString();
        }
        
        String line = FORMAT;
        line = StringUtils.replaceOnce( line, "%v", request.host );
        line = StringUtils.replaceOnce( line, "%h", request.remoteAddress );
        line = StringUtils.replaceOnce( line, "%u", (StringUtils.isEmpty(request.user))?"-":request.user );
        line = StringUtils.replaceOnce( line, "%t", request.date.toString() );
        line = StringUtils.replaceOnce( line, "%r", request.url );
        line = StringUtils.replaceOnce( line, "%s", status );
        line = StringUtils.replaceOnce( line, "%b", bytes );
        line = StringUtils.replaceOnce( line, "%ref", (referrer!=null)?referrer.value():"");                                                        
        line = StringUtils.replaceOnce( line, "%ua", (userAgent!=null)?userAgent.value():"" );                                                        
        line = StringUtils.replaceOnce( line, "%rt", String.valueOf(requestProcessingTime) + " ms");                                                                        
        
        System.out.println(  line );
    }    
    
}