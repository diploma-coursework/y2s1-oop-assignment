package org.assignment.util.extended;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import org.assignment.util.base.BaseItem;
import org.assignment.util.base.DatabaseOperations;

import java.util.HashMap;

public class Item extends BaseItem implements DatabaseOperations {
    private ItemCategory itemCategory;
    private Supplier supplier;

    public Item() {
        this("", "", 0.0,0,null,null);
    }

    public Item(String id, String name, double unitPrice, int quantity, ItemCategory itemCategory, Supplier supplier) {
        super(name, unitPrice, quantity);
        this.itemCategory = itemCategory;
        this.supplier = supplier;
    }

    // setters
    public void setItemCategory(ItemCategory itemCategory){ this.itemCategory = itemCategory; }
    public void setSupplier(Supplier supplier){ this.supplier = supplier; }

    // getters
    public ItemCategory getItemCategory(){ return this.itemCategory; }
    public Supplier getSupplier(){ return this.supplier; }


    public static Item fromDocumentSnapshot(DocumentSnapshot docSnapshot) {
        Item item = new Item();
        item.setId(docSnapshot.getId());
        item.setName(docSnapshot.get("name").toString());
        item.setUnitPrice((double) docSnapshot.get("unitPrice"));
        item.setQuantity(((Long) docSnapshot.get("quantity")).intValue());
        try {
            item.setItemCategory(ItemCategory.fromDocumentSnapshot(((DocumentReference) docSnapshot.get("ItemCategory")).get().get()));
        } catch (Exception e) {
            System.out.println("error here abc");
        }
        return item;
    }

    // Database Operation Methods
    public boolean addToDb() {
        // add self to db
        return true;
    }

    public boolean getFromDb(String id) {
        // fetch using id and store in self
        return true;
    }

    public boolean updateToDb() {
        // update self to db
        return true;
    }

    public boolean deleteFromDb(String id) {
        // delete from db where id is matching
        return true;
    }
}
