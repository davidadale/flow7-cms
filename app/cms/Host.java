package cms;


import play.mvc.Scope;
import play.mvc.Http;

public class Host{
    
    public static String get(){
        String host = Scope.Session.current().get("_host");
        if( host==null ){
            host = Http.Request.current().domain;
        }        
        return host;
    }
    
    public static String getSubDomain(String host){
        String[] parts = host.split("\\.");
        int length = parts.length-2;
        System.out.println("Length in subdomain is " +  length);
        String result = "";
        for(int i=0;i<length;i++){
            result += " " + parts[i];
        }
        result = result.trim();
        result = result.replace(" ",".");
        return result;
    }
}