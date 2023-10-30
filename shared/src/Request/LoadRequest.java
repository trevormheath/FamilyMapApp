package Request;
import Model.Event;
import Model.Person;
import Model.User;

/**
 * Accepts list of users, persons, and events to attempt and make a tree out of them
 */
public class LoadRequest {
    /**
     *List of users
     */
    private User[] users;
    /**
     *List of persons connected to users
     */
    private Person[] persons;
    /**
     *List of events connected to the persons
     */
    private Event[] events;

    public LoadRequest(User[] users, Person[] persons, Event[] events) {
        this.users = users;
        this.persons = persons;
        this.events = events;
    }

    public User[] getUsers() {
        return users;
    }

    public void setUsers(User[] users) {
        this.users = users;
    }

    public Person[] getPersons() {
        return persons;
    }

    public void setPersons(Person[] persons) {
        this.persons = persons;
    }

    public Event[] getEvents() {
        return events;
    }

    public void setEvents(Event[] events) {
        this.events = events;
    }
}
