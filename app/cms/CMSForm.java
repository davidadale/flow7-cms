package cms;

import java.util.*;

public class CMSForm{
    
    public Map<String,Object> fields = new LinkedHashMap<String,Object>();
    
    public CMSForm(){
        
    }
    
    public static CMSForm createFromMap(Map params){
        CMSForm form = new CMSForm();
        Iterator it = params.keySet().iterator();
        while( it.hasNext() ){
            String key = (String)it.next();
            if( key.startsWith("_") ){
                Object value = params.get( key );
                if( value instanceof String[] ){
                    if( ((String[])value).length > 1 ){
                        form.fields.put( remove_ (key), value );
                    }
                }else{
                    form.fields.put( remove_ (key), value );
                }
            }
        }
        
        return form;
    }
    
    
    
    protected static String remove_( String key ){
        if( key.startsWith("_") && !"_id".equals( key ) ){
            return key.substring( 1 );
        }
        return key;
    }
    
}