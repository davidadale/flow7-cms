package cms;

import java.security.*;
import play.Logger;


public class Etag{
    
    public static String get( String site, String url, long modified  ){
        
        try{
            MessageDigest md5 = MessageDigest.getInstance("MD5");;    
            String unique = site + "-" + url + "-" + modified;
            byte[] hash = md5.digest( unique.getBytes() );
            
            StringBuffer hashString = new StringBuffer();
            for(int i=0;i<hash.length; ++i){
              String hex = Integer.toHexString( hash[i] );
              if(hex.length()==1){
                  hashString.append('0');
                  hashString.append( hex.charAt( hex.length()-1 ) );
              }  else{
                  hashString.append( hex.charAt( hex.length()-2 ) );
              }
            } 

            return hashString.toString();            
            
        }catch(Exception e){
            Logger.error(e, "Problem creating etag.");
        }

        return "";
    }    
    
}