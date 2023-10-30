package Service;
import DataAccess.*;
import JsonObjects.*;
import Model.*;
import Result.FillResult;
import com.google.gson.Gson;
import Exception.*;
import java.io.*;
import java.sql.Connection;
import java.util.Random;
import java.util.UUID;

/**
 *Service that processes requests to populate a database for a username
 */
public class FillService {
    private final String[] femNames;
    private final String[] maleNames;
    private final String[] lastNames;
    private final Location[] loc;

    public FillService() throws FileNotFoundException {
        //prepare all the files to be used in the class
        Gson gson = new Gson();
        NameList fNames = gson.fromJson(new FileReader("json/fnames.json"), NameList.class);
        NameList mNames = gson.fromJson(new FileReader("json/mnames.json"), NameList.class);
        NameList lNames = gson.fromJson(new FileReader("json/snames.json"), NameList.class);
        LocArray locations = gson.fromJson(new FileReader("json/locations.json"), LocArray.class);
        femNames = fNames.getData();
        maleNames = mNames.getData();
        lastNames = lNames.getData();
        loc = locations.getData();
    }

    /**
     *Generates 4 generations of data to populate the username with
     * @param username the username for User filling in tree
     * @return FillResult (successful or error response)
     */
    public FillResult doFill(String username, int generation, Connection conn) {
        try {

            //clear the data
            ClearService cServ = new ClearService();
            cServ.doClearUser(conn, username);
            //they have to be registered or else fill doesn't function (error)
            User user = new UserDao(conn).findByUsername(username);

            Person current;
            //if generating a family then execute this to generate parents
            if (generation > 0) {
                UUID dadID = UUID.randomUUID();
                UUID momID = UUID.randomUUID();
                //generate mother side
                generatePerson("f", username, generation, conn, momID.toString(), dadID.toString());
                //generate father side
                generatePerson("m", username, generation, conn, dadID.toString(), momID.toString());
                //generate users person
                current = new Person(user.getPersonID(), username, user.getFirstName(), user.getLastName(), user.getGender(), dadID.toString(), momID.toString(), null);
            } else {
                current = new Person(user.getPersonID(), username, user.getFirstName(), user.getLastName(), user.getGender());
            }
            new PersonDao(conn).insert(current);
            genBirthDate(current, conn);

            //Calculate the number of people and events generated through the recursion
            int numPeople = (int) Math.pow(2, generation + 1) - 1;
            int numEvents = (numPeople * 3) - 2;

            return new FillResult(true, "Successfully added " + numPeople + " persons and " + numEvents + " events to the Database");
        } catch (DataAccessException ex) {
            return new FillResult(false, "Error in filling the DB");
        } catch (FileNotFoundException ex) {
            return new FillResult(false, "Error: File not found");
        } catch (NullPointerException ex) {
            return new FillResult(false, "Error: Null Pointer Exception");
        } catch (Exception ex) {
            return new FillResult(false, "Error: Unknown Exception Thrown");
        }
    }
    /**
     *Fill function for the fillService, generates its own connection then calls sub fill function
     * @param username the username for User filling in tree
     * @param generation number of generations being generated
     * @return FillResult (successful or error response)
     */
    public FillResult doFill(String username, int generation){
        Database db = new Database();
        try {
            db.openConnection();
            FillResult result = doFill(username, generation, db.getConnection());
            db.closeConnection(true);
            return result;
        } catch (DataAccessException ex) {
            db.closeConnection(false);
            return new FillResult(false, "Error in filling the DB");
        }
    }

