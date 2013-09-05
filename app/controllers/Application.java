package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;
import jobs.*;
import play.cache.Cache;

import cms.*;
import static cms.Strings.*;
import java.io.File;
import play.test.*;

@With(Secure.class)
public class Application extends Controller {

    @Before(only={"sites"})
    static void setLocalSite(){
        if( "test".equals( Play.id ) ){
            Fixtures.deleteAllModels();
            new ListenToDirectory().doJob();
        }
    }


    public static void sites() {
        List<Site> sites = Site.all().fetch();
        render( sites );
    }
    
    public static void addSite(){
        Site site = new Site();
        renderTemplate("Application/editSite.html", site );
    }
    
    public static void addSecurityRule(Long id, String rule){
        Site site = Site.findById( id );
        SecurityRule sr = new SecurityRule( site.host, rule );
        sr.save();
        viewRules( site.id );
    }

    public static void deleteSecurityRule(Long id, Long site_id){
        SecurityRule sr = SecurityRule.findById( id );
        sr.delete();
        viewRules( site_id );
    }

    public static void editSite(Long id){
        Site site = Site.findById( id );
        render( site );        
    }
    
    public static void viewSite(Long id){
        Site site = Site.findById( id );
        renderTemplate("Application/info.html",site);
    }

    public static void manage(Long id){
        Site site = Site.findById( id );
        renderTemplate("Application/manage.html",site);
    }

    public static void upload(File file){
        Map result = new HashMap();
        result.put("success",true);
        System.out.println("File: " + file );
        renderJSON(result);
    }

    public static void viewResources(Long id){
        Site site = Site.findById( id );
        List<Resource> resources = Resource.findAllByHost(site.host);
        renderTemplate("Application/resources.html",site,resources);
    }

    public static void viewTransactions(Long id){
        Site site = Site.findById( id );
        List<RequestTransaction> transactions = RequestTransaction.findByHost( site.host );
        renderTemplate("Application/transactions.html",site,transactions);
    }

    public static void viewRules(Long id ){
        Site site = Site.findById( id );
        List<SecurityRule> rules = SecurityRule.findAllByHost( site.host );
        renderTemplate("Application/rules.html",site,rules);
    }

    public static void clearTransactions(Long id){
        Site site = Site.findById( id );
        List<RequestTransaction> transactions = RequestTransaction.findByHost( site.host );
        for( RequestTransaction rt: transactions ){
            rt.delete();
        }
        viewSite(id);
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