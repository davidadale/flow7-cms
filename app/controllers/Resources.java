package controllers;

import play.mvc.*;
import play.Logger;
import play.templates.TemplateLoader;
import play.templates.BaseTemplate;
import play.Play;

import org.apache.commons.lang.StringUtils;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import cms.*;
import models.Resource;
import models.RequestTransaction;

public class Resources extends Controller{
    
    private static final String FORMAT = "%v %h %u [%t] \"%r\" %s %b \"%ref\" \"%ua\" %rt";
    
    public static void serve() throws IOException{

        if( !Host.isManagedSite( Host.get() ) ){
            General.index();
        }

        Key key = Key.get();

        //if( key.shouldRedirect() ){
        //    redirect("/_cms");
        //}

        RequestTransaction rt = RequestTransaction.findByKey( key );

        Logger.debug("Request for resource " + key.toString() );
        
        // check to see if the resource is locked and if
        // the user is connected
        SecurityRules rules = SecurityRules.get( key );
        if( rules.secured( key ) && !UserSession.isConnected() ){
            //unauthorized();
            // redirect to login page
            redirect("/unauthorized.html");
        }

        Resource resource = ResourceCache.get( key );
        
        Logger.debug("Resource retrieved from cache: " + (resource!=null) ); 
        
        if( resource!=null ){ Logger.debug("Checking if resource is stale: " + resource.stale ); }

        if( resource!=null && !resource.stale && !Play.mode.isDev() ){
            rt.setResource( resource );
            notModified(); 
        }else{
            // this find by key may be problematic... need lots of tests on this.
            resource = Resource.findByKey( key );
            if(resource==null ){ rt.setNotFound(true); notFound(); }            
            
            rt.setResource( resource );            
            
            response.contentType = resource.type;                    
            Logger.debug("Resource type is: " + response.contentType +
                         " resource is binary " + resource.isBinary() );

            if( resource.isBinary() ){
                ResourceCache.add( resource ); 
                response.cacheFor( resource.etag  , "200d" , resource.lastUpdate.getTime() );	
                renderBinary( new ByteArrayInputStream( resource.data ) );
            }else if( resource.isHtml() ){
                BaseTemplate bt = TemplateLoader.loadString( new String( resource.data,"utf-8" ) );                
                response.print( bt.render() );
                response.out.flush();                                
            }else{
                response.out.write( resource.data );    
                response.out.flush();                
            } 
            
        }
        rt.incrementCount();
        rt.save();

    }


    
    public static void viewResource(Long id) throws IOException{
        Resource resource = Resource.findById( id );
        if( resource.isBinary() ){
            
            ResourceCache.add( resource ); 
            response.cacheFor( resource.etag  , "200d" , resource.lastUpdate.getTime() );	
            renderBinary( new ByteArrayInputStream( resource.data ) );
                            
        }else{
            
            response.out.write( resource.data );    
            response.out.flush();                
            
        }        
    }
    
    @Catch(IOException.class)
    public static void bad(){
        Logger.error("Something went wrong because IOException was thrown and caught in method 'bad()'");
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
        
        Logger.debug(  line );
    }    
    
}