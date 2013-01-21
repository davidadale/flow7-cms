package cms;

import play.mvc.Scope;
import play.mvc.Http.Request;
import models.*;
import cms.Host;

public class UserSession{

	public static AuthToken newSession(Long userId){
		AuthToken auth = new AuthToken(userId);
		auth.save();
		Scope.Session.current().put("auth",auth.token);
		return auth;
	}

	public static AuthToken connect(){
		String token = Scope.Session.current().get("auth");
		if( token == null ){
			token = Request.current().headers.get("auth").value();
		}
		return  AuthToken.findByToken( token );
	}

	public static boolean isConnected(){
		String auth = Scope.Session.current().get("auth");
		if( auth == null){return false;}
		return isValid( auth );
	}

	protected static boolean isValid( String auth ){
		AuthToken token = AuthToken.findByHostAndToken( Host.get(), auth );
		return token != null;
	}

}