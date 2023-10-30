package Result;

/**
 *The result of finding a person object with a specific personID
 */
public class Person_ID_Result extends Result {
    /**
     *The username associated with the person
     */
    private String associatedUsername;
    /**
     *The ID associated with the person
     */
    private String personID;
    /**
     *The first name associated with the person
     */
    private String firstName;
    /**
     *The last name associated with the person
     */
    private String lastName;
    /**
     *The gender associated with the person
     */
    private  String gender;
    /**
     *The fatherID associated with the person (optional and could be null)
     */
    private String fatherID;
    /**
     *The motherID associated with the person (optional and could be null)
     */
    private String motherID;
    /**
     *The spouseID associated with the person (optional and could be null)
     */
    private String spouseID;

    public Person_ID_Result(String associatedUsername, String personID, String firstName, String lastName, String gender, String fatherID, String motherID, String spouseID) {
        super(true, null);
        this.associatedUsername = associatedUsername;
        this.personID = personID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;
    }
    public Person_ID_Result(boolean success, String message){
        super(success, message);
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFatherID() {
        return fatherID;
    }

    public void setFatherID(String fatherID) {
        this.fatherID = fatherID;
    }

    public String getMotherID() {
        return motherID;
    }

    public void setMotherID(String motherID) {
        this.motherID = motherID;
    }

    public String getSpouseID() {
        return spouseID;
    }

    public void setSpouseID(String spouseID) {
        this.spouseID = spouseID;
    }
}
