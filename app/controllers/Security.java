package controllers;

import models.*;

public class Security extends Secure.Security{
    
    static boolean authenticate(String username, String password){
        User user = User.findByUsername( username );
        if( user!=null ){
        	return user.password.equals( password );
        }
        return false;
    }
    
}