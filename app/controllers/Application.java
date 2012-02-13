package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;
import jobs.*;

import cms.Key;

public class Application extends Controller {

    public static void index() {
        List<Site> sites = Site.all().fetch();
        render( sites );
    }
    
    public static void create(){
        Site site = new Site();
        render( site );
    }
    
    public static void save(Site site){
        site.save();
        index();
    }
    
    public static void refresh( Long id ){
        new RefreshSiteJob( id ).now();
        index();
    }
    
    public static void removeResources( String host  ){
        Site site = Site.findBySiteHost( host );
        site.finishRefresh();
        Resource.deleteAllByHost( host );
        index();
    }
    
    public static void removeSite( Long id ){
        Site site = Site.findById( id );
        site.delete();
        index();
        
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