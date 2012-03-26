package models;

import java.io.Serializable;
import siena.*;
import cms.*;
import java.util.Date;
import java.util.List;

public class RequestTransaction extends Model implements Serializable{

    @Id
    public Long id;
    public Long resourceId;
    public String host;
    public String url;
    public boolean notFound;
    public Date requested = new Date();
    
    public RequestTransaction( Key key ){
        this.host = key.host;
        this.url = key.toString();
    }
    
    public RequestTransaction setResource( Resource resource ){
        resourceId = resource.id;
        return this;
    }
    
    public RequestTransaction setNotFound(boolean flag ){
        this.notFound = flag;
        return this;
    }
    
   	public static Query<RequestTransaction> all() {
        return Model.all(RequestTransaction.class);
    }    
    
    public static List<RequestTransaction> findByHost(String host){
       return all().filter("host", host).order("-requested").fetch(100);
    }

    
}