package models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import play.db.jpa.*;
import javax.persistence.*;
import cms.*;

@Entity
public class Resource extends Model implements Serializable{

    public String type;

    public String path;
    
    public String host;

    @Column(name="file_data",length=2097152)
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
    
    public boolean isHtml(){
        return "text/html".equals( type );
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
    
    public static List<Resource> findAllByHost(String host){
        List<Resource> results = find("byHost",host).fetch();
        if( results.isEmpty() ){
            String subdomain = Host.getSubDomain( host );
            if( subdomain==null || subdomain.length()==0){
                results = find("byHost","www." + host ).fetch();
            }
            if( "www".equals( subdomain) ){
                results = find("byHost",host.substring(4) ).fetch();
            }
        }
        return results;
    }
    
    public static void deleteAllByHost( String host ){
        delete("host=?",host);
    }

    public static Resource findByHostAndPath(String host, String path){
        return find("byHostAndPath",host,path).first();
    }
    
    public static Resource findByKey( Key key ){

        Resource r = findByHostAndPath( key.host, key.path );
        
        
        if( r==null ){
            String subdomain = Host.getSubDomain( key.host );
            // if domain is necked then try to pull resources for www
            if( subdomain==null || subdomain.length()==0){
                r = findByHostAndPath( "www." + key.host, key.path );
            }
            if( "www".equals( subdomain ) ) {
                r = findByHostAndPath(  key.host.substring( 4 ) , key.path );
            }            
        }

        return r;
    }
    

    
}