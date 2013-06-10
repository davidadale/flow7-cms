package models;

import play.db.jpa.*;
import javax.persistence.*;
import java.util.List;
import java.util.regex.*;

import cms.*;

@Entity
public class SecurityRule extends Model{
    
    public String rule;
    public String host;

    public transient Pattern compiledRule = null;

    public SecurityRule(){}

    public SecurityRule(String host, String rule){
    	this.host = host;
    	this.rule = rule;
    }


    public static List<SecurityRule> findAllByHost(String host){
        List<SecurityRule> results = find("byHost",host).fetch();
        if( results.isEmpty() ){
            String subdomain = Host.getSubDomain( host );
            if( subdomain==null || subdomain.length()==0){
                results = find("byHost","www." + host ).fetch();
            }
            if( "www".equals( subdomain) ){
                results = find("byHost",host.substring(4) ).fetch();
            }
        }
        return results;
    }

    protected Pattern getPattern(){
    	if( compiledRule==null ){
    		compiledRule = Pattern.compile( rule );
    	}
    	return compiledRule;
    }

    public boolean matches( Key key ){
    	Matcher m = getPattern().matcher( key.path );
    	return m.matches();
    }

}