package cms;

import java.util.List;
import models.*;

public class SecurityRules{

	List<SecurityRule> rules = null;

	private SecurityRules(List<SecurityRule> rules){
		this.rules = rules;
	}

	public static SecurityRules get(Key key){
		return new SecurityRules( SecurityRule.findAllByHost( key.host ) );
	}

	public boolean secured( Key key ){
		for( SecurityRule sr: rules ){
			if( sr.matches( key ) ){
				return true;
			}
		}
		return false;
	}
}