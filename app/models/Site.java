package models;

import siena.*;
import java.util.Date;
import cms.Host;

public class Site extends Model{

    @Id
    public Long id;
    public String host;
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
    
    public void startRefresh(){
        status = "refreshing";
        lastRefresh = new Date();
        this.save();
    }
    
    public boolean isBusy(){
        return "refreshing".equals( status );
    }
    
    public void finishRefresh(){
        status = "active";
        this.save();
    }
    
   	public static Query<Site> all() {
        return Model.all(Site.class);
    }

	public static int count(){
		return all().count();
	}

    public static Site findById(Long id) {
        return all().filter("id", id).get();
    }	
	public static Site findBySiteHost(String host){
		Site site = all().filter("host",host).get();
		if( site == null ){
		    String sub = Host.getSubDomain( host );
		    if( sub==null || sub.length() ==0 ){
		        site = all().filter("host", "www." + host ).get();
		    }else if( "www".equals( sub ) ){
		        site = all().filter("host", host.substring( 4 )   ).get();
		    }
		}
		return site;
	}    
    
    
    
}