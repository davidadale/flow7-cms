package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;
import jobs.*;
import play.cache.Cache;

import cms.*;

public class Application extends Controller {

    public static void sites() {
        List<Site> sites = Site.all().fetch();
        render( sites );
    }
    
    public static void addSite(){
        Site site = new Site();
        renderTemplate("Application/editSite.html", site );
    }
    
    public static void editSite(Long id){
        Site site = Site.findById( id );
        render( site );        
    }
    
    public static void viewSite(Long id){
        Site site = Site.findById( id );
        List<Resource> resources = Resource.findAllByHost( site.host );
        List<RequestTransaction> transactions = RequestTransaction.findByHost( site.host );
        render(site, resources, transactions);
    }
    
    public static void saveSite(Site site){
        site.save();
        sites();
    }
    
    public static void refresh( Long id ){
        new RefreshSiteJob( id ).now();
        sites();
    }
    
    public static void removeResources( Long id  ){
        Site site = Site.findById( id );
        site.finishRefresh();
        Resource.deleteAllByHost( site.host );
        sites();
    }
    
    public static void removeSite( Long id ){
        Site site = Site.findById( id );
        site.delete();
        Resource.deleteAllByHost( site.host );
        ResourceCache.dump();
        sites();
    }
    
    public static void removeResourceFromCache(Long siteId, Long resourceId ){
        Resource r = Resource.findById( resourceId );
        ResourceCache.remove( r );
        viewSite( siteId );
    }
    
    
    public static void preview(Long id ){
        
        Site site = Site.findById( id );
        
        if( site != null ){
            session.put( "_host" , site.host );			            
        }
        
        redirect("/");
        /*try{
            Key key = Key.get();
            key.path = null;
            
            System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& > " + key.toString() );
            
            Resources.serve( key );
        }catch(Exception e){
            e.printStackTrace();
            notFound();
        }*/
    }
    
    

}