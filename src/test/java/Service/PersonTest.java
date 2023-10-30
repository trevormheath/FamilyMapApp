package Service;

import Model.Person;
import Request.RegisterRequest;
import Result.PersonResult;
import Result.Person_ID_Result;
import Result.RegisterResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PersonTest {
    private PersonService serv;
    private RegisterResult regRes;

    @BeforeEach
    public void setUp() {
        ClearService cServ = new ClearService();
        cServ.doClear();

        serv = new PersonService();
        RegisterService rServ = new RegisterService();
        RegisterRequest rReq = new RegisterRequest("tempUsername", "123456", "test@gmail.com", "Tyler", "Grant", "M");
        regRes =  rServ.doRegister(rReq);
    }
    @Test
    public void personPass() {
        PersonResult result = serv.person(regRes.getAuthtoken());
        assertTrue(result.isSuccess());
    }
    @Test
    public void personFail() {
        PersonResult result = serv.person("RandomAuthToken");
        assertFalse(result.isSuccess());

    }
    @Test
    public void personIDPass() {
        PersonResult pRes =  serv.person(regRes.getAuthtoken());
        Person[] personList = pRes.getData();
        assertNotNull(personList);
        Person_ID_Result result = serv.person(regRes.getAuthtoken(), personList[0].getPersonID());
        assertTrue(result.isSuccess());
    }
    @Test
    public void personIDFail() {
        Person_ID_Result result = serv.person(regRes.getAuthtoken(), "RandomID");
        assertFalse(result.isSuccess());
    }
}
