package controllers;

import play.mvc.*;

public class General extends Controller{

	public static void index(){
		render();
	}

	public static void reset(){
		//session.clear();
		index();
	}
}