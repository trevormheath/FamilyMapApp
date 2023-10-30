package Model;


import java.util.Objects;

/**
 *The class store User objects and the info about individuals
 */
public class User {
    /**
     *Store the username made up by a user
     */
    private String username;
    /**
     *The password made up by the user
     */
    private String password;
    /**
     *The users email
     */
    private String email;
    /**
     *The first name of a user
     */
    private String firstName;
    /**
     *The last name of a user
     */
    private String lastName;
    /**
     *The given gender of a user, accepts f or m
     */
    private String gender;
    /**
     *Foreign key that connects user to Person
     */
    private String personID;

    public User(String username, String password, String email, String firstName, String lastName, String gender, String personID) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.personID = personID;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
       User user = (User) o;
        return Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(email, user.email)
                && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName)
                && Objects.equals(gender, user.gender) && Objects.equals(personID, user.personID);
    }
}
