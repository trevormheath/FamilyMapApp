package Service;
import DataAccess.*;
import Model.Event;
import Model.Person;
import Model.User;
import Request.LoadRequest;
import Result.LoadResult;
import Exception.*;
/**
 *This service runs when the load command is ran
 */
public class LoadService {

    public LoadService() {
    }

    /**
     *Clears all data from database and then uploads it's own data from param
     * @param r, new database info
     * @return LoadResult (successful or error response)
     */
    public LoadResult doLoad(LoadRequest r){
        Database db = new Database();

        try {
            db.openConnection();

            //Clear the Current Database with the current Connection
            ClearService cServ = new ClearService();
            cServ.doClear(db.getConnection());

            int numUsers = 0;
            int numPersons = 0;
            int numEvents = 0;
            UserDao userDao = new UserDao(db.getConnection());
            PersonDao personDao = new PersonDao(db.getConnection());
            EventDao eventDao = new EventDao(db.getConnection());

            //Insert all of the information from the Json Array
            for(User user : r.getUsers()){
               userDao.insert(user);
               numUsers++;
            }
            for(Person person : r.getPersons()){
                personDao.insert(person);
                numPersons++;
            }
            for(Event event : r.getEvents()){
                eventDao.insert(event);
                numEvents++;
            }
            db.closeConnection(true);
            return new LoadResult(true, "Successfully added " + numUsers + " users, " + numPersons + " persons, and " + numEvents + " events to the database.");

        } catch (DataAccessException ex){
            ex.printStackTrace();
            db.closeConnection(false);
            return new LoadResult(false, "Error in Loading DB");
        } catch (Exception ex) {
            ex.printStackTrace();
            db.closeConnection(false);
            return new LoadResult(false, "Error: Unknown Exception Thrown");
        }
    }
}
