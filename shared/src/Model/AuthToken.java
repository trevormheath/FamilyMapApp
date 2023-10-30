package Model;

import java.util.Objects;

/**
 *Class to store Authentication Token Object
 */
public class AuthToken {
    /**
     *The Authentication token
     */
    private String authtoken;
    /**
     *The Foreign Key from the User class
     */
    private String username;

    public AuthToken(String authtoken, String username) {
        this.authtoken = authtoken;
        this.username = username;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthToken auth = (AuthToken) o;
        return Objects.equals(authtoken, auth.getAuthtoken()) && Objects.equals(username, auth.getUsername());
    }
}
