package models;

import siena.*;

public class Site extends Model{

    @Id
    public Long id;
    public String host;
    public String username;
    public String repository;
    public String status = "active";
    
    
    public void startRefresh(){
        status = "refreshing";
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
		    if( host.startsWith("www") ){
		        site = all().filter("host", host.substring( 4 )   ).get();
		    }
		}
		return site;
	}    
    
    
    
}