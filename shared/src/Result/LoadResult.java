package Result;

/**
 * Stores whether loading the tree was succesful or not (just inherits Result)
 */
public class LoadResult extends Result {
    public LoadResult(boolean success, String message) {
        super(success, message);
    }
}
