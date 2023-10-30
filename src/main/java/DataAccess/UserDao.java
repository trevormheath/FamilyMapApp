package DataAccess;
import Model.User;
import Exception.*;
import java.sql.*;

/**
 *Class to act as middle ground between database and user objects
 */
public class UserDao {
    private final Connection conn;

    public UserDao(Connection conn) {
        this.conn = conn;
    }

    /**
     *Add a user to the DB
     */
    public void insert(User user) throws DataAccessException {
        String sql = "INSERT INTO User (Username, Password, Email, FirstName, LastName, Gender, PersonID) VALUES(?, ?, ?, ?, ?, ?, ?)";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getFirstName());
            stmt.setString(5, user.getLastName());
            stmt.setString(6, user.getGender());
            stmt.setString(7, user.getPersonID());

            stmt.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting a user into the database");
        }
    }
    /**
     *Get the user object associated with a given personID
     */
    public User find(String personID) throws DataAccessException {
        User user;
        ResultSet rs;
        String sql = "SELECT * FROM User WHERE PersonID = ?;";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, personID);
            rs = stmt.executeQuery();
            if(rs.next()){
                user = new User(rs.getString("Username"), rs.getString("Password"), rs.getString("Email"),
                        rs.getString("FirstName"), rs.getString("LastName"), rs.getString("Gender"),
                        rs.getString("PersonID"));
                return user;
            } else {
                return null;
            }
        } catch (SQLException e){
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding a user in the database");
        }
    }
    /**
     *Get the user object associated with a given username
     */
    public User findByUsername(String username) throws DataAccessException {
        User user;
        ResultSet rs;
        String sql = "SELECT * FROM User WHERE Username = ?;";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            if(rs.next()){
                user = new User(rs.getString("Username"), rs.getString("Password"), rs.getString("Email"),
                        rs.getString("FirstName"), rs.getString("LastName"), rs.getString("Gender"),
                        rs.getString("PersonID"));
                return user;
            } else {
                return null;
            }
        } catch (SQLException e){
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding a user in the database");
        }
    }
    /**
     * Removes all the user data
     */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM User";
        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing the user table");
        }
    }

    /**
     *Check to see if the username and password are valid in the DB
     */
    public boolean validate(String username, String password) throws DataAccessException {
        ResultSet rs;
        String sql = "SELECT * FROM User WHERE Username = ? AND Password = ?;";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, username);
            stmt.setString(2, password);
            rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e){
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding a user in validating the user");
        }
    }

}
