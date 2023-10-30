package Service;
import DataAccess.*;
import Model.AuthToken;
import Model.User;
import Request.RegisterRequest;
import Result.RegisterResult;
import Exception.*;
import java.util.UUID;

/**
 * When someone registers an account this processes their info
 */
public class RegisterService {

    public RegisterService() {
    }

    /**
     * Creates a new user, generates 4 generations of ancestor data, logs in the user, retrieves an authToken
     * Puts all the info into the return type
     * Can also return an error
     * @param r, a RegisterRequest object
     * @return RegisterResult (successful or error)
     */
    public RegisterResult doRegister(RegisterRequest r){
        Database db = new Database();
        try {
            db.openConnection();

            //try and find authToken by username to check if already in the db
            if(new AuthTokenDao(db.getConnection()).getToken(r.getUsername()) != null) {
                throw new DataAccessException("Username already Taken");
            }

            //generate random ID's for the personID and authToken classes
            UUID genID = UUID.randomUUID();
            String personUUID = genID.toString();
            genID = UUID.randomUUID();
            String authTokenUUID = genID.toString();

            //create a new User object and insert it into the database
            User newReg = new User(r.getUsername(), r.getPassword(), r.getEmail(), r.getFirstName(), r.getLastName(), r.getGender(), personUUID);
            new UserDao(db.getConnection()).insert(newReg);

           //create an authToken for the new user and insert into database
            AuthToken token = new AuthToken(authTokenUUID, newReg.getUsername());
            new AuthTokenDao(db.getConnection()).insert(token);

            //If everything else works correctly then fill the new users tree
            FillService fServ = new FillService();
            fServ.doFill(newReg.getUsername(), 4, db.getConnection());

            db.closeConnection(true);
            return new RegisterResult(token.getAuthtoken(), newReg.getUsername(), personUUID);

        } catch (DataAccessException ex){
            ex.printStackTrace();
            db.closeConnection(false);
            return new RegisterResult(false, "Error in RegisterService");
        } catch (Exception ex) {
            ex.printStackTrace();
            db.closeConnection(false);
            return new RegisterResult(false, "Error: Unknown Exception Thrown");
        }
    }
}
