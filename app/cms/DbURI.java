package cms;

public class DbURI{
    
    public String host;
    public Integer port;
    public String username;
    public String password;
    public String database;
    
    public DbURI(String uri){
        String[] parts = uri.split("@");
        String[] userAndPass = parts[0].substring(10).split(":");
        String[] hostAndDb = parts[1].split("/");
        String[] hostAndPort = hostAndDb[0].split(":");
        
        for( int i=0;i<parts.length;i++){
            System.out.println("parts " + parts[i]);
        }
        
        for(int i=0;i<userAndPass.length;i++){
            System.out.println("user and pass " + userAndPass[i]);
        }
        
        for( int i=0;i<hostAndDb.length;i++ ){
            System.out.println("host and db " + hostAndDb[i]);
        }
        
        for( int i=0;i<hostAndPort.length;i++){
            System.out.println("host and port " + hostAndPort[i]);
        }
        
        this.host = hostAndPort[0];
        this.port = Integer.parseInt( hostAndPort[1] );
        this.username = userAndPass[0];
        this.password = userAndPass[1];
        this.database = hostAndDb[1];
        
    }
    
    public static void main(String[] args){
        DbURI uri = new DbURI("mongodb://heroku_app2423536:g1gf1usm7it68qehbju2753f55@ds029277.mongolab.com:29277/heroku_app2423536");
        System.out.println( "Host " + uri.host + " port " + uri.port + " username " + uri.username + " password " + uri.password + " database " + uri.database );
    }
}