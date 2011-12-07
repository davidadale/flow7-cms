package cms;

import play.Play;

public class RecorderFactory{
    
    static Recorder instance = null;
    
    public static Recorder get(String host){
        
        try{
            if( Play.configuration.contains("recorder.class") ){
                Class c = Class.forName( Play.configuration.getProperty("recorder.class") );
                return (Recorder) c.newInstance();
            }else{
                return new BasicRecorder( host );
            }
        }catch(Exception e ){
            throw new CMSException("Could not create a recorder class.");
        }     
        
    }
    
}