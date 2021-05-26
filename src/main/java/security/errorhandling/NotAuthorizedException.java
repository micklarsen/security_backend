
package security.errorhandling;


public class NotAuthorizedException extends Exception{
    
    public NotAuthorizedException(String message) {
        super(message);
    }

    public NotAuthorizedException() {
        super("Could not be Authorize");
    }  
    
}
