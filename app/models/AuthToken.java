package models;

import java.security.SecureRandom;
import java.util.Date;
import play.db.jpa.*;
import javax.persistence.*;
import cms.Host;


@Entity
public class AuthToken extends Model{
    
    protected static SecureRandom random = new SecureRandom();

	public Long userId;
	public String host;
	public String token;
	public Date created;

	public AuthToken(){
		token = generateToken();
		created = new Date();
		host = "localhost";
	}

	public AuthToken(Long userId){
		this.token = generateToken();
		this.created = new Date();
		this.userId = userId;
		this.host = Host.get();
	}

    private synchronized String generateToken() {
        long longToken = Math.abs( random.nextLong() );
        return Long.toString( longToken, 16 );            
    }


	public boolean valid(){
		return true;
	}
        

    public static AuthToken findByToken(String token){
    	return find("byToken",token).first();
    }

    public static AuthToken findByHostAndToken(String host,String token){
    	return find("byHostAndToken",host,token).first();
    }

}