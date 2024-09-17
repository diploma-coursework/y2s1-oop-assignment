package org.assignment.util.extended;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QueryDocumentSnapshot;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Staff extends Person implements DatabaseOperations {
    protected String gender;
    protected StaffPosition position;
    protected LocalDate dateJoined;
    protected double salary;
    protected String password;
    private static final CollectionReference staffCollection = FirebaseConnection.getDb().collection("Staff");
    private static final Logger logger = LogManager.getLogger(Staff.class);

    //constructor
    public Staff() {
        //default
    }

    // setters
    public boolean setGender(String gender, boolean updateToDb) {
        try {
            this.gender = validationCheck.checkGender(gender.charAt(0));
            if (updateToDb) {
                return this.updateToDb("gender");
            }
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    public boolean setPosition(StaffPosition position, boolean updateToDb) {
        try {
            String positionName = position.name();
            if (validationCheck.isValidPosition(positionName)) {
                this.position = position;
                if (updateToDb) {
                    return this.updateToDb("position");
                }
                return true;
            } else {
                throw new IllegalArgumentException("Invalid position: " + positionName);
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    public void setDateJoined(LocalDate dateJoined) {
        validationCheck.checkDateJoined(dateJoined);
        this.dateJoined = dateJoined;
    }

    public boolean setSalary(double salary, boolean updateToDb) {
        try {
            this.salary = validationCheck.checkSalary((int) salary);
            if (updateToDb) {
                return this.updateToDb("salary");
            }
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean setPassword(String password, boolean updateToDb) {
        try {
            String validatedPassword = validationCheck.checkPassword(password);
            this.password = BCrypt.hashpw(validatedPassword, BCrypt.gensalt());
            if (updateToDb) {
                return this.updateToDb("password");
            }
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean setName(String name, boolean updateToDb) {
        try{
            String formattedName = validationCheck.checkAndFormatName(name);
            super.setName(formattedName);

            if (updateToDb) {
                return this.updateToDb("name");
            }
            return true;

        } catch (IllegalArgumentException e) {
            // Handle exception or provide user feedback
            System.out.println(e.getMessage());
            // You might want to rethrow or handle the error according to your application's needs
            return false;
        }
    }

    public boolean setContactNumber(String contactNumber, boolean updateToDb) {
        try {
            validationCheck.checkContactNumber(contactNumber);
            super.setContactNumber(contactNumber);
            if (updateToDb) {
                return this.updateToDb("contactNumber");
            }
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean setEmail(String email, boolean updateToDb) {
        try {
            validationCheck.checkEmail(email);
            super.setEmail(email);
            if (updateToDb) {
                return this.updateToDb("email");
            }
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean setAddress(String address, boolean updateToDb) {
        try {
            validationCheck.checkAddress(address);
            super.setAddress(address);
            if (updateToDb) {
                return this.updateToDb("address");
            }
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    // Overloaded methods without updateToDb
    public void setGender(String gender) {
        setGender(gender, false);
    }
    public void setPosition(StaffPosition position) {
        setPosition(position, false);
    }
    public void setSalary(double salary) {
        setSalary(salary, false);
    }

    // getters
    public String getGender() { return this.gender; }
    public StaffPosition getPosition() { return this.position; }
    public LocalDate getDateJoined() { return this.dateJoined; }
    public double getSalary() { return this.salary; }
    public String getPassword() { return this.password; }
    //public static CollectionReference getStaffCollection() { return Staff.staffCollection; }

    // database operations implementation
    public boolean addToDb() {
        try {

            System.out.println("Checking for existing staff with email: " + this.email);


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

            String prefix = switch (getPosition().getAccessLevel()) {
                case MANAGER -> "MNG";
                case ADMIN -> "ADM";
                case STAFF -> "STF";  // 'DEFAULT' is not necessary here but included for clarity
            };

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
            fetchedStaff.password = Optional.ofNullable((String) docData.get("password")).orElse("");  // No need to hash again

            // cache and return
            FirebaseConnection.cacheNewDocument(staffCollection, staffCollection.document(id), fetchedStaff);
            return fetchedStaff;

        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public static List<Staff> getAllStaffInfo() {
        List<Staff> staffList = new ArrayList<>();
        try {
            // Retrieve all documents from the staff collection
            List<QueryDocumentSnapshot> documents = staffCollection.get().get().getDocuments();

            for (QueryDocumentSnapshot doc : documents) {
                // Fetch each staff record using the document ID
                Staff staff = getFromDb(doc.getId());
                if (staff != null) {
                    staffList.add(staff);
                }
            }
        } catch (Exception e) {
            System.err.println("Error fetching all staff records: " + e.getMessage());
        }
        return staffList;
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

    private Field findField(String fieldName) throws NoSuchFieldException {
        Class<?> currentClass = this.getClass();
        while (currentClass != null) {
            try {
                return currentClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                currentClass = currentClass.getSuperclass();
            }
        }
        throw new NoSuchFieldException("Field " + fieldName + " not found in any class.");
    }

    public boolean updateToDb(String fieldName) {
        try {
            System.out.println("Updating field " + fieldName + "...");

            Field field = findField(fieldName);
            field.setAccessible(true);
            Object fieldValue = field.get(this);

            staffCollection.document(this.id).update(fieldName, fieldValue).get();
            return true;

        } catch (NoSuchFieldException e) {
            logger.error("Field not found: {}", fieldName, e); //If I use print trace it will give out warnings
            return false;
        } catch (IllegalAccessException e) {
            logger.error("Illegal access to field: {}", fieldName, e);
            return false;
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Error updating Firebase document: {}", e.getMessage(), e);
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
