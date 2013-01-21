package cms;

import static cms.Strings.*;

public class MongoDbURI{
    
    public String host;
    public Integer port = 27017;
    public String database = "flow7-web";
    public String username;
    public String password;
    
    
    public MongoDbURI(String uri){

        String[] parts = uri.split("@");
        String hostPortDb = uri;

        if( parts.length == 2 ){
            // the URI has a username password
            setUserPass( parts[0] );
            hostPortDb = parts[1];
        }

        setHostPortDb( hostPortDb );
        
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Host: " + host + "\n");
        sb.append("Port: " + port + "\n");
        sb.append("username: " + username + "\n");
        sb.append("password: " + password + "\n");
        sb.append("database: " + database + "\n");
        return sb.toString();
    }

    private void setUserPass( String value ){
        // string schema mongodb:// off
        String userpass = value.substring( 10 );
        if( userpass.indexOf(":") > -1 ){
            username = userpass.split(":")[0];
            password = userpass.split(":")[1];
        }

    }

    private void setHostPortDb( String value ){
        if( value.startsWith("mongodb://") ){
            value = value.substring( "mongodb://".length() );
        }

        if( value.indexOf("/") > -1 ){
            String[] hostAndDb = value.split("/");
            setHostPort( hostAndDb[0] );
            database = hostAndDb[1];
        }else{
            setHostPort( value );
        }
    } 

    private void setHostPort( String value ){
        try{
            
            if( value.indexOf(":") > -1 ){
                host = value.split(":")[0];
                port = Integer.parseInt( value.split(":")[1] );
            }else{
                host = value;
            }

        }catch(Exception e){

        }
    }
    public boolean hasDb(){
        return database!=null;
    }


    public boolean hasUserPass(){
        return username!=null && password!=null;
    }


}