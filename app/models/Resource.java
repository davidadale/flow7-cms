package models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import siena.*;
import cms.*;


public class Resource extends Model implements Serializable{

    @Id
    public Long id;

    public String type;

    public String path;
    
    public String host;

    @Column("file_data")
    public byte[] data;
    
    public String etag;
    
    public Date lastUpdate;

    //@Transient
    public boolean stale = false;

    public Resource(){}
    
    public Resource(String host, String path){
        this.host = host;
        this.path = path;
        this.type = new ContentType( path ).toString();
    }

    public Resource(String host, String path, byte[] data, String etag ){
    	this.host = host;
    	this.path = path;
    	this.type = new ContentType( path ).toString();
    	this.data = data;
    	this.etag = etag;
    }

    public Key getKey(){
    	return new Key( host, path );
    }    
    
    public boolean isBinary(){
        return ContentType.isBinary( type );
    }
    
    public boolean isOld( Resource resource ){
        if( this.lastUpdate==null || resource.lastUpdate == null ){ return false; }
        return lastUpdate.before( resource.lastUpdate );
    }
    
    public void refresh(Resource resource){
        this.data = resource.data;
        this.etag = resource.etag;
        this.lastUpdate = resource.lastUpdate;
    }
    
    
    public boolean equals(Object o){
        if( o == null){ return false; }
        if( !(o instanceof Resource) ){return false;}
        Resource rhs = (Resource)o;
        return new EqualsBuilder()
            .append( host, rhs.host )
            .append( path, rhs.path )
            .isEquals();
        
    }
    
    public int hashCode(){
        return new HashCodeBuilder(17,43)
            .append( host )
            .append( path )
            .toHashCode();
    }
   	
   	public static Query<Resource> all() {
        return Model.all(Resource.class);
    }    
    
	/*public static Site findBySiteName(String name){
		Site site = all().filter("name",name).get();
		if( site == null ){
		    if( name.startsWith("www") ){
		        site = all().filter("name", name.substring( 4 )   ).get();
		    }
		}
		return site;
	}*/    
    
    public static List<Resource> findAllByHost(String host){
        return all().filter("host",host).fetch();
    }
    
    public static Resource findByHostAndPath(String host, String path){
        return all().filter("host",host).filter("path",path).get();
    }
    
    public static void deleteAllByHost( String host ){
        all().filter("host",host).delete();
    }
    
    public static Resource findByKey( Key key ){
        Resource r = findByHostAndPath( key.host, key.path );
        // handle necked domains
        if( r==null &&  
            key.host!=null && 
            !key.host.startsWith("www.") ){
            r = findByHostAndPath( "www." + key.host, key.path );
        }
        
        if( r==null &&
            key.host!=null &&
            key.host.startsWith("www.") ){
                
            r = findByHostAndPath( key.host.substring(4), key.path );    
        }
        
        return r;
    }

    
}