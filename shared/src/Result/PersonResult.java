package Result;
import Model.Person;

/**
 * Result for requesting all the family members of a user
 */
public class PersonResult extends Result {
    /**
     * Array of all the persons connected to the user
     */
    private Person[] data;

    public PersonResult(Person[] data) {
        super(true, null);
        this.data = data;
    }
    public PersonResult(boolean success, String message) {
        super(success, message);
    }

    public Person[] getData() {
        return data;
    }

    public void setData(Person[] data) {
        this.data = data;
    }
}
