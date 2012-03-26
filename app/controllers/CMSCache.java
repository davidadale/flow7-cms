package controllers;

import play.mvc.*;
import cms.*;
import java.util.List;

public class CMSCache extends Controller{

    public static void index(){
        List keys = ResourceCache.keys();
        render( keys );
    }
    
    public static void dump(){
        ResourceCache.dump();
    }
    
    public static void remove(String key){
        ResourceCache.remove( key );
        index();
    }
}