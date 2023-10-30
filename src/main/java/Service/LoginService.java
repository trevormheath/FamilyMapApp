package Service;
import DataAccess.AuthTokenDao;
import Exception.DataAccessException;
import DataAccess.Database;
import DataAccess.UserDao;
import Model.AuthToken;
import Model.User;
import Request.LoginRequest;
import Result.LoginResult;

import java.util.UUID;

/**
 * Each time someone attempts to log into the server this runs
 */
public class LoginService {

    public LoginService() {
    }

    /**
     *This functions logs the user in and retrieves an authtoken
     * Can also return an error
     * @param r, a LoadRequest object
     * @return LoginResult (successful or error response)
     */
    public LoginResult doLogin(LoginRequest r){
        Database db = new Database();
        try {
            db.openConnection();

            //generate random ID's for the authToken class
            UUID genID = UUID.randomUUID();
            String authTokenUUID = genID.toString();

            //create a new User object and insert it into the database
            if(new UserDao(db.getConnection()).validate(r.getUsername(), r.getPassword())) {
                User user = new UserDao(db.getConnection()).findByUsername(r.getUsername());
                //create an authToken for the new user and insert into database
                AuthToken token = new AuthToken(authTokenUUID, r.getUsername());
                new AuthTokenDao(db.getConnection()).insert(token);

                db.closeConnection(true);
                return new LoginResult(token.getAuthtoken(), user.getUsername(), user.getPersonID());
            }
            else {
                throw new DataAccessException("Password and Username combination is Incorrect");
            }
        } catch (DataAccessException ex){
            ex.printStackTrace();
            db.closeConnection(false);
            return new LoginResult(false, "Error in LoginService");
        } catch (Exception ex) {
            ex.printStackTrace();
            db.closeConnection(false);
            return new LoginResult(false, "Error: Unknown Error Thrown");
        }
    }
}
