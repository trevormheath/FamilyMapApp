package Service;

import Request.RegisterRequest;
import Result.Person_ID_Result;
import Result.RegisterResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RegisterTest {
    private RegisterService serv;
    private RegisterResult result;

    @BeforeEach
    public void setUP() {
        ClearService cServe = new ClearService();
        cServe.doClear();

        serv = new RegisterService();
        RegisterRequest rReq = new RegisterRequest("tempUsername", "123456", "test@gmail.com", "Tyler", "Grant", "M");
        result = serv.doRegister(rReq);
    }
    @Test
    public void registerPass() {
        assertTrue(result.isSuccess());
        PersonService pServ = new PersonService();
        Person_ID_Result pResult = pServ.person(result.getAuthtoken(),result.getPersonID());
        assertTrue(pResult.isSuccess());
    }
    @Test
    public void registerFail() {
        //username already taken
        RegisterRequest failReq = new RegisterRequest("tempUsername", "diffPassword", "fail@gmail.com", "George", "Tyler", "M");
        RegisterResult failResult = serv.doRegister(failReq);

        assertFalse(failResult.isSuccess());
    }
}
