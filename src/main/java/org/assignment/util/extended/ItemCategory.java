package org.assignment.util.extended;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import org.assignment.util.FirebaseConnection;
import org.assignment.util.Utility.DbMethods;
import org.assignment.util.base.BaseItemCategory;

import java.util.*;
import java.util.concurrent.ExecutionException;

import static org.assignment.util.Utility.DbMethods.db;

public class ItemCategory extends BaseItemCategory {
    private List<Item> item;

    public ItemCategory() {
        this.item = new ArrayList<>();  // Initialize to avoid null pointer issues
    }

    public ItemCategory(String name, String description) {
        super(name, description);
        System.out.println("Reach 1");
    }

    public ItemCategory(String name, String description, Item... item) {
        super(name, description);
        System.out.println("Reach 1");
        this.item = new ArrayList<>(Arrays.asList(item));
    }

    // Getter
    public List<Item> getItems() {
        return this.item;
    }

    public int getSize(){
        return getItems().size();
    }

    // Setter
    public void setItems(List<Item> items) {
        this.item = new ArrayList<>(items);
    }

    public void setItems(Item item) {
        if (this.item == null) {
            this.item = new ArrayList<>();
        }
        this.item.add(item);
    }

    // add an item to existing list
    public void addItem(Item item) {
        this.item.add(item);
    }

    //-----
    public boolean secureInstance(ItemCategory itemCategory){
        if(ItemCategoryMenu.confirmSave(itemCategory)) {
            dbCreateNewCategory();
            return true;
        }
        return false;
    }

    // create New Item
    public void dbCreateNewCategory(){
        setId(DbMethods.dbCreateNewId("ItemCategory","CAT",4));

        Map<String, Object> itemInfo = new HashMap<>();
        itemInfo.put("name",getId());
        itemInfo.put("description",getDescription());
        if(getItems() != null){
            List<DocumentReference> itemRefs = createArray(getItems());
            itemInfo.put("Item", itemRefs);
        }
        System.out.println("Getting ID here");
        DbMethods.writeToDb(itemInfo, "ItemCategory", getId());
    }

    public List<DocumentReference> createArray(List<Item> items) {
        List<DocumentReference> itemRefs = new ArrayList<>();

        for (Item item : items) {
            if (item.getId() != null && !item.getId().isEmpty()) {
                System.out.println("Each item id: " + item.getId());
                DocumentReference docRef = db.collection("Item").document(item.getId());
                itemRefs.add(docRef);
            } else {
                System.out.println("Item ID is null or empty.");
                throw new IllegalArgumentException("Item ID is null or empty.");
            }
        }

        return itemRefs;
    }

    // Modify
    public <T> void modifyInDb(String id, int choice, int index, T newValue) throws ExecutionException, InterruptedException {
        DocumentReference docRef = db.collection("yourCollection").document(id);
        String field = editChoice(choice, DbMethods.deserialized(id, ItemCategory.class));

        if (choice >= 3 && choice <= Objects.requireNonNull(DbMethods.deserialized(id, ItemCategory.class)).getSize() + 2) {
            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot document = future.get();

            // Retrieve the array field
            List<DocumentReference> items = (List<DocumentReference>) document.get("Item");

            if (index >= 0 && index < Objects.requireNonNull(items).size()) {
                items.set(index, (DocumentReference) newValue); // Update the specific index with DocumentReference

                // Prepare the update map
                Map<String, Object> updateMap = new HashMap<>();
                updateMap.put("Item", items);

                // Update the document with the modified array
                DbMethods.modifyInDb(id, "ItemCategory", "Item", updateMap);
                return; // Exit after successful update
            } else {
                System.err.println("Index out of bounds");
            }
        }

        // Handle non-array updates
        DbMethods.modifyInDb(id, "ItemCategory", field, newValue);
    }

    // Delete
    public void deleteInDb(String id){
        DbMethods.deleteInDb(id, "ItemCategory");
    }

    // View
    public void viewInDb(String id){
        DbMethods.viewInDb(id,"ItemCategory");
    }

    public static String editChoice(int choice,ItemCategory itemCategory) {
        if (choice >= 1 && choice <=2){
            return "name";
        }
        else if (choice >= 3 && choice <= itemCategory.getSize() + 2) {
            return "Item";
        }
        return null;
    }

}
