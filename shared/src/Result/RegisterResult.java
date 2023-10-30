package Result;

/**
 *Either a successful or error response to a RegisterRequest
 */
public class RegisterResult extends Result{
    /**
     *Given users authtoken
     */
    private String authtoken;
    /**
     *Given users username
     */
    private String username;
    /**
     *The ID of a given user
     */
    private String personID;

    public RegisterResult(String authtoken, String username, String personID) {
        super(true, null);
        this.authtoken = authtoken;
        this.username = username;
        this.personID = personID;
    }
    public RegisterResult(boolean success, String message){
        super(success, message);
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }
}
