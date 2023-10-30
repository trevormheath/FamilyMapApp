package Result;

/**
 *Information resulting from login
 */
public class LoginResult extends Result {
    /**
     *Returned authtoken
     */
    private String authtoken;
    /**
     *The username provided
     */
    private String username;
    /**
     *The personID associated with the username
     */
    private String personID;

    public LoginResult(String authtoken, String username, String personID) {
        super(true, null);
        this.authtoken = authtoken;
        this.username = username;
        this.personID = personID;
    }
    public LoginResult(boolean success, String message){
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
