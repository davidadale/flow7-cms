package tags;

import play.templates.FastTags;
import groovy.lang.Closure;
import java.io.PrintWriter;
import java.util.Map;
import play.templates.GroovyTemplate.ExecutableTemplate;
import cms.*;
import models.*;
import play.templates.*;
import play.exceptions.*;

@FastTags.Namespace("cms") 
public class CmsTags extends FastTags{
    
    public static void _msg(Map<?, ?> args, Closure body, PrintWriter out, 
       ExecutableTemplate template, int fromLine){
           Object value = args.get("key");
           out.write( "bang" );//cms.Message.get( (String)value ) );
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
                   System.out.println(">>>>>>>>>>>>>>>>>>>>>> Using template with resource.path " + resource.path);
                   BaseTemplate.layout.set( (BaseTemplate) TemplateLoader.load(resource.path, new String( resource.data,"utf-8" ) ) );                   
               }
               /*if (name.startsWith("./")) {
                   String ct = BaseTemplate.currentTemplate.get().name;
                   if (ct.matches("^/lib/[^/]+/app/views/.*")) {
                       ct = ct.substring(ct.indexOf("/", 5));
                   }
                   ct = ct.substring(0, ct.lastIndexOf("/"));
                   name = ct + name.substring(1);
               }*/
               //TemplateLoader.load(resource.path, new String( resource.data,"utf-8" ) );
              // BaseTemplate.layout.set( (BaseTemplate) TemplateLoader.load(name) );
           } catch (TemplateNotFoundException e) {
               throw new TemplateNotFoundException(e.getPath(), template.template, fromLine);
           } catch( Exception e ){
               
           }
    }
    
}