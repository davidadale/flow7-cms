package models;

import siena.*;
@Table("Account")
public class User extends Model{
	@Id
	public Long id;
	public String username;
	public String password;

	public User(){

	}

	public User(String username,String password){
		this.username = username;
		this.password = password;
	}

   	public static Query<User> all() {
        return Model.all(User.class);
    }	

    public static User findById(Long id) {
        return all().filter("id", id).get();
    }	

    public static User findByUsername(String username){
		return all().filter("username", username).get();    	
    }

    public static int count(){
		return all().count();
	}
}