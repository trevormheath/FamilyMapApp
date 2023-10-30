package Service;

import DataAccess.*;
import Exception.*;
import Request.RegisterRequest;
import Result.ClearResult;
import Result.Person_ID_Result;
import Result.RegisterResult;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClearTest {
    private ClearService serv;
    private RegisterResult rResult;
    private PersonService pServ;

   @BeforeEach
    public void setUp() {
       serv = new ClearService();
       serv.doClear();

       RegisterService rServ = new RegisterService();
       RegisterRequest rReq = new RegisterRequest("tempUsername", "123456", "test@gmail.com", "Tyler", "Grant", "M");
       rResult = rServ.doRegister(rReq);

       pServ = new PersonService();
   }

    @Test
    public void doClearPass() {
        ClearResult result = serv.doClear();
        assertTrue(result.isSuccess());
        Person_ID_Result pResult = pServ.person(rResult.getAuthtoken(), rResult.getPersonID());
        assertFalse(pResult.isSuccess());
    }
    @Test
    public void doClearEmpty() {
        serv.doClear();
        //now empty database
        ClearResult result = serv.doClear();
        assertTrue(result.isSuccess());
        //double cleared hopefully it's not still there haha
        Person_ID_Result pResult = pServ.person(rResult.getAuthtoken(), rResult.getPersonID());
        assertFalse(pResult.isSuccess());
    }
    @Test
    public void doClearConnPass() throws DataAccessException {
        Database db = new Database();
        db.openConnection();
        ClearResult result = serv.doClear(db.getConnection());
        db.closeConnection(true);
        assertTrue(result.isSuccess());

        Person_ID_Result pResult = pServ.person(rResult.getAuthtoken(), rResult.getPersonID());
        assertFalse(pResult.isSuccess());
    }
    @Test
    public void doClearConnEmpty() throws DataAccessException {
        serv.doClear();
        //now an empty database to start out
        Database db = new Database();
        db.openConnection();
        ClearResult result = serv.doClear(db.getConnection());
        db.closeConnection(true);
        assertTrue(result.isSuccess());

        Person_ID_Result pResult = pServ.person(rResult.getAuthtoken(), rResult.getPersonID());
        assertFalse(pResult.isSuccess());
    }
    @Test
    public void doClearUserPass() throws DataAccessException {
        RegisterService rServ = new RegisterService();
        RegisterRequest rReq = new RegisterRequest("DifferentUser", "678543", "anyone@gmail.com", "Coco", "Scott", "F");
        RegisterResult res1 = rServ.doRegister(rReq);
        RegisterRequest rReq2 = new RegisterRequest("randUser", "654223", "test52@gmail.com", "Jackson", "Mike", "M");
        RegisterResult res2 = rServ.doRegister(rReq2);

        Database db = new Database();
        db.openConnection();
        ClearResult result = serv.doClearUser(db.getConnection(), rReq.getUsername());
        db.closeConnection(true);
        assertTrue(result.isSuccess());

        PersonService pServ = new PersonService();
        assertTrue(pServ.person(res2.getAuthtoken(), res2.getPersonID()).isSuccess());
        assertFalse(pServ.person(res2.getAuthtoken(), res1.getPersonID()).isSuccess());
    }
    @Test
    public void doClearUserEmpty() throws DataAccessException {
       serv.doClear();
       //now empty database

        Database db = new Database();
        db.openConnection();
        ClearResult result = serv.doClearUser(db.getConnection(), "RandomUsername");
        db.closeConnection(true);
        assertTrue(result.isSuccess());

        Person_ID_Result pResult = pServ.person(rResult.getAuthtoken(), rResult.getPersonID());
        assertFalse(pResult.isSuccess());
    }
}
