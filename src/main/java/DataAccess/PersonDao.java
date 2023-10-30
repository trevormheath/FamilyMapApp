package DataAccess;

import Exception.DataAccessException;
import Model.Person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 *Class to act as middle ground between database and person objects
 */
public class PersonDao {
    private final Connection conn;

    public PersonDao(Connection conn) {
        this.conn = conn;
    }

    /**
     *Add a person to the DB
     */
    public void insert(Person person) throws DataAccessException {
        String sql = "INSERT INTO Person (PersonID, AssociatedUsername, FirstName, LastName, Gender, FatherID, " +
                "MotherID, SpouseID) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, person.getPersonID());
            stmt.setString(2, person.getAssociatedUsername());
            stmt.setString(3, person.getFirstName());
            stmt.setString(4, person.getLastName());
            stmt.setString(5, person.getGender());
            stmt.setString(6, person.getFatherID());
            stmt.setString(7, person.getMotherID());
            stmt.setString(8, person.getSpouseID());

            stmt.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting an Person into the database");
        }
    }
    /**
     *Get the person associated with given personID
     */
    public Person find(String personID) throws DataAccessException {
        Person person;
        ResultSet rs;
        String sql = "SELECT * FROM Person WHERE PersonID = ?;";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, personID);
            rs = stmt.executeQuery();
            if(rs.next()){
                person = new Person(rs.getString("PersonID"), rs.getString("AssociatedUsername"), rs.getString("FirstName"),
                        rs.getString("LastName"), rs.getString("Gender"), rs.getString("FatherID"),
                        rs.getString("MotherID"), rs.getString("SpouseID"));
                return person;
            } else{
                return null;
            }
        } catch (SQLException e){
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding a person in the database");
        }
    }

    /**
     * Removes all the Person data
     */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM Person";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing the person table");
        }
    }
    /**
     * Removes all the Person data for a user
     */
    public void clearUser(String username) throws DataAccessException {
        String sql = "DELETE FROM Person WHERE AssociatedUsername = ?";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, username);
            stmt.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing the person table");
        }
    }

    /**
     * Return an array of all persons connected below an ID
     * @param username value of a person added in a tree
     * @return Person[] an array of persons
     */
    public Person[] getFamily(String username) throws DataAccessException {
        ResultSet rs;

        String sql = "SELECT * FROM Person WHERE AssociatedUsername = ?";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            int numEntries = 0;
            while (rs.next()){
                numEntries++;
            }
            rs = stmt.executeQuery();
            Person[] personList = new Person[numEntries];
            int i = 0;
            while(rs.next()){
                Person person = new Person(rs.getString("PersonID"), rs.getString("AssociatedUsername"), rs.getString("FirstName"),
                        rs.getString("LastName"), rs.getString("Gender"), rs.getString("FatherID"),
                        rs.getString("MotherID"), rs.getString("SpouseID"));
                personList[i] = person;
                i++;
            }
            if(personList.length == 0){
                return null;
            }
            return personList;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered finding family Data in Person");
        }
    }


}
