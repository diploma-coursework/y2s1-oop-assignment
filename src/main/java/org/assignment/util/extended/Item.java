package org.assignment.util.extended;

import org.assignment.util.base.BaseItem;

// IDK if needed
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

// imports from example
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import org.assignment.util.FirebaseConnection;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class Item extends BaseItem {

    public void dbCreateNewItem(){
        Firestore db = FirebaseConnection.getDb();

        try {
            Map<String, Object> itemInfo = new HashMap<>();
            itemInfo.put("name", "ExampleItem1");
            itemInfo.put("unitPrice",3.0);
            itemInfo.put("quantity", 0);

            ApiFuture<WriteResult> future = db.collection("item").document("id").set(itemInfo);
            WriteResult result = future.get();

            System.out.println(result.getUpdateTime());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
