package cms;

import models.Resource;

public interface Recorder{

    public void startRecording();
    public void stopRecording();
    public void recordNew(Resource fresh);
    public void recordUpdate(Resource old, Resource fresh);
    public void recordDelete(Resource resource);
    public void flush();

}