package tags;

import play.templates.FastTags;
import play.templates.TagContext;
import groovy.lang.Closure;
import java.io.PrintWriter;
import java.util.Map;
import play.templates.GroovyTemplate.ExecutableTemplate;
import cms.*;
import models.*;
import play.templates.*;
import play.exceptions.*;
import groovy.lang.GroovyShell;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import java.util.*;
import play.mvc.Http.Request;

import java.util.Random;


@FastTags.Namespace("cms") 
public class CmsTags extends FastTags{ 
    
    static GroovyShell shell = new GroovyShell();
    
    public static void _msg(Map<?, ?> args, Closure body, PrintWriter out, 
       ExecutableTemplate template, int fromLine){
           Object value = args.get("key");
           out.write( "bang" );//cms.Message.get( (String)value ) );
    }    
    
    public static void _form(Map<?, ?> args, Closure body, PrintWriter out, 
       ExecutableTemplate template, int fromLine){
           
    }
    
    public static void _active(Map<?, ?> args, Closure body, PrintWriter out, 
       ExecutableTemplate template, int fromLine){
        String name = (String) args.get("arg");
        String active = (String) BaseTemplate.layoutData.get().get("active");
        if( name.equals( active ) ){
            out.print( "active" );
        }
    }
    /**
    * This is a utility method tag that allows the body content to be repeated a certain
    * number of times with a random integer as one of the parameters.
    *
    */
    public static void _randomList(Map<?, ?> args, Closure body, PrintWriter out, 
       ExecutableTemplate template, int fromLine){
           
           Integer times = (Integer)args.get("times");
           Integer max = (Integer)args.get("max");
           Random random = new Random();
           
           for(int i=0;i<times;i++){
               body.setProperty("index", i );
               body.setProperty("random", random.nextInt( max ) );
               body.call();
           }
    }
    
    public static void _collection(Map<?, ?> args, Closure body, PrintWriter out, 
       ExecutableTemplate template, int fromLine){
        
        String name = (String) args.get("name");
        String queryString = (String) args.get("query");
        Integer limit = (Integer)args.get("limit");
        Integer skip = (Integer)args.get("skip");
        
        String as = (String) args.get("as");
        if( as==null || as.length()==0 ){ as = "item"; }

        MongoDb db = new MongoDb();
        List<Map> result = null;
        result = db.collection( name ).find( queryString ).limit( limit ).skip(skip).fetch();

        for( Map row: result ){
            body.setProperty(as,row);
            body.call();            
        }            
    }
    
    public static void _extends(Map<?, ?> args, Closure body, PrintWriter out, 
       ExecutableTemplate template, int fromLine){
           try {
               if (!args.containsKey("arg") || args.get("arg") == null) {
                   //throw new TemplateExecutionException(template.template, fromLine, "Specify a template name", new TagInternalException("Specify a template name"));
               }
               String path = args.get("arg").toString();
               
               Key key = new Key( Host.get(), path   );
               Resource resource = ResourceCache.get( key );

               if( resource==null ){
                   resource = Resource.findByKey( key );
               }
               
               if(resource!=null ){
                   BaseTemplate.layout.set( (BaseTemplate) TemplateLoader.load(resource.path, new String( resource.data,"utf-8" ),true ) );                   
               }

           } catch (TemplateNotFoundException e) {
               throw new TemplateNotFoundException(e.getPath(), template.template, fromLine);
           } catch( Exception e ){
               
           }
    }
    
}