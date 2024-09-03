package org.assignment.util.extended;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import org.assignment.util.FirebaseConnection;
import org.assignment.util.Utility.DbMethods;
import org.assignment.util.base.BaseItemCategory;

import java.util.*;

public class ItemCategory extends BaseItemCategory {
    private List<Item> items;

    public ItemCategory(){
        this(null,null, (Item) null);
    }

    public ItemCategory(String name, String description, Item... items) {
        super(name, description);
        this.items = new ArrayList<>(Arrays.asList(items));
    }

    // Getter
    public List<Item> getItems() {
        return Collections.unmodifiableList(this.items);
    }

    // Setter
    public void setItems(List<Item> items) {
        this.items = new ArrayList<>(items);
    }

    // add an item to existing list
    public void addItem(Item item) {
        this.items.add(item);
    }

    //-----

    public boolean secureInstance(ItemCategory itemCategory){
        if(ItemCategoryMenu.cofirmSave(itemCategory)) {
            dbCreateNewCategory(itemCategory.getItems());
            return true;
        }
        return false;
    }

    public void dbCreateNewCategory(){
        try {
            dbCreateNewCategory(null);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    // create New Item
    public void dbCreateNewCategory(List<Item> items){
        Firestore db = FirebaseConnection.getDb();
        setId(DbMethods.dbCreateNewId("item","ITEM",4));

        DbMethods.writeToDb(createMap(items), "itemCategory", getId());
    }

    public Map<String, Object> createMap(List<Item> items) {
        Firestore db = FirebaseConnection.getDb();
        Map<String, Object> itemInfo = new HashMap<>();

        for (Item item : items) {
            DocumentReference docRef = db.collection("item").document(item.getId());
            itemInfo.put(item.getId(), docRef);
        }

        return itemInfo;
    }

    // Modify
    // need change
    public <T> void editInDb(String id, int choice, T newValue){
        DbMethods.editInDb(id, "itemCategory", choice, newValue);
    }

    // Delete
    public void deleteInDb(String id){
        DbMethods.deleteInDb(id, "itemCategory");
    }

    // View
    public void viewInDb(String id){
        DbMethods.viewInDb(id,"itemCategory");
    }
}
