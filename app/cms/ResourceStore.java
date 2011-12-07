package cms;

import models.Resource;

public interface ResourceStore{
    public Resource get( Key key );
    public void put( Key key, Resource resource);
}