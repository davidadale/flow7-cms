package models;

import play.db.jpa.*;
import javax.persistence.*;

public class SiteUser extends Model{
    
    public static String username;
    public static String password;
    public static boolean enabled;
    
}