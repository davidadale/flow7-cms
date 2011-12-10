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

public class ApplicationTest extends FunctionalTest {

    /**
    * Clear the database and install data from the data yaml file
    * effectively resetting the database.
    */
    @Before
    public void setUp(){
        play.modules.siena.SienaFixtures.deleteAll();
        play.modules.siena.SienaFixtures.load( "data.yml" );
    }
    
    /**
    * This test is set to pull in 3 resources. One should be removed
    * one will be updated and one will stay the same.
    */
    @Test 
    public void test_background_job_refresh(){
        // look up a site
        Site site = Site.findBySiteHost("www.michaelbockoven.com");
        
        // use the site from above to start the background job to refresh
        Response response = GET("/process/refresh/" + site.id);        
        assertStatus(200, response);
        
        
        List<Resource> files = Resource.findAllByHost("www.michaelbockoven.com");
        assertNotNull( files );
        assertTrue( files.size() == 3 );
        assertTrue( files.contains( new Resource("www.michaelbockoven.com","index.html") ) );
        assertTrue( files.contains( new Resource("www.michaelbockoven.com","about.html") ) );
        assertTrue( files.contains( new Resource("www.michaelbockoven.com","newfile.html") ) );                
        

        
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