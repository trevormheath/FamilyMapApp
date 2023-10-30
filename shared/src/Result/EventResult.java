package Result;
import Model.Event;

/**
 * Result of a request to see all events
 */
public class EventResult extends Result {
    /**
     *Array of all events connected to a user
     */
    private Event[] data;

    public EventResult(Event[] data) {
        super(true, null);
        this.data = data;
    }
    public EventResult(boolean success, String message) {
        super(success, message);
    }

    public Event[] getData() {
        return data;
    }

    public void setData(Event[] data) {
        this.data = data;
    }
}
