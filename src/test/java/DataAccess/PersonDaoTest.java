package DataAccess;

import Model.Event;
import Model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import Exception.*;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class PersonDaoTest {
    private Database db;
    private Person bestPerson;
    private PersonDao pDao;

    @BeforeEach
    public void setUp() throws DataAccessException {
        db = new Database();

        bestPerson = new Person("6294578", "Gamer123", "Brent", "Davis",
                "M", "4433221", "2323231", "1111111");

        Connection conn = db.getConnection();
        pDao = new PersonDao(conn);
        pDao.clear();
    }

    @AfterEach
    public void tearDown() {
        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        pDao.insert(bestPerson);
        Person compareTest = pDao.find(bestPerson.getPersonID());
        assertNotNull(compareTest);
        assertEquals(bestPerson, compareTest);
    }

    @Test
    public void insertFail() throws DataAccessException {
        pDao.insert(bestPerson);
        assertThrows(DataAccessException.class, () -> pDao.insert(bestPerson));
    }

    @Test
    public void findPass() throws DataAccessException {
        //the same as insert?
        pDao.insert(bestPerson);
        assertEquals(bestPerson, pDao.find(bestPerson.getPersonID()));
    }

    @Test
    public void findFail() throws DataAccessException {
        pDao.insert(bestPerson);
        String diffID = "5555555";
        assertNull(pDao.find(diffID));
    }

    @Test
    public void clearPass() throws DataAccessException {
        pDao.insert(bestPerson);
        assertEquals(bestPerson, pDao.find(bestPerson.getPersonID()));
        assertDoesNotThrow(() -> pDao.clear());
        assertNull(pDao.find(bestPerson.getPersonID()));
    }

    @Test
    public void clearUserPass() throws DataAccessException {
        Person tempPerson = new Person("1239878888888", "Let'sGetItStartedInHere", "Trent", "Castoway",
                "M", "12364534", "34523234", "2346632");
        pDao.insert(tempPerson);
        pDao.insert(bestPerson);
        assertEquals(tempPerson, pDao.find(tempPerson.getPersonID()));
        assertEquals(bestPerson, pDao.find(bestPerson.getPersonID()));

        assertDoesNotThrow(() -> pDao.clearUser(bestPerson.getAssociatedUsername()));
        assertEquals(tempPerson, pDao.find(tempPerson.getPersonID()));
        assertNull(pDao.find(bestPerson.getPersonID()));
    }

    @Test
    public void getFamPass() throws DataAccessException {
        Person tempPerson = new Person("123987", "Let'sGetItStartedInHere", "Trent", "Castoway",
                "M", "66666666", "00000000", "1111111");
        Person tempPerson2 = new Person("892573", bestPerson.getAssociatedUsername(), "Travis", "Scott",
                "M", "2956543", "123124", "974562");
        Person[] personList = {bestPerson, tempPerson2};

        pDao.insert(bestPerson);
        pDao.insert(tempPerson);
        pDao.insert(tempPerson2);
        Person[] retList = pDao.getFamily(bestPerson.getAssociatedUsername());
        assertEquals(2, retList.length);
        assertEquals(personList[0].getPersonID(), retList[0].getPersonID());
        assertEquals(personList[1].getPersonID(), retList[1].getPersonID());
    }
    @Test
    public void getFamFail() throws DataAccessException {
        Person tempPerson = new Person("123987", "Let'sGetItStartedInHere", "Trent", "Castoway",
                "M", "66666666", "00000000", "1111111");

        Person[] personList = {bestPerson, tempPerson};

        pDao.insert(bestPerson);
        pDao.insert(tempPerson);
        assertNull(pDao.getFamily("FakeUsername"));
    }
}
