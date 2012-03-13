package cms;


import play.mvc.Scope;
import play.mvc.Http;

public class Host{
    
    public static String get(){
        String site = Scope.Session.current().get("_host");
        if( site==null ){
            site = Http.Request.current().domain;
        }        
        return site;
    }
}