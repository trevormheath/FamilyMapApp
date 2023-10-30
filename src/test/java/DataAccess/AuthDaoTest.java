package DataAccess;

import Exception.*;
import Model.AuthToken;
import Model.User;
import org.junit.jupiter.api.*;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class AuthDaoTest {
    private Database db;
    private AuthToken bestAuth;
    private AuthTokenDao aDao;


    @BeforeEach
    public void setUp() throws DataAccessException {
        // Here we can set up any classes or variables we will need for each test
        // lets create a new instance of the Database class
        db = new Database();
        // and a new event with random data
        bestAuth = new AuthToken("1A2B3C4D5FGHIJK", "World_Peace420");

        // Here, we'll open the connection in preparation for the test case to use it
        Connection conn = db.getConnection();
        //Then we pass that connection to the EventDAO, so it can access the database.
        aDao = new AuthTokenDao(conn);
        //Let's clear the database as well so any lingering data doesn't affect our tests
        aDao.clear();
    }

    @AfterEach
    public void tearDown() {
        // Here we close the connection to the database file, so it can be opened again later.
        // We will set commit to false because we do not want to save the changes to the database
        // between test cases.
        db.closeConnection(false);
    }
    @Test
    public void insertPass() throws DataAccessException {
        aDao.insert(bestAuth);
        AuthToken compareTest = aDao.find(bestAuth.getAuthtoken());
        assertNotNull(compareTest);
        assertEquals(bestAuth, compareTest);
    }
    @Test
    public void insertFail() throws DataAccessException {
        aDao.insert(bestAuth);
        assertThrows(DataAccessException.class, () -> aDao.insert(bestAuth));
    }
    @Test
    public void findPass() throws DataAccessException {
        aDao.insert(bestAuth);
        assertEquals(bestAuth, aDao.find(bestAuth.getAuthtoken()));
    }

    @Test
    public void findFail() throws DataAccessException {
        aDao.insert(bestAuth);
        String diffID = "5555555";
        assertNull(aDao.find(diffID));
    }
    @Test
    public void getTokenPass() throws DataAccessException {
        aDao.insert(bestAuth);
        assertEquals(bestAuth, aDao.getToken(bestAuth.getUsername()));
    }
    @Test
    public void getTokenFail() throws DataAccessException {
        aDao.insert(bestAuth);
        String diffID = "5555555";
        assertNull(aDao.getToken(diffID));
    }
    @Test
    public void clearPass() throws DataAccessException {
        aDao.insert(bestAuth);
        assertEquals(bestAuth, aDao.find(bestAuth.getAuthtoken()));
        assertDoesNotThrow(() -> aDao.clear());
        assertNull(aDao.find(bestAuth.getAuthtoken()));
    }
}