    /**
     * Has a lot of parameters but this is the recursive function to generate a family based on number of generations
     * @param gender gender of the new person
     * @param username associated username
     * @param generations number of generations past current user
     * @param conn connection variable
     * @param personID the ID of person being generated
     * @param spouseID the personID for the spouse
     * @return Person Object generated
     * @throws DataAccessException When a Dao doesn't work properly
     * @throws Exception Other exceptions such as Null Pointer
     */
    private Person generatePerson(String gender, String username, int generations, Connection conn, String personID, String spouseID) throws DataAccessException, NullPointerException, Exception {
        Person mother = null;
        Person father = null;
        Random rand = new Random();
        String lastName;
        String firstName;
        Person person;

        if(generations > 1) {
            UUID momID = UUID.randomUUID();
            UUID dadID = UUID.randomUUID();
            mother = generatePerson("f", username,generations-1, conn, momID.toString(), dadID.toString());
            father = generatePerson("m", username,generations-1, conn, dadID.toString(), momID.toString());
        }
        if(personID == null) {
            personID = UUID.randomUUID().toString();
        }
        //generate the first name based on gender
        if(gender.equals("f")){
            firstName = femNames[rand.nextInt(femNames.length)];
        } else {
          firstName = maleNames[rand.nextInt(maleNames.length)];
        }
        //last name will be the same as fathers if your father has been generated
        if(father != null){
            lastName = father.getLastName();
            person = new Person(personID, username, firstName, lastName, gender, father.getPersonID(), mother.getPersonID(), spouseID);
        } else {
            //generate the last name for a user and declare them with null parents since they had null values
            lastName = lastNames[rand.nextInt(lastNames.length)];
            person = new Person(personID, username, firstName, lastName, gender, null, null, spouseID);
        }

        new PersonDao(conn).insert(person);
        //generate all events for person
        int birthDay = genBirthDate(person, conn);
        int deathDay = genDeathDate(birthDay, person, conn);
        genMarriageDate(birthDay, deathDay, person, conn);
        return person;
    }

/*


     * In between function to call the separate functions for each event
     * @param person the current person object with information being generated
     * @param conn connection to perform calculations on
     * @throws DataAccessException When a Dao doesn't work properly
     * @throws Exception Other exceptions such as Null Pointer


    private void generateEvents(Person person, Connection conn) throws DataAccessException, NullPointerException, Exception {

        int birthDay = genBirthDate(person, conn);
        int deathDay = genDeathDate(birthDay, person, conn);
        genMarriageDate(birthDay, deathDay, person, conn);

    }
*/

    /**
     * Generates the birthdate for a person object based on parents bdays
     * @param person the current person object with information being generated
     * @param conn connection to perform calculations on
     * @return Birth Date
     * @throws DataAccessException When a Dao doesn't work properly
     * @throws Exception Other exceptions such as Null Pointer
     */
    private int genBirthDate(Person person, Connection conn) throws DataAccessException, NullPointerException, Exception {
        EventDao eDao = new EventDao(conn);
        Random rand = new Random();
        int random = rand.nextInt(loc.length);
        UUID genID = UUID.randomUUID();

        int momBirth;
        int momDeath;
        int fathBirth;
        int fathDeath;

        //If mom and dad are valid objects then use the years of the births and deaths
        if (eDao.findEventID(person.getMotherID(), "Birth") != null && eDao.findEventID(person.getFatherID(), "Birth") != null) {
            momBirth = eDao.find(eDao.findEventID(person.getMotherID(), "Birth")).getYear();
            momDeath = eDao.find(eDao.findEventID(person.getMotherID(), "Death")).getYear();
            fathBirth = eDao.find(eDao.findEventID(person.getFatherID(), "Birth")).getYear();
            fathDeath = eDao.find(eDao.findEventID(person.getFatherID(), "Death")).getYear();
        } else {
            //Else use these "random" years
            momBirth = 1968;
            momDeath = 2050;
            fathBirth = 1965;
            fathDeath = 2045;
        }
        int minBirth;
        //parents >= 13, after parents death date, mom age <= 50

        //Kid can't be born before both parents are older than 13
        if (momBirth > fathBirth) {
            minBirth = momBirth + 13;
        } else {
            minBirth = fathBirth + 13;
        }

        int maxBirthRange;
        //Kid can't be born after menopause or after a parent dies
        if ((momDeath < (momBirth + 50)) || (fathDeath < (momBirth + 50))) {
            if (momDeath < fathDeath) {
                maxBirthRange = momDeath - minBirth;
            } else {
                maxBirthRange = fathDeath - minBirth;
            }
        } else {
            maxBirthRange = (momBirth + 50) - minBirth;
        }

        //random number after minBirthDate within the range created
        int birthDay;
        if (maxBirthRange > 0) {
            birthDay = rand.nextInt(maxBirthRange) + minBirth;
        } else {
            birthDay = minBirth;
        }
        //both people have to be 13 before someone dies too, so birthday needs to be 13 years before either death
        if (person.getSpouseID() != null) {
            //if their wedding has already been created then they need to be born 13 years before it
            Event wedding = eDao.find(eDao.findEventID(person.getSpouseID(), "Marriage"));
            if(wedding != null) {
                if(wedding.getYear() < (birthDay + 13)){
                    birthDay = wedding.getYear() - 13;
                }
            }
            Event spouseDeath = eDao.find(eDao.findEventID(person.getSpouseID(), "Death"));
            if (spouseDeath != null) {
                if (spouseDeath.getYear() < (birthDay + 13)) {
                    birthDay = spouseDeath.getYear() - 13;
                }
            }
        }
        Event birth = new Event(genID.toString(), person.getAssociatedUsername(), person.getPersonID(), loc[random].getLatitude(), loc[random].getLongitude(), loc[random].getCountry(), loc[random].getCity(), "Birth", birthDay);
        eDao.insert(birth);

        return birthDay;
    }

