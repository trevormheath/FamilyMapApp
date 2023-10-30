package Service;
import DataAccess.*;
import Model.Person;
import Result.PersonResult;
import Result.Person_ID_Result;
import Exception.*;

/**
 *Service that is processed when the person command is run
 */
public class PersonService {

    public PersonService() {
    }

    /**
     *Returns all family members of the current user
     * @param authtoken required for verification
     * @return PersonResult an array of person objects (or an error)
     */
    public PersonResult person(String authtoken){
        Database db = new Database();
        try {
            db.openConnection();

            PersonDao pDao = new PersonDao(db.getConnection());
            AuthTokenDao authDao = new AuthTokenDao(db.getConnection());

            //Find the username of a AuthToken object
            String username = authDao.find(authtoken).getUsername();
            if(username == null){
                throw new AuthTokenException();
            }
            //Return the array of people from the Dao
            Person[] personList = pDao.getFamily(username);

            db.closeConnection(true);
            return new PersonResult(personList);
        } catch (DataAccessException ex){
            ex.printStackTrace();
            db.closeConnection(false);
            return new PersonResult(false, "Error Accessing Database");
        } catch (AuthTokenException ex) {
            ex.printStackTrace();
            db.closeConnection(false);
            return new PersonResult(false, "Error: AuthToken not valid");
        } catch (Exception ex) {
            ex.printStackTrace();
            db.closeConnection(false);
            return new PersonResult(false, "Error: Unknown Error Thrown");
        }
    }
    /**
     *Person command with specific ID will return that person info
     * @param personID info for a specific person
     * @param authtoken required for verification
     * @return Person_ID_Result which is all of this person info (or an error if not found)
     */
    public Person_ID_Result person(String authtoken, String personID){
        Database db = new Database();
        try {
            db.openConnection();

            PersonDao pDao = new PersonDao(db.getConnection());
            AuthTokenDao authDao = new AuthTokenDao(db.getConnection());

            //Check for an individual personID in the database
            Person person = pDao.find(personID);
            String username = person.getAssociatedUsername();
            //Make sure the Authtoken is correct
            if(!username.equals(authDao.find(authtoken).getUsername())){
                throw new AuthTokenException();
            }

            db.closeConnection(true);
            return new Person_ID_Result(person.getAssociatedUsername(), person.getPersonID(), person.getFirstName(), person.getLastName(), person.getGender(), person.getFatherID(), person.getMotherID(), person.getSpouseID());
        } catch (DataAccessException ex){
            ex.printStackTrace();
            db.closeConnection(false);
            return new Person_ID_Result(false, "Error Accessing Database");
        } catch (AuthTokenException ex) {
            ex.printStackTrace();
            db.closeConnection(false);
            return new Person_ID_Result(false, "Error: AuthToken not valid");
        } catch (Exception ex) {
            ex.printStackTrace();
            db.closeConnection(false);
            return new Person_ID_Result(false, "Error: Unknown Error Thrown");
        }
    }
}
