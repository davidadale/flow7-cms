package cms;

import play.Play;
import play.Logger;

public class RecorderFactory{
    
    static Recorder instance = null;
    
    public static Recorder get(String host){
        
        Logger.debug("Creating a recorder class.");
        try{
            if( Play.configuration.contains("recorder.class") ){
                Logger.debug("Found a recorder.class to use");
                Class c = Class.forName( Play.configuration.getProperty("recorder.class") );
                return (Recorder) c.newInstance();
            }else{
                Logger.debug("Didn't find a recorder.class file to use. Creating a new BasicRecorder class.");
                return new BasicRecorder( host );
            }
        }catch(Exception e ){
            throw new CMSException("Could not create a recorder class.");
        }     
        
    }
    
}