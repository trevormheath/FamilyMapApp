package Request;

/**
 *The information provided at attempted login
 */
public class LoginRequest {
    /**
     *The username provided
     */
    private String username;
    /**
     *The password provided
     */
    private String password;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
