import org.junit.*;
import java.util.*;
import play.test.*;
import models.Resource;
import models.Site;
import cms.*;

public class BasicTest extends UnitTest {

    public static String DEFAULT_LOCATION = "http://www.michaelbockoven.com/index.html";
    public static String DEFAULT_SECURE_LOCATION = "https://www.michaelbockoven.com/index.html";
    
    @Test
    public void test_default_cms_key(){
        Key key = new Key( "www.michaelbockoven.com", null );
        assertEquals( key.toString(), DEFAULT_LOCATION  );
    }
    
    @Test
    public void test_host_with_http(){
        Key key = new Key("http://www.michaelbockoven.com",null);
        assertEquals( key.toString(), DEFAULT_LOCATION );
    }
    
    @Test
    public void test_host_with_https(){
        Key key = new Key("https://www.michaelbockoven.com",null);
        assertEquals( key.toString(), DEFAULT_SECURE_LOCATION );        
    }

    @Test
    public void test_with_path_minus_slash(){
        Key key = new Key("www.michaelbockoven.com","about.html");
        assertEquals( key.toString(), "http://www.michaelbockoven.com/about.html");        
    }
    
    @Test
    public void test_with_path(){
        Key key = new Key("www.michaelbockoven.com","/about.html");
        assertEquals( key.toString(), "http://www.michaelbockoven.com/about.html");        
    }
    
    @Test
    public void test_with_longer_path(){
        Key key = new Key("www.michaelbockoven.com","/images/logo.png");
        assertEquals( key.toString(), "http://www.michaelbockoven.com/images/logo.png");        
    }
    
    @Test
    public void test_with_longer_path_minus_slash(){
        Key key = new Key("www.michaelbockoven.com","images/logo.png");
        assertEquals( key.toString(), "http://www.michaelbockoven.com/images/logo.png");        
    }    
    
    @Test
    public void test_equals_method(){
        Key k1 = new Key("http://www.leansoftware.org","home.html");
        Key k2 = new Key("http://www.leansoftware.org","home.html");
        assertTrue( k1.equals( k2 ) );
    }
    
    @Test
    public void test_hash_code(){
        Key k1 = new Key("http://www.leansoftware.org","home.html");
        Key k2 = new Key("http://www.leansoftware.org","home.html");
        System.out.println("K1 " + k1.hashCode() );
        System.out.println("k2 " + k2.hashCode() );
        assertTrue( k1.hashCode() == k2.hashCode() );

    }
    
    @Test
    public void test_content_type_gif(){
        ContentType type = new ContentType( "/images/logo.gif");
        assertEquals( type.getType(), "image/gif");
        assertEquals( type.getExt(), "gif");
    }
    
    @Test
    public void test_resource_equals(){
        Resource resource = new Resource();
        resource.host = "michaelbockoven.com";
        resource.path = "index.html";
        
        Resource rhs = new Resource();
        rhs.host = "michaelbockoven.com";
        rhs.path = "index.html";
        
        assertTrue( resource.equals( rhs ) );
        assertTrue( resource.hashCode() == rhs.hashCode() );
    }
    
    @Test 
    public void test_key_to_string(){
        Key  key = new Key("michaelbockoven.com","index.html");
        assertEquals("http://michaelbockoven.com/index.html", key.toString() );
    }
    
    
    @Test
    public void test_list_contains_resource(){
        Resource resource = new Resource();
        resource.host = "michaelbockoven.com";
        resource.path = "index.html";
        
        Resource rhs = new Resource();
        rhs.host = "michaelbockoven.com";
        rhs.path = "index.html";
        
        List<Resource> files = new ArrayList<Resource>();
        files.add( resource );
        assertTrue( files.contains( rhs ) );        
    }
    
    
    @Test
    public void test_outdated_resource(){
        Resource resource = new Resource();
        resource.lastUpdate = new Date();
        Calendar past = Calendar.getInstance();
        past.add( Calendar.DATE, -30);
        
        Resource old = new Resource();
        old.lastUpdate = past.getTime();
        
        assertFalse( resource.isOld( old ) );
        assertTrue(  old.isOld( resource  ) );
        
    }
    
    @Test
    public void test_find_sub_domain(){
        String host = "dev.imagine1.org";
        String sub = Host.getSubDomain( host );
        assertEquals( "dev", sub);
    }
    
    @Test
    public void test_long_sub_domain(){
        String host = "s1.dev.michaelbockoven.com";
        String sub = Host.getSubDomain( host );
        assertEquals("s1.dev" , sub );
    }
    
    @Test
    public void test_naked_domain(){
        String host = "imagine1.org";
        String sub = Host.getSubDomain( host );
        assertEquals( "", sub );
    }
    
    /*@Test
    public void test_recorder_mock(){
        Recorder rec = RecorderFactory.get();
        rec.startRecording();
        assertTrue( )
    }*/
    
    @Test
    public void test_mongo_db_uri(){
        
            String prop = "mongodb://testUser:testPass@ds029277.mongolab.com:29277/heroku_app123";
            MongoDbURI uri = new MongoDbURI( prop );
            assertEquals("testUser",uri.username);
            assertEquals("testPass", uri.password);
            assertEquals("ds029277.mongolab.com", uri.host);
            assertTrue( 29277 == uri.port );
            assertEquals("heroku_app123", uri.database );
        
    }

    @Test
    public void test_mongo_db_uri_with_defaults(){
        String prop = "mongodb://localhost";
        MongoDbURI uri = new MongoDbURI( prop );
        assertEquals("localhost",uri.host);
        assertEquals("flow7-web", uri.database);
        assertEquals(new Integer(27017), uri.port );
        assertNull( uri.username );
        assertNull( uri.password );

    }
    
    

}
