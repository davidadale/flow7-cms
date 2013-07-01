package cms;

import play.mvc.Scope;
import play.mvc.Http;
import models.*;

public class Host{
    
    public static String get(){
        
        String host = Scope.Session.current().get("_host");
        
        if( host==null ){
            host = Http.Request.current().host;
        }        

        //if( host==null ){
        //    host = "localhost";
        //}

        return host;
    }

    public static Boolean isManagedSite( String host ){

        Resource resource = Resource.find("byHost", host ).first();
        return ( resource != null );

    }
    
    public static String getSubDomain(String host){
        String[] parts = host.split("\\.");
        int length = parts.length-2;
        String result = "";
        for(int i=0;i<length;i++){
            result += " " + parts[i];
        }
        result = result.trim();
        result = result.replace(" ",".");
        return result;
    }
}