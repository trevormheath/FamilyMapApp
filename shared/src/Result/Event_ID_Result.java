package Result;

/**
 *Results of requesting a specific Event by ID
 */
public class Event_ID_Result extends Result{
    /**
     *Username associated with the eventID
     */
    private String associatedUsername;
    /**
     *eventID if successful
     */
    private String eventID;
    /**
     *the personID associated with the Event
     */
    private  String personID;
    /**
     *Latitude of the Event
     */
    private float latitude;
    /**
     *Longitude of the Event
     */
    private float longitude;
    /**
     *Country the Event took place in
     */
    private String country;
    /**
     *City the Event took place in
     */
    private String city;
    /**
     *Type of Event occurred (marriage, birth, death, etc)
     */
    private String eventType;
    /**
     *What year the Event took place in
     */
    private int year;

    public Event_ID_Result(String associatedUsername, String eventID, String personID, float latitude, float longitude, String country, String city, String eventType, int year) {
        super(true, null);
        this.associatedUsername = associatedUsername;
        this.eventID = eventID;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
    }
    public Event_ID_Result(boolean success, String message){
        super(success, message);
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
