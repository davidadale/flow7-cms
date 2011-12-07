package cms;

import models.Site;
import models.Resource;

import java.util.List;

public interface FetchProvider{
    public List<Resource> getResources(Site site);
}