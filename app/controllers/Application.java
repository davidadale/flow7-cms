package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;
import jobs.*;

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
    
    public static void remove( String host  ){
        Resource.deleteAllByHost( host );
        index();
    }
    
    

}