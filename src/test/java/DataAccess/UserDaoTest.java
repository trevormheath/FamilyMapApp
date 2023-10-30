package DataAccess;

import Model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import Exception.*;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class UserDaoTest {
    private Database db;
    private User bestUser;
    private UserDao uDao;

    @BeforeEach
    public void setUp() throws DataAccessException {
        db = new Database();

        bestUser = new User("Gamer123", "NoneYa", "basicFortniteBro@gmail.com", "Brent",
                "Childers", "M", "88888888");

        Connection conn = db.getConnection();
        uDao = new UserDao(conn);
        uDao.clear();
    }

    @AfterEach
    public void tearDown() {
        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        uDao.insert(bestUser);
        User compareTest = uDao.find(bestUser.getPersonID());
        assertNotNull(compareTest);
        assertEquals(bestUser, compareTest);
    }

    @Test
    public void insertFail() throws DataAccessException {
        uDao.insert(bestUser);
        assertThrows(DataAccessException.class, () -> uDao.insert(bestUser));
    }

    @Test
    public void findPass() throws DataAccessException {
        uDao.insert(bestUser);
        assertEquals(bestUser, uDao.find(bestUser.getPersonID()));
    }

    @Test
    public void findFail() throws DataAccessException {
        uDao.insert(bestUser);
        String diffID = "5555555";
        assertNull(uDao.find(diffID));
    }

    @Test
    public void clearPass() throws DataAccessException {
        uDao.insert(bestUser);
        assertEquals(bestUser, uDao.find(bestUser.getPersonID()));
        assertDoesNotThrow(() -> uDao.clear());
        assertNull(uDao.find(bestUser.getPersonID()));
    }
    @Test
    public void findUserPass() throws DataAccessException {
        uDao.insert(bestUser);
        assertEquals(bestUser, uDao.findByUsername(bestUser.getUsername()));
    }
    @Test
    public void findUserFail() throws DataAccessException {
        uDao.insert(bestUser);
        String diffID = "5555555";
        assertNull(uDao.findByUsername(diffID));
    }
    @Test
    public void validatePass() throws DataAccessException {
        uDao.insert(bestUser);
        assertNotNull(uDao.find(bestUser.getPersonID()));
        assertTrue(uDao.validate(bestUser.getUsername(), bestUser.getPassword()));
    }
    @Test
    public void validateFail() throws DataAccessException {
        uDao.insert(bestUser);
        assertNotNull(uDao.find(bestUser.getPersonID()));
        assertFalse(uDao.validate(bestUser.getUsername(), "RandomPassword"));
        assertFalse(uDao.validate("IncorrectUsername", bestUser.getPassword()));
    }
}
