package org.assignment.util.extended;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import org.assignment.util.FirebaseConnection;
import org.assignment.util.base.DatabaseOperations;
import org.assignment.util.base.Person;
import org.assignment.util.enums.StaffPosition;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Staff extends Person implements DatabaseOperations {
    protected char gender;
    protected StaffPosition position;
    protected LocalDate dateJoined;
    protected double salary;
    protected String password;
    private static final CollectionReference staffCollection = FirebaseConnection.getDb().collection("Staff");

    public Staff() {
        this("", "", "", "", "", '\u0000', null, null, 0, "");
    }

    public Staff(String id, String name, String contactNumber, String email, String address, char gender, StaffPosition position, LocalDate dateJoined, double salary, String password) {
        super(id, name, contactNumber, email, address);
        this.gender = gender;
        this.position = position;
        this.dateJoined = dateJoined;
        this.salary = salary;
        this.password = password;
    }

    // setters
    public void setGender(char gender) { this.gender = gender; }
    public void setPosition(StaffPosition position) { this.position = position; }
    public void setDateJoined(LocalDate dateJoined) { this.dateJoined = dateJoined; }
    public void setSalary(double salary) { this.salary = salary; }
    public void setPassword(String password) { this.password = password; }

    // getters
    public char getGender() { return this.gender; }
    public StaffPosition getPosition() { return this.position; }
    public LocalDate getDateJoined() { return this.dateJoined; }
    public double getSalary() { return this.salary; }
    public String getPassword() { return this.password; }
    public static CollectionReference getStaffCollection() { return Staff.staffCollection; }

    // database operations implementation
    public boolean addToDb() {
        // add self to db
        return true;
    }

    public static Staff getFromDb(String id) {
        try {
            // get from cache if available
            Staff cachedStaffObject = (Staff) FirebaseConnection.getCachedDocument(staffCollection, staffCollection.document(id));
            if (cachedStaffObject != null) return cachedStaffObject;

            // fetch from db
            DocumentSnapshot doc = staffCollection.document(id).get().get();
            if (!doc.exists()) return null;
            Map<String, Object> docData = doc.getData();
            if (docData == null) return null;

            // create new instance and assign values
            Staff fetchedStaff = new Staff();
            fetchedStaff.setId(doc.getId());
            fetchedStaff.setName(Optional.ofNullable(docData.get("name").toString()).orElse(""));
            fetchedStaff.setContactNumber(Optional.ofNullable(docData.get("contactNumber").toString()).orElse(""));
            fetchedStaff.setEmail(Optional.ofNullable(docData.get("email").toString()).orElse(""));
            fetchedStaff.setAddress(Optional.ofNullable(docData.get("address").toString()).orElse(""));
            fetchedStaff.setGender(Optional.ofNullable((char) docData.get("gender")).orElse('\u0000'));
            fetchedStaff.setPosition(StaffPosition.fromString(docData.get("position").toString()));
//            fetchedStaff.setDateJoined(Optional.ofNullable(docData.get("dateJoined").toString()).orElse("")); TODO: parse to LocalDate
            fetchedStaff.setSalary(Double.parseDouble(Optional.ofNullable(docData.get("salary")).orElse(0.0).toString()));
            fetchedStaff.setPassword(Optional.ofNullable(docData.get("password").toString()).orElse(""));

            // cache and return
            FirebaseConnection.cacheNewDocument(staffCollection, staffCollection.document(id), fetchedStaff);
            return fetchedStaff;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public boolean updateToDb(String fieldName) {
        // update self to db
        return true;
    }

    public boolean deleteFromDb() {
        // delete from db where id is matching
        return true;
    }
}
