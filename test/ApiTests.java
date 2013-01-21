
import play.test.*;
import play.mvc.Http.Response;
import play.mvc.Http.Cookie;
import play.mvc.Http.Request;
import play.mvc.Http.Header;
import play.mvc.*;
import play.mvc.Scope.Session;
import org.junit.Before;
import java.util.*;
import org.junit.*;
import com.google.gson.*;
import cms.MongoDb;

public class ApiTests extends FunctionalTest{

	@Before
	public void before(){				
		Fixtures.deleteAllModels();
		Fixtures.loadModels("data.yml");
		MongoDb.get("localhost").with("foo").drop();
		MongoDb.get("localhost").load("foo","foo.data");
	}

	@Test
	public void test_authenticate(){

		Map<String,String> params = new HashMap<String,String>();
		params.put("username","tester@flow7.net");
		params.put("password","password");

		Response response = POST("/_api/authenticate",params);
		Map<String,Object> data = getData( response );

		assertIsOk( response );		
		assertNotNull( data );
		assertEquals( Boolean.TRUE, data.get("success")  );
		assertNotNull( data.get("auth") );
	}

	@Test
	public void test_retrieve_objects(){
		// login
		Map<String,String> params = new HashMap<String,String>();
		params.put("username","admin@flow7.net");
		params.put("password","changeme");
		Response response = POST("/_api/authenticate",params);
		Map<String,Object> data  = getData( response );

		Header auth = new Header( "auth",(String) data.get("auth") );

		Request req = newRequest();
		req.method = "GET";
		req.path = "/_api/collection/foo";
		//req.querystring = "query=zoo";
		req.headers.put("auth", auth );	
		response = makeRequest( req );
		data = getData( response );
		assertEquals( 10, ((Collection)data.get("items")).size() );	
	}

	@Test
	public void test_save_object(){
		// login
		Map<String,String> params = new HashMap<String,String>();
		params.put("username","admin@flow7.net");
		params.put("password","changeme");
		Response response = POST("/_api/authenticate",params);
		Map<String,Object> data  = getData( response );


		Map<String,Object> args = new HashMap<String,Object>();
		args.put("obj","{'foo':true,'loo':'lang'}");		

		Header auth = new Header( "auth",(String) data.get("auth") );
		Request req = newRequest();
		req.method = "POST";
		req.path = "/_api/collection/foo";
		req.headers.put("auth", auth );
		req.params.put("obj","{'foo':true,'loo':'lang'}");

		// add object to collection foo
		response = makeRequest( req );
		data = getData( response );

		assertEquals( Boolean.TRUE, data.get("success") );

	}

	protected Map<String,Object> getData( Response resp ){
		String content = getContent( resp );
    	Gson gson = new Gson();
    	return gson.fromJson( content, Map.class );
	}

	
}