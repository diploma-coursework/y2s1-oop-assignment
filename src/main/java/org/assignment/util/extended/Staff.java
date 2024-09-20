package org.assignment.util.extended;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.Timestamp;

import org.assignment.util.IdUtils;
import org.assignment.util.FirebaseConnection;
import org.assignment.util.base.DatabaseOperations;
import org.assignment.util.base.Person;
import org.assignment.util.enums.StaffPosition;
import org.assignment.util.ValidationCheck;
import org.mindrot.jbcrypt.BCrypt;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.ZoneId;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static org.fusesource.jansi.Ansi.ansi;


public class Staff extends Person implements DatabaseOperations {
    private String gender;
    private StaffPosition position;
    private LocalDate dateJoined;
    private double salary;
    private String password;
    private static final CollectionReference staffCollection = FirebaseConnection.getDb().collection("Staff");
    private static final Logger logger = LogManager.getLogger(Staff.class);

    //constructor
    public Staff() {
        //default
    }

    //parameterized
    public Staff(String name, String contactNumber, String email, String address, String gender, StaffPosition position, LocalDate dateJoined, double salary, String password) {
        super(name, contactNumber, email, address);
        setGender(gender);
        setPosition(position);
        setDateJoined(dateJoined);
        setSalary(salary);
        setPassword(password);
    }

    // setters
    public boolean setGender(String gender, boolean updateToDb) {
        try {
            this.gender = ValidationCheck.checkGender(gender.charAt(0));
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
            if (ValidationCheck.isValidPosition(positionName)) {
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
        ValidationCheck.checkDateJoined(dateJoined);
        this.dateJoined = dateJoined;
    }
    public boolean setSalary(double salary, boolean updateToDb) {
        try {
            this.salary = ValidationCheck.checkSalary((int) salary);
            if (updateToDb) {
                return this.updateToDb("salary");
            }
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    public boolean setPassword(String password, boolean updateToDb, boolean signup) {
        try {
            String validatedPassword = ValidationCheck.checkPassword(password);

            if (updateToDb) {
                this.password = BCrypt.hashpw(validatedPassword, BCrypt.gensalt());
                return this.updateToDb("password");
            }

            // If this is a signup process (but not updating to DB yet)
            if (signup) {
                this.password = BCrypt.hashpw(validatedPassword, BCrypt.gensalt());
                return true; // Assuming signup is successful
            }

            // If just setting the password without DB update or signup
            this.password = validatedPassword;
            return true;

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    public boolean setName(String name, boolean updateToDb) {
        try{
            String validatedName = ValidationCheck.checkAndFormatName(name);
            super.setName(validatedName);
            if (updateToDb) {
                return this.updateToDb("name");
            }
            return true;
        } catch (IllegalArgumentException e) {
            // Handle exception or provide user feedback
            System.out.println(ansi().bgBrightRed().a(e.getMessage()).reset());
            // You might want to rethrow or handle the error according to your application's needs
            return false;
        }
    }
    public boolean setContactNumber(String contactNumber, boolean updateToDb) {
        try {
            ValidationCheck.checkContactNumber(contactNumber);
            super.setContactNumber(contactNumber);
            if (updateToDb) {
                return this.updateToDb("contactNumber");
            }
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println(ansi().bgBrightRed().a(e.getMessage()).reset());
            return false;
        }
    }
    public boolean setEmail(String email, boolean updateToDb) {
        try {
            ValidationCheck.checkEmail(email);
            super.setEmail(email);
            if (updateToDb) {
                return this.updateToDb("email");
            }
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println(ansi().bgBrightRed().a(e.getMessage()).reset());
            return false;
        }
    }
    public boolean setAddress(String address, boolean updateToDb) {
        try {
            ValidationCheck.checkAddress(address);
            super.setAddress(address);
            if (updateToDb) {
                return this.updateToDb("address");
            }
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println(ansi().bgBrightRed().a(e.getMessage()).reset());
            return false;
        }
    }

    //overloaded setter methods gg
    public void setPassword(String password) {
        setPassword(password, false, false); // Default values for `updateToDb` and `isSignup`
    }
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

    // database operations implementation
    public boolean addToDb() {
        try {

            System.out.println("\nChecking for existing staff with email: " + this.email);


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

            String prefix = switch (getPosition()) {
                case INVENTORY_CLERK, STOCK_CONTROLLER, SUPPLIER, WAREHOUSE_MANAGER, INVENTORY_SUPERVISOR, ADMIN -> "STF";
            };

            this.id = IdUtils.dbCreateNewId("Staff", prefix, 4);
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

            String name = Optional.ofNullable((String) docData.get("name")).orElse("");
            String contactNumber = Optional.ofNullable((String) docData.get("contactNumber")).orElse("");
            String email = Optional.ofNullable((String) docData.get("email")).orElse("");
            String address = Optional.ofNullable((String) docData.get("address")).orElse("");
            String genderStr = Optional.ofNullable((String) docData.get("gender")).orElse("");
            StaffPosition position = StaffPosition.fromString(Optional.ofNullable((String) docData.get("position")).orElse(""));
            LocalDate dateJoined = Optional.ofNullable((Timestamp) docData.get("dateJoined"))
                    .map(ts -> ts.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                    .orElse(LocalDate.now());
            double salary = Double.parseDouble(Optional.ofNullable(docData.get("salary")).orElse("0.0").toString());
            String password = Optional.ofNullable((String) docData.get("password")).orElse("");

            Staff fetchedStaff = new Staff(name, contactNumber, email, address, genderStr, position, dateJoined, salary, password);
            fetchedStaff.setId(doc.getId());

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
            System.out.println("\nUpdating field " + fieldName + "...");

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

    // to string
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
