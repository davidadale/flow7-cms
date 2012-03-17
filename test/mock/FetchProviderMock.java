package mock;

import models.*;
import cms.*;
import java.util.*;

public class FetchProviderMock implements FetchProvider{
    
    public List<Resource> getResources( Site site ){
        List<Resource> result =  new ArrayList<Resource>();
        
        Resource index = new Resource();
        index.host = "www.michaelbockoven.com";
        index.path = "/index.html";
        index.lastUpdate = new Date();

        Resource about = new Resource();
        about.host = "www.michaelbockoven.com";
        about.path = "/about.html";
        about.lastUpdate = new Date();
        
        Resource newFile = new Resource("www.michaelbockoven.com","/newfile.html");
        newFile.lastUpdate = new Date();
        
        result.add( index );
        result.add( about );
        result.add( newFile );
        return result;
    }
    
}