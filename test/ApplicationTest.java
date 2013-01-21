import org.junit.*;
import play.test.*;
import play.mvc.*;
import play.mvc.Http.*;
import models.*;
import play.vfs.VirtualFile;
import play.Play;
import org.junit.Before;
import cms.*;
import java.util.*;
import java.io.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import jobs.*;

public class ApplicationTest extends FunctionalTest {

    /**
    * Clear the database and install data from the data yaml file
    * effectively resetting the database.
    */
    @Before
    public void setUp(){
        Fixtures.deleteAllModels();
        Fixtures.loadModels( "data.yml" );
    }
    
    @Test 
    public void test_www_domain() throws Exception{

        Site site = Site.findBySiteHost("www.michaelbockoven.com");
        RefreshSiteJob job = new RefreshSiteJob( site.id );
        job.doJob();
        
        List<Resource> files = Resource.findAllByHost("www.michaelbockoven.com");
        assertNotNull( files );
        assertTrue( files.size() == 3 );
        assertTrue( files.contains( new Resource("www.michaelbockoven.com","/index.html") ) );
        assertTrue( files.contains( new Resource("www.michaelbockoven.com","/about.html") ) );
        assertTrue( files.contains( new Resource("www.michaelbockoven.com","/newfile.html") ) );                
        
    }
    
    
    @Test 
    public void test_naked_domain() throws Exception{
        
        Site site = Site.findBySiteHost("michaelbockoven.com");
        RefreshSiteJob job = new RefreshSiteJob( site.id );
        job.doJob(); 
        
        List<Resource> files = Resource.findAllByHost("michaelbockoven.com");
        assertNotNull( files );
        assertTrue( files.size() == 3 );
        assertTrue( files.contains( new Resource("www.michaelbockoven.com","/index.html") ) );
        assertTrue( files.contains( new Resource("www.michaelbockoven.com","/about.html") ) );
        assertTrue( files.contains( new Resource("www.michaelbockoven.com","/newfile.html") ) );                
        
    }
    
    @Test 
    public void test_dev_sub_domain() throws Exception{
        
        List<Resource> files = Resource.findAllByHost("dev.michaelbockoven.com");
        assertNotNull( files );
        assertTrue( files.size() == 2 );
        assertTrue( files.contains( new Resource("dev.michaelbockoven.com","/index.html") ) );
        assertTrue( files.contains( new Resource("dev.michaelbockoven.com","/about.html") ) );

        
    }   
    
    @Test     
    public void test_get_dev_resource_using_key(){
        Key key = new Key("dev.michaelbockoven.com","/index.html");
        Resource r = Resource.findByKey(key);
        assertNotNull( r );
        assertTrue( new Resource("dev.michaelbockoven.com","/index.html").equals( r ) );
    }
    
    @Test     
    public void test_get_www_resource_using_key(){
        Key key = new Key("www.michaelbockoven.com","/index.html");
        Resource r = Resource.findByKey(key);
        assertNotNull( r );
        assertTrue( new Resource("www.michaelbockoven.com","/index.html").equals( r ) );
    }    
    
    @Test     
    public void test_get_naked_resource_using_key(){
        Key key = new Key("michaelbockoven.com","/index.html");
        Resource r = Resource.findByKey(key);
        assertNotNull( r );
        assertTrue( new Resource("www.michaelbockoven.com","/index.html").equals( r ) );
    }    
    
    @Test
    public void test_html_store_and_retrieve(){
        
         VirtualFile index = null;
        
        for(VirtualFile vf: Play.javaPath){
            index = vf.child("index_test.html");
            if(index!=null && index.exists() ){
                break;
            }
        }
        
        if( index == null ){
            fail("Can not find the sample index.html file.");
        }
        
        Resource resource = new Resource("www.michaelbockoven.com","index2.html");
        try{ resource.data = FileUtils.readFileToByteArray( index.getRealFile() );
        }catch(Exception e ){}
        resource.lastUpdate = new Date();
        resource.save();
        
        Resource r = Resource.findByHostAndPath("www.michaelbockoven.com","index2.html");
        assertNotNull( r );

        try{
            assertTrue( IOUtils.contentEquals( new FileInputStream( index.getRealFile() ),
                                               new ByteArrayInputStream( r.data ) )  );
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    
}