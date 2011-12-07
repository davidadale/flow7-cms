import org.junit.*;
import java.util.*;
import play.test.*;


import play.modules.siena.SienaFixtures;


import models.Resource;
import models.Site;


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
    
    
}