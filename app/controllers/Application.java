package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;
import jobs.*;

import cms.Key;

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
        render(site, resources);
    }
    
    public static void saveSite(Site site){
        site.save();
        sites();
    }
    
    public static void refresh( Long id ){
        new RefreshSiteJob( id ).now();
        sites();
    }
    
    public static void removeResources( String host  ){
        Site site = Site.findBySiteHost( host );
        site.finishRefresh();
        Resource.deleteAllByHost( host );
        sites();
    }
    
    public static void removeSite( Long id ){
        Site site = Site.findById( id );
        site.delete();
        sites();
        
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