package models;

import play.db.jpa.*;
import javax.persistence.*;
import java.util.*;

@Table(name="Account")
@Entity
public class User extends Model{

	public enum Role{ADMIN,SITE_USER}

	public String username;
	public String password;
	public String host;
	public Role role;

	public User(){

	}

	public User(String username,String password){		
		this.username = username;
		this.password = password;
		this.role = Role.ADMIN;
	}
	public User(String host, String username,String password){
		this.host = host;
		this.username = username;
		this.password = password;
		this.role = Role.SITE_USER;
	}

    public static User findByUsername(String username){
		return find("byUsername", username).first();    	
    }

    public static User findByHostAndUsername(String host, String username){
    	return find("byHostAndUsername",host,username).first();
    	//return a.filter("host",host).filter("username", username).get();
    }


}