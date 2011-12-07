package cms;

import java.io.Serializable;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

public class Key implements Serializable{
    
    String host = null;
    String path = null;
    
    public Key(String host, String path){
        
        if( isEmpty(host) ){
            throw new CMSException("Can not create a key with an empty host.");
        }
        
        //this.host = host.replace("/","");
        this.host = host;
        this.path = isEmpty(path) ?"/":path;
    }
    
    public String toString(){
        return getPrefixedHost() + getPath();
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
    
    protected String getPath(){
        if( isRootPath( path ) ){
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
    
}