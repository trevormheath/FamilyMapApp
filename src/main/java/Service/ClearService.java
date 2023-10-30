package Service;
import Exception.*;
import DataAccess.Database;
import Result.ClearResult;
import DataAccess.*;

import java.sql.Connection;

/**
 *Service to process the clear function
 */
public class ClearService {

    public ClearService() {
    }

    /**
     *Clears all data from the database
     * @return ClearResult (successful or error response)
     */
    public ClearResult doClear(){
        Database db = new Database();
        try {
            db.openConnection();

            //Clear all information in all tables
            new PersonDao(db.getConnection()).clear();
            new UserDao(db.getConnection()).clear();
            new EventDao(db.getConnection()).clear();
            new AuthTokenDao(db.getConnection()).clear();

            db.closeConnection(true);
            return new ClearResult(true, "Clear Succeeded.");
        } catch (DataAccessException ex){
            ex.printStackTrace();
            db.closeConnection(false);
            return new ClearResult(false, "Error Accessing Database");
        } catch (Exception ex){
            ex.printStackTrace();
            db.closeConnection(false);
            return new ClearResult(false, "Error: Unknown Exception Thrown");
        }
    }

    //Doesn't close the connection since it is being used by another function
    /**
     * Clear service function but uses a connection provided by another service
     * @param conn
     * @return
     */
    public ClearResult doClear(Connection conn){
        try {
            //clear the database besides the users using a pre-existing connection
            new PersonDao(conn).clear();
            new EventDao(conn).clear();
            new UserDao(conn).clear();
            new AuthTokenDao(conn).clear();

            return new ClearResult(true, "Clear Succeeded.");
        } catch (DataAccessException ex){
            ex.printStackTrace();
            return new ClearResult(false, "Error Accessing Database");
        } catch (Exception ex){
            ex.printStackTrace();
            return new ClearResult(false, "Error: Unknown Exception Thrown");
        }
    }

    /**
     * Another clear service function but only for one users info (someone wants to reset their data)
     * @param conn
     * @param username
     * @return
     */
    public ClearResult doClearUser(Connection conn, String username){
        try {
            //clear the database for one users family
            new PersonDao(conn).clearUser(username);
            new EventDao(conn).clearUser(username);

            return new ClearResult(true, "Clear Succeeded.");
        } catch (DataAccessException ex){
            ex.printStackTrace();
            return new ClearResult(false, "Error Accessing Database");
        } catch (Exception ex){
            ex.printStackTrace();
            return new ClearResult(false, "Error: Unknown Exception Thrown");
        }
    }
}
