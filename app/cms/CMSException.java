package cms;

public class CMSException extends RuntimeException{
    
    public CMSException(){
        super();
    }
    
    public CMSException(String msg){
        super( msg );
    }
    
    public CMSException(String msg, Throwable e){
        super( msg, e );
    }
    
    public CMSException( Throwable e ){
        super( e );
    }
}