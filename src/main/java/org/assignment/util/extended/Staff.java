package org.assignment.util.extended;


import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import org.assignment.util.FirebaseConnection;
import org.assignment.util.base.BaseStaff;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;


public class Staff extends BaseStaff {
    // temporary
    public static void dbCreateNewStaff() {
        Firestore db = FirebaseConnection.getDb();

        try {
            Map<String, Object> staffInfo = new HashMap<>();
            staffInfo.put("name", "aaa aaaa");
            staffInfo.put("gender", "A");
            staffInfo.put("dateJoined", Timestamp.from(Instant.now()));

            ApiFuture<WriteResult> future = db.collection("staff").document("iddd").set(staffInfo);
            WriteResult result = future.get();

            System.out.println(result.getUpdateTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // temporary
    public static void dbGetStaffById(String staffId) {
        Firestore db = FirebaseConnection.getDb();

        try {
            ApiFuture<DocumentSnapshot> future = db.collection("staff").document("iddd").get();
            DocumentSnapshot document = future.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
