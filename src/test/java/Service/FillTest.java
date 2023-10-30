package Service;

import DataAccess.Database;
import DataAccess.EventDao;
import DataAccess.PersonDao;
import DataAccess.UserDao;
import Model.User;
import Result.FillResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import Exception.*;

import java.io.FileNotFoundException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class FillTest {
    private FillService serv;
    private User bestUser;
    private Database db;

    @BeforeEach
    public void setUp() throws DataAccessException, FileNotFoundException {
        ClearService cServ = new ClearService();
        cServ.doClear();

        db = new Database();
        db.openConnection();
        UserDao uDao = new UserDao(db.getConnection());
        bestUser = new User("Gamer123", "NoneYa", "basicFortniteBro@gmail.com", "Brent",
                "Childers", "M", "88888888");
        uDao.insert(bestUser);
        db.closeConnection(true);

        serv = new FillService();
    }
    @AfterEach
    public void takeDown() throws DataAccessException, SQLException {
        if(!db.getConnection().isClosed()){
            db.closeConnection(false);
        }
    }
    @Test
    public void fillPass() throws DataAccessException {
        FillResult result =  serv.doFill(bestUser.getUsername(), 4);
        assertTrue(result.isSuccess());

        db = new Database();
        db.openConnection();

        PersonDao pDao = new PersonDao(db.getConnection());
        EventDao eDao = new EventDao(db.getConnection());
        assertNotNull(pDao.find(bestUser.getPersonID()));
        assertNotNull(eDao.findEventID(bestUser.getPersonID(), "Birth"));

        db.closeConnection(true);

    }
    @Test
    public void fillFail() {
        //user not found
        FillResult result =  serv.doFill("RandomUsername", 4);
        assertFalse(result.isSuccess());
    }
    @Test
    public void fillConnPass() throws DataAccessException, FileNotFoundException, Exception {
        //Same thing as earlier but the fill uses a db connection
        db = new Database();
        db.openConnection();

        FillResult result =  serv.doFill(bestUser.getUsername(), 4, db.getConnection());
        assertTrue(result.isSuccess());

        PersonDao pDao = new PersonDao(db.getConnection());
        EventDao eDao = new EventDao(db.getConnection());
        assertNotNull(pDao.find(bestUser.getPersonID()));
        assertNotNull(eDao.findEventID(bestUser.getPersonID(), "Birth"));

        db.closeConnection(true);
    }
    @Test
    public void fillConnFail() throws DataAccessException, FileNotFoundException, Exception {
        //user not found
        db = new Database();
        db.openConnection();

        FillResult result =  serv.doFill("RandomUsername", 4, db.getConnection());
        db.closeConnection(false);
        assertFalse(result.isSuccess());

    }
}
