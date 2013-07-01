package models;

import java.io.Serializable;
import play.db.jpa.*;
import javax.persistence.*;
import cms.*;
import java.util.Date;
import java.util.List;

@Entity
public class RequestTransaction extends Model implements Serializable{

    public Long resourceId;
    public String host;
    public String url;
    public boolean notFound;
    public Date requested = new Date();
    public Long timesServed = 0l;


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
    
    public void incrementCount(){
        this.timesServed++;
    }

    public static List<RequestTransaction> findByHost(String host){
        return find("host=? order by requested desc", host).fetch();
       //return find("byHost", host).order("-requested").fetch(100);
    }

    public static RequestTransaction findByKey( Key key ){
        RequestTransaction rt = find("host=? and url=?", key.host, key.toString() ).first();
        if( rt == null ){
            rt = new RequestTransaction( key );
        }
        return rt;
    }

    
}