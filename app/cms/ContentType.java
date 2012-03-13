package cms;

import java.util.Map;
import java.util.HashMap;

import models.Resource;

public class ContentType{
    
	static Map<String,String> mimes = new HashMap<String,String>();
	
	static{
		mimes.put( "js",   "text/javascript");
		mimes.put( "htm",  "text/html");
		mimes.put( "html", "text/html");
		mimes.put( "css",  "text/css");		
		mimes.put( "xml",  "application/atom+xml");
		mimes.put( "gif",  "image/gif");
		mimes.put( "jpg",  "image/jpeg");
		mimes.put( "jpeg", "image/jpeg");
		mimes.put( "jpe",  "image/jpeg");				
		mimes.put( "png",  "image/png");
		mimes.put( "ico",  "image/x-icon");
		//mimes.put("", "");												
	}
	
	String path;
	
    public static boolean isBinary( String type ){
	    if ( type==null ){ return false; }
	
		return type.equals("image/gif") ||
		type.equals("image/jpeg") ||
		type.equals("image/png") ||
		type.equals("image/x-icon");
        
    }

	public static boolean isImage( Resource resource ){
	    return isBinary( resource.type );
	}
	
	public static boolean isStatic( Resource resource ){
	    return mimes.containsValue( resource.type );
	}
	
	public ContentType(String path){
		this.path = path;
	}
	
	public String getExt(){
		return path.substring( (path.lastIndexOf(".")+1), path.length() );
	}
	
	public String getType(){
		return mimes.get( getExt() );
	}
	public String toString(){
		return getType();
	}
	
}