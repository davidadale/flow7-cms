package cms;

import java.util.List;
import java.util.ArrayList;
import models.*;

import java.io.FileOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.net.HttpURLConnection;
import java.util.Date;

public class Github implements FetchProvider{
    
	public List<Resource> getResources( Site site ){
		
		List<Resource> resources = new ArrayList<Resource>();

        try { //https://github.com/{userName}/{repositoryName}/zipball/{branch}

			String url = "https://github.com/"+site.username+"/"+site.repository+"/zipball/master";
            HttpURLConnection conn = (HttpURLConnection) ( new URL( url ) ).openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            ZipInputStream zis = new ZipInputStream( conn.getInputStream() );

            ZipEntry entry = null;
            while ((entry = zis.getNextEntry()) != null) {
				if( !entry.isDirectory() ){
					String path = entry.getName().substring(entry.getName().indexOf("/") );
					Resource r = new Resource( site.host, path , unzip(zis), Etag.get( site.host , path, entry.getTime() )  );
					r.lastUpdate = new Date( entry.getTime() );
					resources.add(  r );
				}
            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }	
	
		return resources;
	}
	
    protected byte[] unzip( ZipInputStream zin )
            throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] b = new byte[1024];
        int len = 0;
        while ((len = zin.read(b)) != -1) {
            out.write(b, 0, len);
        }
        out.close();
		return out.toByteArray();
    }    
}