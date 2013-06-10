package controllers;

import play.mvc.*;
import models.User;
import java.util.*;

public class Users extends Controller{

	public static void list(){
		List<User> users = User.findAll();
		render( users );
	}

	public static void create(){
		User user = new User();
		render( user );
	}

	public static void edit(Long id){
		User user = User.findById( id );
		render( user );
	}
}