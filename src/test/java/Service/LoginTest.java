package Service;

import Request.LoginRequest;
import Request.RegisterRequest;
import Result.LoginResult;
import Result.RegisterResult;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginTest {
    private LoginService serv;
    private LoginRequest req;

    @BeforeEach
    public void setUp() {
        ClearService cServe = new ClearService();
        cServe.doClear();

        RegisterService rServ = new RegisterService();
        RegisterRequest rReq = new RegisterRequest("tempUsername", "123456", "test@gmail.com", "Tyler", "Grant", "M");
        RegisterResult result = rServ.doRegister(rReq);

        serv = new LoginService();
        req = new LoginRequest(rReq.getUsername(), rReq.getPassword());
    }
    @Test
    public void loginPass() {
        LoginResult result = serv.doLogin(req);
        assertTrue(result.isSuccess());
    }
    @Test
    public  void loginFail() {
        LoginRequest failReq = new LoginRequest("tempUsername", "wrongPassword");
        LoginRequest failReqTwo = new LoginRequest("wrongUsername", "123456");

        LoginResult result = serv.doLogin(failReq);
        assertFalse(result.isSuccess());

        result = serv.doLogin(failReqTwo);
        assertFalse(result.isSuccess());

    }
}
