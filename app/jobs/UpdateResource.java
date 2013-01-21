package jobs;

import play.jobs.*;
import models.*;
import static cms.Strings.*;
import cms.Key;

public class UpdateResource extends Job{
	
	public enum Action{
		Update,
		Delete,
		Create
	}

	Key key;
	byte[] data;
	Action action;
	String etag;

	public UpdateResource(Key key,byte[] data){
		this.key = key;
		this.data = data;
	}

	public void setAction(Action action ){
		this.action = action;
	}

	public void setEtag( String etag ){
		this.etag = etag;
	}

	public void doJob(){
		switch(action){
			case Update:
				update();
			break;
			case Delete:
			break;
			case Create:
				create();
			break;
		}		
	}

	protected void update(){
		Resource resource = Resource.findByKey( key );
		if( resource!=null ){
			resource.data = data;
			resource.save();			
		}
		
	}

	protected void delete(){
		Resource resource = Resource.findByKey( key );
		if( resource!=null ){
			resource.delete();	
		}
		
	}

	protected void create(){
		Resource resource = new Resource( key.host, key.path,data,etag );
		resource.save();		
	}	
}