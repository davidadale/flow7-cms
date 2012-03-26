import org.junit.*;
import java.util.*;
import play.test.*;


import play.modules.siena.SienaFixtures;


import models.Resource;
import models.Site;
import cms.*;

public class ModelTest extends UnitTest {

    @Before
    public void setUp(){
        SienaFixtures.deleteAll();
    }


    @Test
    public void test_save_resource(){
        Resource r = new Resource();
        r.save();
        List<Resource> list = r.all().fetch();
        assertTrue( list.size()== 1 );
    }
    
    @Test
    public void test_save_site(){
        Site site = new Site();
        site.save();
        List<Site> list = Site.all().fetch();
        assertTrue( list.size() == 1 );
    }
    
    @Test
    public void test_start_refresh_site_status(){
        Site site = new Site();
        site.host = "www.flow7.net";
        site.username = "davidadale";
        site.repository = "www.flow7.net";
        site.save();
        assertEquals( site.status, "active");
        site.startRefresh();
        Site s = Site.findBySiteHost("www.flow7.net");
        assertEquals( s.status , "refreshing" );
        
    }
    
    @Test
    public void test_finish_refresh_site_status(){
        Site site = new Site();
        site.host = "www.flow7.net";
        site.username = "davidadale";
        site.repository = "www.flow7.net";
        site.save();
        Site s = Site.findBySiteHost("www.flow7.net");
        s.finishRefresh();
        assertEquals( s.status, "active" );
    }    
    
    @Test
    public void test_resource_find_by_host_and_path(){
        Resource resource = new Resource("www.imagine1.org","/index.html");
        resource.save();
        Resource result = Resource.findByHostAndPath( "www.imagine1.org","/index.html" );
        assertNotNull ( result );
    }
    
    @Test
    public void test_resource_find_by_key(){
        Resource resource = new Resource("www.imagine1.org","/index.html");
        resource.save();
        Resource result = Resource.findByKey( new Key("www.imagine1.org", "index.html") );
        assertNotNull ( result );
    }
    
    @Test
    public void test_naked_resource_find_by_key(){
        Resource resource = new Resource("imagine1.org","/index.html");
        resource.save();
        Resource result = Resource.findByKey( new Key("www.imagine1.org", "index.html") );
        assertNotNull ( result );
    }    
    
    @Test
    public void test_resource_find_by_key_without_www(){
        Resource resource = new Resource("www.imagine1.org","/index.html");
        resource.save();
        Resource result = Resource.findByKey( new Key("imagine1.org", "index.html") );
        assertNotNull ( result );
    }    
    
}