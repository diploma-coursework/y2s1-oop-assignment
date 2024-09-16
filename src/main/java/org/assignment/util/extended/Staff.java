package org.assignment.util.extended;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import org.assignment.util.DbMethods;
import org.assignment.util.FirebaseConnection;
import org.assignment.util.base.DatabaseOperations;
import org.assignment.util.base.Person;
import org.assignment.util.enums.StaffPosition;
import org.assignment.util.validationCheck;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.Timestamp;
import java.time.ZoneId;
import org.mindrot.jbcrypt.BCrypt;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Staff extends Person implements DatabaseOperations {
    protected String gender;
    protected StaffPosition position;
    protected LocalDate dateJoined;
    protected double salary;
    protected String password;
    private static final CollectionReference staffCollection = FirebaseConnection.getDb().collection("Staff");

    public Staff() {
    }

    public Staff(String name, String contactNumber, String email, String address, String gender, StaffPosition position, LocalDate dateJoined, double salary, String password) {
        super(name, contactNumber, email, address);
        setGender(gender);
        setPosition(position);
        setDateJoined(dateJoined);
        setSalary(salary);
        setPassword(password);
    }

    // setters
    public void setGender(String gender) {
        this.gender = validationCheck.checkGender(gender.charAt(0));
    }
    public void setPosition(StaffPosition position) {
        String positionName = position.name();
        // Validate the position
        if (validationCheck.isValidPosition(positionName)) {
            this.position = position;
        } else {
            throw new IllegalArgumentException("Invalid position: " + positionName);
        }
    }
    public void setDateJoined(LocalDate dateJoined) {
        validationCheck.checkDateJoined(dateJoined);
        this.dateJoined = dateJoined;
    }
    public void setSalary(double salary) {
        this.salary = validationCheck.checkSalary((int) salary);
    }

    public void setPassword(String password) {
        //this.password = validationCheck.checkPassword(password);

        // Validate the password first
        String validatedPassword = validationCheck.checkPassword(password);
        // Hash the validated password using BCrypt
        this.password = BCrypt.hashpw(validatedPassword, BCrypt.gensalt());
    }

    @Override
    public void setName(String name) {
        validationCheck.checkAndFormatName(name);
        super.setName(name);
    }

    @Override
    public void setContactNumber(String contactNumber) {
        validationCheck.checkContactNumber(contactNumber);
        super.setContactNumber(contactNumber);
    }

    @Override
    public void setEmail(String email) {
        validationCheck.checkEmail(email);
        super.setEmail(email);
    }

    @Override
    public void setAddress(String address) {
        validationCheck.checkAddress(address);
        super.setAddress(address);
    }

    // getters
    public String getGender() { return this.gender; }
    public StaffPosition getPosition() { return this.position; }
    public LocalDate getDateJoined() { return this.dateJoined; }
    public double getSalary() { return this.salary; }
    public String getPassword() { return this.password; }
    public static CollectionReference getStaffCollection() { return Staff.staffCollection; }

    // database operations implementation
    public boolean addToDb() {
        try {

            System.out.println("Checking for existing staff with email: " + this.email);

            System.out.println(this.password);

            QuerySnapshot existingStaffSnapshot = staffCollection.whereEqualTo("email", this.email.trim()).get().get();
            if (!existingStaffSnapshot.isEmpty()) {
                System.out.println("Staff with this email already exists.");
                return false;  // Prevent duplicate entry
            }

            HashMap<String, Object> docValues = new HashMap<>();
            docValues.put("name", this.name);
            docValues.put("contactNumber", getContactNumber());
            docValues.put("address", getAddress());
            docValues.put("email", getEmail());
            docValues.put("gender", getGender());
            docValues.put("position", getPosition().toString());

            if (getDateJoined() != null) {
                docValues.put("dateJoined", Timestamp.ofTimeSecondsAndNanos(getDateJoined().atStartOfDay(ZoneId.systemDefault()).toEpochSecond(), 0));
            } else {
                docValues.put("dateJoined", Timestamp.now());  // Fallback to current timestamp if null
            }

            docValues.put("salary", getSalary());
            docValues.put("password", getPassword());
            //docValues.put("password", this.password());

            String prefix;
            switch (getPosition().getAccessLevel()) {
                case MANAGER:
                    prefix = "MNG";
                    break;
                case ADMIN:
                    prefix = "ADM";
                    break;
                case STAFF:
                default:
                    prefix = "STF";
                    break;
            }

            this.id = DbMethods.dbCreateNewId("Staff", prefix, 4);
            staffCollection.document(this.id).set(docValues).get();
            return true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
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

            // Retrieve and validate name
            String name = Optional.ofNullable((String) docData.get("name")).orElse("");
            fetchedStaff.setName(name);

            // Retrieve and validate contactNumber
            String contactNumber = Optional.ofNullable((String) docData.get("contactNumber")).orElse("");
            fetchedStaff.setContactNumber(contactNumber);

            // Retrieve and validate email
            String email = Optional.ofNullable((String) docData.get("email")).orElse("");
            fetchedStaff.setEmail(email);

            // Retrieve and validate address
            String address = Optional.ofNullable((String) docData.get("address")).orElse("");
            fetchedStaff.setAddress(address);

            // Retrieve and validate gender
            String genderStr = Optional.ofNullable((String) docData.get("gender")).orElse("");
            fetchedStaff.setGender(genderStr); // Ensure valid gender

            // Retrieve and validate position
            String positionStr = Optional.ofNullable((String) docData.get("position")).orElse("");
            fetchedStaff.setPosition(StaffPosition.fromString(positionStr)); // Convert position string to enum

            // Handle dateJoined (assume it's a Timestamp, convert to LocalDate)
            Timestamp timestamp = (Timestamp) docData.get("dateJoined");
            fetchedStaff.setDateJoined(timestamp != null ? timestamp.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : LocalDate.now());

            // Handle salary (convert from Object to double)
            fetchedStaff.setSalary(Double.parseDouble(Optional.ofNullable(docData.get("salary")).orElse("0.0").toString()));

            // Retrieve and validate password
//            String password = Optional.ofNullable((String) docData.get("password")).orElse("");
//            fetchedStaff.setPassword(password);
            fetchedStaff.password = Optional.ofNullable((String) docData.get("password")).orElse("");  // No need to hash again

            // cache and return
            FirebaseConnection.cacheNewDocument(staffCollection, staffCollection.document(id), fetchedStaff);
            return fetchedStaff;

        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public boolean updateToDb(String fieldName)  {
        try {
            Field field = this.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            staffCollection.document(this.id).update(fieldName, field.get(this)).get();
            return true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public boolean deleteFromDb()  {
        try {
            staffCollection.document(this.id).delete().get();
            FirebaseConnection.deleteCachedDocument(staffCollection, staffCollection.document(this.id));
            return true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    @Override
    public String toString(){
        return "Staff ID: " + getId()
                + "\nStaff Name: " + getName()
                + "\nStaff gender: " + getGender()
                + "\nStaff position: " + getPosition()
                + "\nStaff contact number: " + getContactNumber()
                + "\nStaff email: " + getEmail()
                + "\nStaff address: "+ getAddress()
                + "\nStaff salary: " + getSalary()
                + "\nStaff password: " + getPassword();
    }
}
