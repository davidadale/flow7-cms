package cms;

import play.i18n.Lang;
import models.*;
import java.io.ByteArrayInputStream;
import java.util.Properties;


public class Message{
    
    public static String get(String messageKey){
        Key key = new Key( Host.get(), getMessageBundle() );
        Resource resource = ResourceCache.get( key );
        
        if( resource == null ){
            resource =  Resource.findByKey( key );
        }
        
        if( resource==null ){
            return "";
        }
        
        try{
            ByteArrayInputStream bis = new ByteArrayInputStream( resource.data );
            Properties props = new Properties();
            props.load( bis );
            return props.getProperty( messageKey );            
        }catch(Exception e){
            
        }

        return "";
        
    }
    
    protected static String getMessageBundle(){
        return "/lang/messages" + getSuffix();
    }
    
    protected static String getSuffix(){
        String locale = Lang.get();
        if( locale == null || locale.length() == 0 ){ return "";}
        return "." + locale;
    }
}