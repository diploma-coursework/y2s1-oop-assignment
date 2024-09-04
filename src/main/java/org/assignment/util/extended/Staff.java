package org.assignment.util.extended;

import org.assignment.util.base.BaseStaff;
import org.assignment.util.enums.StaffPosition;
import org.assignment.util.enums.AccessLevel;
import org.assignment.util.Utility.DbMethods;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;


public class Staff extends BaseStaff {

    private StaffPosition position;

    public Staff(){
        this(null, null, null, null, null, null, 0, null, null);
    }

    public Staff(String name, String gender, StaffPosition position, String contactNumber, String email, LocalDate dateJoined, int salary, String address, String password) {
        super(name, gender, position, contactNumber, email, dateJoined, salary, address, password);
        // Optional: Validation during initialization


        if (!isValid()) {
            throw new IllegalArgumentException("Invalid staff details.");
        }

        if (position == null || dateJoined == null) {
            throw new IllegalArgumentException("Position and dateJoined cannot be null.");
        }

        this.position = position;
        this.dateJoined = dateJoined;
    }

    //accessor
    public StaffPosition getPosition() {
        return position;
    }

    //mutator
    public void setPosition(StaffPosition position) {
        this.position = position;
    }

    //get access level based on position of staff
    public AccessLevel getAccessLevel() {
        return position.getAccessLevel();
    }


    // Convert Staff object to Firestore map
    public Map<String, Object> toFirestoreMap() {
        Map<String, Object> staffData = new HashMap<>();
        staffData.put("name", getName());
        staffData.put("gender", String.valueOf(getGender())); // Convert char to string
        staffData.put("position", getPosition().name()); // Enum to string
        staffData.put("contactNumber", getContactNumber());
        staffData.put("email", getEmail());
        staffData.put("dateJoined", getDateJoined().toString()); // LocalDate to string
        staffData.put("salary", getSalary());
        staffData.put("address", getAddress());
        staffData.put("password", getPassword());
        return staffData;
    }

    public void performTask() {
        switch (getAccessLevel()) {
            case STAFF:
                // Actions for STAFF level
                System.out.println("Performing staff-level actions.");
                break;
            case MANAGER:
                // Actions for MANAGER level
                System.out.println("Performing manager-level actions.");
                break;
            case ADMIN:
                // Actions for ADMIN level
                System.out.println("Performing admin-level actions.");
                break;
        }
    }

    // Write staff data to Firestore
    public void writeToDb() {
        try {
            setId(DbMethods.dbCreateNewId("Staff", "STF", 4));
            Map<String, Object> staffData = this.toFirestoreMap();
            DbMethods.writeToDb(staffData, "Staff", getId());
        } catch (Exception e) {
            System.err.println("Error writing staff data to database: " + e.getMessage());
        }
    }

    // Modify staff data in Firestore
    public <T>void modifyInDb(String id, int choice, T newValue) {
        try {
            DbMethods.modifyInDb(getId(), "Staff",editChoice(choice), newValue);
        } catch (Exception e) {
            System.err.println("Error modifying staff data in database: " + e.getMessage());
        }
    }

    // Delete staff data from Firestore
    public static void deleteInDb(String id) {
        try {
            DbMethods.deleteInDb(id, "Staff");
        } catch (Exception e) {
            System.err.println("Error deleting staff data from database: " + e.getMessage());
        }
    }

    // View staff data from Firestore
    public static void viewInDb(String id) {
        try {
            DbMethods.viewInDb(id, "Staff");
        } catch (Exception e) {
            System.err.println("Error viewing staff data from database: " + e.getMessage());
        }
    }

    public String editChoice(int choice) {
        switch (choice) {
            case 1:
                return "name";
            case 2:
                return "gender";
            // continue onwards
            default:
                return null;
        }
    }
}

