package Service;
import DataAccess.*;
import Model.AuthToken;
import Model.Event;
import Result.EventResult;
import Result.Event_ID_Result;
import Exception.*;

/**
 *Service that is processed when the event command is run
 */
public class EventService {

    public EventService() {
    }

    /**
     * Gets an array of all events in the tree of the user
     * @param authtoken Used to find who the user is
     * @return EventResult An array of event objects (or an error)
     */
    public EventResult event(String authtoken){
        Database db = new Database();
        try {
            db.openConnection();

            EventDao eDao = new EventDao(db.getConnection());
            AuthTokenDao authDao = new AuthTokenDao(db.getConnection());

            AuthToken tempAuth = authDao.find(authtoken);
            //if unable to find the authToken then it wasn't valid
            if(tempAuth == null){
                throw new AuthTokenException();
            }
            String username = tempAuth.getUsername();
            Event[] eventList = eDao.getEvents(username);

            db.closeConnection(true);
            return new EventResult(eventList);
        } catch (DataAccessException ex){
            ex.printStackTrace();
            db.closeConnection(false);
            return new EventResult(false, "Error Accessing Database");
        } catch (AuthTokenException ex) {
            ex.printStackTrace();
            db.closeConnection(false);
            return new EventResult(false, "Error: AuthToken not valid");
        } catch (Exception ex) {
            ex.printStackTrace();
            db.closeConnection(false);
            return new EventResult(false, "Error Unknown Exception Thrown");
        }
    }

    /**
     *Finds a specific event based on the ID provided
     * @param authtoken Used to find who the user is
     * @param eventID Used to determind the event
     * @return Event_ID_Result All the information relating to the found event (or an error)
     */
    public Event_ID_Result event(String authtoken, String eventID){
        Database db = new Database();
        try {
            db.openConnection();

            EventDao eDao = new EventDao(db.getConnection());
            AuthTokenDao authDao = new AuthTokenDao(db.getConnection());

            //Find the event associated with the ID
            Event event = eDao.find(eventID);
            String username = event.getAssociatedUsername();
            //If the events username doesn't match username of authToken then throw error
            if(!username.equals(authDao.find(authtoken).getUsername())){
                throw new AuthTokenException();
            }
            db.closeConnection(true);
            return new Event_ID_Result(event.getAssociatedUsername(), event.getEventID(), event.getPersonID(), event.getLatitude(), event.getLongitude(), event.getCountry(), event.getCity(), event.getEventType(), event.getYear());
        } catch (DataAccessException ex){
            ex.printStackTrace();
            db.closeConnection(false);
            return new Event_ID_Result(false, "Error Accessing Database");
        } catch (AuthTokenException ex) {
            ex.printStackTrace();
            db.closeConnection(false);
            return new Event_ID_Result(false, "Error: AuthToken not valid");
        } catch (Exception ex) {
            ex.printStackTrace();
            db.closeConnection(false);
            return new Event_ID_Result(false, "Error: Unknown Exception Thrown");
        }
    }
}
