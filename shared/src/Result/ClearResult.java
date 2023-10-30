package Result;

/**
 * Gives message if clear was successful or not (just inherits Result)
 */
public class ClearResult extends Result {

    public ClearResult(boolean b, String message){
        super(b, message);
    }
}
