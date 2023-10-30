package Result;

/**
 * Returns a message if the tree was successfully filled or not (just inherits Result)
 */
public class FillResult extends Result {
    public FillResult(boolean success, String message) {
        super(success, message);
    }
}
