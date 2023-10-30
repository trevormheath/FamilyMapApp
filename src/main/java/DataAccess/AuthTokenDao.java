package DataAccess;
import Model.AuthToken;
import java.sql.*;
import Exception.*;

/**
 *Class to act as middle ground between database and AuthToken objects
 */
public class AuthTokenDao {
    private final Connection conn;

    public AuthTokenDao(Connection conn) {this.conn = conn;}
    /**
     * Return an AuthToken object through the ID
     */
    public AuthToken find(String authString) throws DataAccessException {
        AuthToken auth;
        ResultSet rs;
        String sql = "SELECT * FROM AuthToken WHERE AuthToken = ?;";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, authString);
            rs = stmt.executeQuery();
            if(rs.next()) {
                auth = new AuthToken(rs.getString("AuthToken"), rs.getString("Username"));
                return auth;
            } else {
              return null;
            }
        } catch (SQLException ex){
            ex.printStackTrace();
            throw new DataAccessException("Error finding AuthToken");
        }
    }
    /**
     *Add AuthToken to the DB
     */
    public void insert(AuthToken token) throws DataAccessException {
        String sql = "INSERT INTO AuthToken (AuthToken, Username) VALUES(?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, token.getAuthtoken());
            stmt.setString(2, token.getUsername());

            stmt.executeUpdate();
        } catch (SQLException ex){
            ex.printStackTrace();
            throw new DataAccessException("Error encountered when inserting AuthToken to DB");
        }
    }
    /**
     *Find the AuthToken associated with given username
     */
    public AuthToken getToken(String username) throws DataAccessException {
        AuthToken auth;
        ResultSet rs;
        String sql = "SELECT * FROM AuthToken WHERE Username = ?;";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            if(rs.next()) {
                auth = new AuthToken(rs.getString("AuthToken"), rs.getString("Username"));
                return auth;
            } else {
                return null;
            }
        } catch (SQLException ex){
            ex.printStackTrace();
            throw new DataAccessException("Error finding AuthToken from Username");
        }
    }
    /**
     * Removes all the auth data
     */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM AuthToken";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing the event table");
        }
    }
}
