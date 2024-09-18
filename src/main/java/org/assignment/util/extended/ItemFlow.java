package org.assignment.util.extended;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import org.assignment.util.FirebaseConnection;
import org.assignment.util.base.DatabaseOperations;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.fusesource.jansi.Ansi.ansi;

public class ItemFlow implements DatabaseOperations {
    private LocalDateTime updateTimeId;
    private Supplier supplier;
    private Item item;
    private int delta;
    private static final DateTimeFormatter updateTimeIdFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    private static final CollectionReference itemFlowCollection = FirebaseConnection.getDb().collection("ItemFlow");

    public ItemFlow() {
        this(LocalDateTime.now(), null, null, 0);
    }

    public ItemFlow(LocalDateTime updateTimeId, Supplier supplier, Item item, int delta) {
        this.updateTimeId = updateTimeId;
        this.supplier = supplier;
        this.item = item;
        this.delta = delta;
    }

    // setters
    public void setUpdateTimeId(LocalDateTime updateTimeId) { this.updateTimeId = updateTimeId; }

    public void setSupplier(Supplier supplier) { this.supplier = supplier; }

    public boolean setSupplier(Supplier supplier, boolean updateToDb) {
        this.supplier = supplier;
        if (updateToDb) return this.updateToDb("supplier");
        return false;
    }

    public void setItem(Item item) { this.item = item; }

    public boolean setItem(Item item, boolean updateToDb) {
        this.item = item;
        if (updateToDb) return this.updateToDb("item");
        return false;
    }

    public void setDelta(int delta) { this.delta = delta; }

    public boolean setDelta(int delta, boolean updateToDb) {
        this.delta = delta;
        if (updateToDb) return this.updateToDb("delta");
        return false;
    }

    // getters
    public LocalDateTime getUpdateTimeId() { return this.updateTimeId;}
    public String getUpdateTimeIdAsString() { return this.updateTimeId.format(updateTimeIdFormatter); }
    public Supplier getSupplier(){ return this.supplier; }
    public Item getItem() { return this.item; }
    public int getDelta() { return this.delta; }
    public static CollectionReference getItemFlowCollection() { return ItemFlow.itemFlowCollection; }

    // database operations implementation
    public boolean addToDb() {
        try {
            HashMap<String, Object> docValues = new HashMap<>();
            docValues.put("supplier", Supplier.getSupplierCollection().document(this.supplier.getId()));
            docValues.put("item", Item.getItemCollection().document(this.item.getId()));
            docValues.put("delta", this.delta);

            this.updateTimeId = LocalDateTime.now();
            itemFlowCollection.document(this.getUpdateTimeIdAsString()).set(docValues).get();
            return true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public static ItemFlow getFromDb(String id) {
        try {
            // get from cache if available
            ItemFlow cachedItemFlowObject = (ItemFlow) FirebaseConnection.getCachedDocument(itemFlowCollection, itemFlowCollection.document(id));
            if (cachedItemFlowObject != null) return cachedItemFlowObject;

            // fetch from db
            DocumentSnapshot doc = itemFlowCollection.document(id).get().get();
            if (!doc.exists()) return null;
            Map<String, Object> docData = doc.getData();
            if (docData == null) return null;

            // create new instance and assign values
            ItemFlow fetchedItemFlow = new ItemFlow();
            fetchedItemFlow.setUpdateTimeId(LocalDateTime.parse(doc.getId(), ItemFlow.updateTimeIdFormatter));
            fetchedItemFlow.setSupplier(Supplier.getFromDb(((DocumentReference)docData.get("supplier")).getId()));
            fetchedItemFlow.setItem(Item.getFromDb(((DocumentReference)docData.get("item")).getId()));
            fetchedItemFlow.setDelta(Integer.parseInt(Optional.ofNullable(docData.get("delta")).orElse(0).toString()));

            // cache and return
            FirebaseConnection.cacheNewDocument(itemFlowCollection, itemFlowCollection.document(id), fetchedItemFlow);
            return fetchedItemFlow;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public boolean updateToDb(String fieldName) {
        try {
            Field field = this.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);

            itemFlowCollection.document(this.getUpdateTimeIdAsString()).update(fieldName, field.get(this)).get();
            return true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public boolean deleteFromDb() {
        try {
            itemFlowCollection.document(this.getUpdateTimeIdAsString()).delete().get();
            FirebaseConnection.deleteCachedDocument(itemFlowCollection, itemFlowCollection.document(this.getUpdateTimeIdAsString()));
            return true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public static List<ItemFlow> getAllItemFlow() {
        try {
            List<QueryDocumentSnapshot> documents = ItemFlow.itemFlowCollection.get().get().getDocuments();

            List<ItemFlow> itemFlows = new ArrayList<>();
            for (QueryDocumentSnapshot doc : documents) {
                itemFlows.add(ItemFlow.getFromDb(doc.getId()));
            }

            return itemFlows;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }


    // TODO: someone do this pls im lazy
//    public String toString() {
//        StringBuilder returnString = new StringBuilder();
//
//        returnString
//                .append("Id         : ").append(ansi().fgYellow().a(this.getId()).reset().toString()).append("\n")
//                .append("Name       : ").append(ansi().fgBlue().a(this.getName()).reset().toString()).append("\n")
//                .append("Description: ").append(ansi().fgBrightMagenta());
//
//        // do text wrap for description with alignment
//        String[] tokenizedDescription = this.getDescription().split(" ");
//        int currentLineLength = 0;
//
//        for (String word : tokenizedDescription) {
//            if (currentLineLength > 50) {
//                returnString.append("\n             ");
//                currentLineLength = 0;
//            }
//
//            returnString.append(word).append(" ");
//            currentLineLength += word.length() + 1 ;
//        }
//        returnString.append(ansi().reset()).append("\n");
//        return returnString.toString();
//    }
}
