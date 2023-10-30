package Service;

import Model.Event;
import Request.RegisterRequest;
import Result.EventResult;
import Result.Event_ID_Result;
import Result.RegisterResult;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class EventTest {
    private EventService serv;
    private RegisterService rServ;
    private RegisterResult regRes;

    @BeforeEach
    public void setUp() {
        ClearService cServ = new ClearService();
        cServ.doClear();

        serv = new EventService();
        rServ = new RegisterService();
        RegisterRequest rReq = new RegisterRequest("tempUsername", "123456", "test@gmail.com", "Tyler", "Grant", "M");
        regRes =  rServ.doRegister(rReq);

    }
    @Test
    public void eventPass() {
        EventResult result = serv.event(regRes.getAuthtoken());
        assertTrue(result.isSuccess());
    }
    @Test
    public void eventFail() {
        EventResult result = serv.event("ThisShouldn'tBeTheSameAuth");
        assertFalse(result.isSuccess());
    }
    @Test
    public void eventIDPass() {
        EventResult eRes =  serv.event(regRes.getAuthtoken());
        Event[] eventList = eRes.getData();
        assertNotNull(eventList);
        Event_ID_Result result = serv.event(regRes.getAuthtoken(), eventList[0].getEventID());
        assertTrue(result.isSuccess());
    }
    @Test
    public void eventIDFail() {
        Event_ID_Result result = serv.event(regRes.getAuthtoken(), "RandomID");
        assertFalse(result.isSuccess());
    }
}
