package cms;

public class MongoDbURI{
    
    public String host;
    public Integer port;
    public String username;
    public String password;
    public String database;
    
    public MongoDbURI(String uri){
        String[] parts = uri.split("@");
        String[] userAndPass = parts[0].substring(10).split(":");
        String[] hostAndDb = parts[1].split("/");
        String[] hostAndPort = hostAndDb[0].split(":");
        
        this.host = hostAndPort[0];
        this.port = Integer.parseInt( hostAndPort[1] );
        this.username = userAndPass[0];
        this.password = userAndPass[1];
        this.database = hostAndDb[1];
        
    }

}