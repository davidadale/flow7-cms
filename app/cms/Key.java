package cms;

import java.io.Serializable;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

import play.mvc.Http;
import play.mvc.Scope;


public class Key implements Serializable{
    
    public String host = null;
    public String path = null;
    public String etag = "";
    
    public Key(String host, String path){
        
        if( isEmpty(host) ){
            throw new CMSException("Can not create a key with an empty host.");
        }
        
        //this.host = host.replace("/","");
        this.host = host;
        this.path = getPath( path );
    }
    
    public String toString(){
        return getPrefixedHost() + path;
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
        if( isEmpty( path ) || isRootPath( path ) ){
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
        String site = Scope.Session.current().get("_host");
        String url = Http.Request.current().url;
        Key key = null;
        
        if( site!=null ){
            key = new Key( site, url);
        }else{
            key = new Key( Http.Request.current().domain, url );
        }

        Http.Header etag = (Http.Header) 
            Http.Request.current().headers.get("if-none-match");        

        if( etag!=null ){
            key.etag = etag.value();
        }
        
        return key;
    }    
    
    
    
}