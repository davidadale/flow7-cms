package models;

import play.db.jpa.*;
import javax.persistence.*;
import java.util.Date;
import cms.Host;

@Entity
public class Site extends Model{

    public String host;
    public Boolean localDev = false; 
    public String username;
    public String repository;
    public String status = "active";
    public Date lastRefresh;
    
    public String registryProvider;
    public String providerUsername;
    public String providerPassword;
    
    public String emailHost;
    public String emailUsername;
    public String emailPassword;
    
    public Site(){
        super();
    }

    public Site(Boolean local){
        this.localDev = local;
    }

    public void startRefresh(){
        status = "refreshing";
        lastRefresh = new Date();
        this.save();
    }
    
    public String getCollPrefix(){
        if( localDev ==  true ){
            return host.substring( host.lastIndexOf("/")+1 );
        }else{
            return host;
        }
    }

    public boolean isBusy(){
        return "refreshing".equals( status );
    }
    
    public void finishRefresh(){
        status = "active";
        this.save();
    }
	
	public static Site findBySiteHost(String host){
		Site site = find("byHost",host).first();
		if( site == null ){
		    String sub = Host.getSubDomain( host );
		    if( sub==null || sub.length() ==0 ){
		        site = find("byHost", "www." + host ).first();
		    }else if( "www".equals( sub ) ){
		        site = find("byHost", host.substring( 4 )   ).first();
		    }
		}
		return site;
	}    
    
    
    
}