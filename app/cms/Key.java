package cms;

import java.io.Serializable;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

import play.mvc.Http;

public class Key implements Serializable{
    
    public String host = null;
    public String path = null;
    public String etag = "";
    
    public Key(String host, String path){
        
        if( isEmpty(host) ){
            throw new CMSException("Cannot create a key with an empty host.");
        }
        
        this.host = host;
        this.path = getPath( path );
    }
    
    public String toString(){
        return getPrefixedHost() + getPath( path );
    }
    
    protected String getPrefixedHost(){
        if( host.startsWith("http://") ){
            return host;
        }
        if( host.startsWith("https://") ){
            return host;
        }
        return "http://" + host;
    }
    
    protected String getPath( String path ){
        if( isEmpty( path ) || isRootPath( path ) || "/_cms/redirect".equals( path ) ){
            return "/index.html";
        }
        if( path!=null && !path.startsWith("/") ){
            return "/" + path;
        }
        return path;
    }
    
    protected boolean isRootPath( String path ){
        return "/".equals( path );
    }
    
    protected boolean isEmpty(String value ){
        return value==null || "".equals( value );
    }
    
    public boolean equals(Object o){
        if (o == null) { return false; }
        if (o == this) { return true; }
        if (o.getClass() != getClass()) {
            return false;
        }
        Key rhs = (Key) o;
        return new EqualsBuilder()                    
                        .append(host, rhs.host)
                        .append(path, rhs.path)
                        .isEquals();        
    }
    
    public int hashCode(){
          return new HashCodeBuilder(17,37)
                .append(host)
                .append(path)
                .toHashCode();
    }
    
    public static Key get(){
        
        String url = Http.Request.current().url;
        Key key = new Key( Host.get(), url );

        Http.Header etag = (Http.Header) 
            Http.Request.current().headers.get("if-none-match");        

        if( etag!=null ){
            key.etag = etag.value();
        }
        
        return key;
    }   
    
    
    
}