package controllers;

import play.mvc.*;
import models.User;
import java.util.*;

public class Users extends Controller{

	public static void list(){
		List<User> users = User.findAll();
		render( users );
	}
}