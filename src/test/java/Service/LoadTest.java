package Service;


import DataAccess.Database;
import DataAccess.EventDao;
import DataAccess.PersonDao;
import DataAccess.UserDao;
import Model.Event;
import Model.Person;
import Model.User;
import Request.LoadRequest;
import Result.LoadResult;
import Exception.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoadTest {
    private LoadService serv;
    private LoadRequest req;
    private User bestUser;
    private Person bestPerson;
    private Event bestEvent;

    @BeforeEach
    public void setUp() throws DataAccessException {
        ClearService cServe = new ClearService();
        cServe.doClear();

        bestUser = new User("Gamer123", "NoneYa", "basicFortniteBro@gmail.com", "Brent",
                "Childers", "M", "88888888");
        bestPerson = new Person("6294578", "Gamer123", "Brent", "Davis",
                "M", "4433221", "2323231", "1111111");
        bestEvent = new Event("Biking_123A", "Gale", "Gale123A",
                35.9f, 140.1f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        User[] userList = new User[1];
        Person[] personList = new Person[1];
        Event[] eventList = new Event[1];

        userList[0] = bestUser;
        personList[0] = bestPerson;
        eventList[0] = bestEvent;

        serv = new LoadService();
        req = new LoadRequest(userList, personList, eventList);
    }
    @Test
    public void loadPass() throws DataAccessException{
        LoadResult result = serv.doLoad(req);

        Database db  = new Database();
        db.getConnection();
        assertNotNull(new UserDao(db.getConnection()).find(bestUser.getPersonID()));
        assertNotNull(new PersonDao(db.getConnection()).find(bestPerson.getPersonID()));
        assertNotNull(new EventDao(db.getConnection()).find(bestEvent.getEventID()));
        db.closeConnection(false);

        assertTrue(result.isSuccess());
    }
    @Test
    public void loadFail() {
        User[] testList = new User[1];
        testList[0] = null;
        req.setUsers(testList);

        LoadResult result = serv.doLoad(req);
        assertFalse(result.isSuccess());

    }
}
