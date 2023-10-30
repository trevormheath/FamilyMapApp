package Result;

/**
 * All Results need these variables and/or methods
 */
public class Result {
    /**
     *Did it get a successful response or not
     */
    private boolean success;
    /**
     *Used in Error Response to describe the problem
     */
    private String message;

    public Result(){
        this.success = false;
        this.message = null;
    }
    public Result(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
