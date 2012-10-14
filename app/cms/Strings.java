package cms;

public class Strings{
    public static boolean isEmpty( String value  ){
        return value==null || "".equals( value );        
    }
    
    public static void println ( String msg ){
		System.out.println( msg );
	}
    
    /*public static String appendPath(String url, String path){
        if( isEmpty(url) ){
            return 
        }
    }*/


}