    /**
     * Generates death date randomly using birthdate as reference
     * @param birthDay Day of birth generated for the person
     * @param person the current person object with information being generated
     * @param conn connection to perform calculations on
     * @return Death Date
     * @throws DataAccessException When a Dao doesn't work properly
     * @throws Exception Other exceptions such as Null Pointer
     */
    private int genDeathDate(int birthDay, Person person, Connection conn) throws DataAccessException, NullPointerException, Exception {
        EventDao eDao = new EventDao(conn);
        Random rand = new Random();
        int random = rand.nextInt(loc.length);
        UUID genID = UUID.randomUUID();


        //Can die at 120 but not after, and not before they turn 13. 85 years old min death should do to remove some randomization
        int deathDay = rand.nextInt(110 - 85) + (birthDay + 85);
        //int deathDay = birthDay + 85;
        Event death = new Event(genID.toString(), person.getAssociatedUsername(), person.getPersonID(), loc[random].getLatitude(), loc[random].getLongitude(), loc[random].getCountry(), loc[random].getCity(), "Death", deathDay);
        eDao.insert(death);

        return deathDay;
    }

    /**
     * Generates a marriage day and matches it to their spouses if spouses record has already been written
     * @param birthDay Day of birth generated for the person
     * @param deathDay Day of death generated for the person
     * @param person the current person object with information being generated
     * @param conn connection to perform calculations on
     * @throws DataAccessException When a Dao doesn't work properly
     * @throws Exception Other exceptions such as Null Pointer
     */
    public void genMarriageDate(int birthDay, int deathDay, Person person, Connection conn) throws DataAccessException, NullPointerException, Exception {
            EventDao eDao = new EventDao(conn);
            Random rand = new Random();
            int random = rand.nextInt(loc.length);
            UUID genID = UUID.randomUUID();

            //age >= 13, in between death and birth, match year and location to spouse, spouse must also be older than 13
            int wedDay = rand.nextInt(deathDay - (birthDay+13)) + (birthDay + 13);
            //if spouse has already generated a wedding day then use their info else create new wedding information
            if (eDao.findEventID(person.getSpouseID(), "Marriage") == null) {
                Event marriage = new Event(genID.toString(), person.getAssociatedUsername(), person.getPersonID(), loc[random].getLatitude(), loc[random].getLongitude(), loc[random].getCountry(), loc[random].getCity(), "Marriage", wedDay);
                eDao.insert(marriage);
            } else {
                Event wedding = eDao.find(eDao.findEventID(person.getSpouseID(), "Marriage"));
                Event marriage = new Event(genID.toString(), person.getAssociatedUsername(), person.getPersonID(), wedding.getLatitude(), wedding.getLongitude(), wedding.getCountry(), wedding.getCity(), "Marriage", wedding.getYear());
                eDao.insert(marriage);
            }
    }
}
