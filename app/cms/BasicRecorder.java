package cms;

import models.Resource;
import java.io.OutputStream;

import java.text.SimpleDateFormat;
import java.util.Date;
import play.Logger;

public class BasicRecorder implements Recorder{
    
    //BufferedOutputStream bos = new BufferedOutputStream();
    SimpleDateFormat format = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
    
    String host;
    StringBuffer buffer = new StringBuffer();
    int totalUpdates = 0;
    int totalNew = 0;
    int totalDel = 0;
    
    public BasicRecorder(String host){
        this.host = host;
    }
    
    
    public void startRecording(){
        buffer.append( "\n____________________ Site Refresh ___________________________\n" );
        buffer.append("     Started: "+ format.format(new Date()) +"\n"    );
        buffer.append("     Site:    " + host  + "\n\n" );        
    }
    
    public void recordUpdate( Resource old, Resource fresh){
        totalUpdates ++;
        buffer.append("     + U     " + old.path + "\n");
    }
    
    public void recordNew( Resource fresh ){
        totalNew ++;
        buffer.append("     + N     " + fresh.path + "\n");
    }
    
    public void recordDelete( Resource resource ){
        totalDel ++;
        buffer.append("     - D     " + resource.path + "\n");
    }
    
    public void stopRecording(){
        buffer.append("\n    Total Updates: " + totalUpdates + "\n");
        buffer.append("    Total New:     " + totalNew + "\n" );
        buffer.append("    Total Deleted: " + totalDel + "\n");
        buffer.append("    Finished: "+ format.format(new Date())+"\n" );
        buffer.append("______________________ Finished ______________________________\n");
    }
    
    public void flush(){
        Logger.info( this.toString() );
    }
    
    
    @Override
    public String toString(){
        return buffer.toString();
    }
    
    
    
}