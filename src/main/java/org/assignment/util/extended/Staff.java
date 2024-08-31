package org.assignment.util.extended;


import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import org.assignment.util.FirebaseConnection;
import org.assignment.util.base.BaseStaff;
import org.assignment.util.enums.StaffPosition;

import java.time.LocalDate;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;


public class Staff extends BaseStaff {

    public Staff(String id, String name, char gender, StaffPosition position, String contactNumber, String email, LocalDate dateJoined, int salary, String address) {
        super(id, name, gender, position, contactNumber, email, dateJoined, salary, address);
    }

}
