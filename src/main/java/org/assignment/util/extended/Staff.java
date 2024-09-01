package org.assignment.util.extended;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import org.assignment.util.FirebaseConnection;
import org.assignment.util.base.BaseStaff;
import org.assignment.util.enums.StaffPosition;
import org.assignment.util.enums.AccessLevel;
import org.assignment.util.Utility.DbMethods;

import java.time.LocalDate;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;


public class Staff extends BaseStaff {

    public Staff(){
        this(null, null, '\u0000', null, null, null, null, 0, null);
    }

    public Staff(String id, String name, char gender, StaffPosition position, String contactNumber, String email, LocalDate dateJoined, int salary, String address) {
        super(id, name, gender, position, contactNumber, email, dateJoined, salary, address);

        // Optional: Validation during initialization
        if (!isValid()) {
            throw new IllegalArgumentException("Invalid staff details.");
        }
    }

    // Convert Staff object to Firestore map
    public Map<String, Object> toFirestoreMap() {
        Map<String, Object> staffData = new HashMap<>();
        staffData.put("id", getId());
        staffData.put("name", getName());
        staffData.put("gender", String.valueOf(getGender())); // Convert char to string
        staffData.put("position", getPosition().name()); // Enum to string
        staffData.put("contactNumber", getContactNumber());
        staffData.put("email", getEmail());
        staffData.put("dateJoined", getDateJoined().toString()); // LocalDate to string
        staffData.put("salary", getSalary());
        staffData.put("address", getAddress());

        return staffData;
    }

    public StaffPosition getPosition() {
        return position;
    }

    public void setPosition(StaffPosition position) {
        this.position = position;
    }

    // Write staff data to Firestore
    public void writeToDb() {
        Map<String, Object> staffData = this.toFirestoreMap();
        String id = DbMethods.dbCreateNewId("item","ITEM",4);
        DbMethods.writeToDb(staffData, "staff", id);
    }

    // Modify staff data in Firestore
    public void modifyInDb(Map<String, Object> itemInfo) {
        DbMethods.modifyInDb(itemInfo, getId(), "staff");
    }

    // Delete staff data from Firestore
    public static void deleteInDb(String id) {
        DbMethods.deleteInDb(id, "staff");
    }

    // View staff data from Firestore
    public static void viewInDb(String id) {
        DbMethods.viewInDb(id, "staff");
    }

}

