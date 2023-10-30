package Exception;

public class AuthTokenException  extends Exception{
    public AuthTokenException() {
        super("Invalid Auth Token");
    }
}
