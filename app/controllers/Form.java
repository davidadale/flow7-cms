package controllers;

import play.mvc.*;
import cms.*;

public class Form extends Controller{
    
    public static void post(String collection){
        
        MongoDb db = new MongoDb();
        CMSForm form = CMSForm.createFromMap( params.all() );
        db.save( collection, form.fields );
        
        String path = params.get("redirect");
        if( path==null || path.length()==0){
            path = "/";
        }
        redirect( path );
    }
    
}