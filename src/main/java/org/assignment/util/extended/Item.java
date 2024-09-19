package org.assignment.util.extended;

import com.google.cloud.firestore.*;
import org.assignment.util.IdUtils;
import org.assignment.util.FirebaseConnection;
import org.assignment.util.base.BaseItem;
import org.assignment.util.base.DatabaseOperations;

import java.lang.reflect.Field;
import java.util.*;

import static org.fusesource.jansi.Ansi.ansi;

public class Item extends BaseItem implements DatabaseOperations {
    private ItemCategory itemCategory;
    private Supplier supplier;
    private static final CollectionReference itemCollection = FirebaseConnection.getDb().collection("Item");

    public Item() {
        this("", 0.0,0,null,null);
    }

    public Item(String name, double unitPrice, int quantity, ItemCategory itemCategory, Supplier supplier) {
        super(name, unitPrice, quantity);
        this.itemCategory = itemCategory;
        this.supplier = supplier;
    }

    // setters
    public boolean setName(String name, boolean updateToDb) {
        setName(name);
        if (updateToDb) {return this.updateToDb("name");}
        return false;
    }

    public boolean setUnitPrice(double unitPrice, boolean updateToDb) {
        setUnitPrice(unitPrice);
        if (updateToDb) return this.updateToDb("unitPrice");
        return false;
    }

    public boolean setQuantity(int quantity, boolean updateToDb) {
        setQuantity(quantity);
        if (updateToDb) return this.updateToDb("quantity");
        return false;
    }

    public void setItemCategory(ItemCategory itemCategory) { this.itemCategory = itemCategory; }

    public boolean setItemCategory(ItemCategory itemCategory, boolean updateToDb) {
        this.itemCategory = itemCategory;
        if (updateToDb) return this.updateToDb("itemCategory");
        return false;
    }

    public void setSupplier(Supplier supplier) { this.supplier = supplier; }

    public boolean setSupplier(Supplier supplier, boolean updateToDb) {
        this.supplier = supplier;
        if (updateToDb) return this.updateToDb("supplier");
        return false;
    }

    // getters
    public ItemCategory getItemCategory() { return this.itemCategory; }
    public Supplier getSupplier() { return this.supplier; }
    public static CollectionReference getItemCollection() { return Item.itemCollection; }

    // database operations implementation
    public boolean addToDb() {
        try {
            HashMap<String, Object> docValues = new HashMap<>();
            docValues.put("name", getName());
            docValues.put("unitPrice", getUnitPrice());
            docValues.put("quantity", getQuantity());
            docValues.put("itemCategory", ItemCategory.getItemCategoryCollection().document(this.itemCategory.getId()));
            docValues.put("supplier", Supplier.getSupplierCollection().document(this.supplier.getId()));

            setId(IdUtils.dbCreateNewId("Item","ITEM",4));
            itemCollection.document(getId()).set(docValues).get();
            return true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public static Item getFromDb(String id) {
        try {
            // get from cache if available
            Item cachedItemObject = (Item) FirebaseConnection.getCachedDocument(itemCollection, itemCollection.document(id));
            if (cachedItemObject != null) return cachedItemObject;

            // fetch from db
            DocumentSnapshot doc = itemCollection.document(id).get().get();
            if (!doc.exists()) return null;
            Map<String, Object> docData = doc.getData();
            if (docData == null) return null;

            // create new instance and assign values
            Item fetchedItem = new Item();
            fetchedItem.setId(doc.getId());
            fetchedItem.setName(Optional.ofNullable(docData.get("name")).orElse("").toString());
            fetchedItem.setUnitPrice(Double.parseDouble(Optional.ofNullable(docData.get("unitPrice")).orElse(0.0).toString()));
            fetchedItem.setQuantity(Integer.parseInt(Optional.ofNullable(docData.get("quantity")).orElse(0).toString()));
            fetchedItem.setItemCategory(ItemCategory.getFromDb(((DocumentReference) docData.get("itemCategory")).getId()));
            fetchedItem.setSupplier(Supplier.getFromDb(((DocumentReference) docData.get("supplier")).getId()));

            // cache and return
            FirebaseConnection.cacheNewDocument(itemCollection, itemCollection.document(id), fetchedItem);
            return fetchedItem;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    // FIXME: updateToDb not working
    public boolean updateToDb(String fieldName) {
        try {
            Field field = this.getClass().getSuperclass().getDeclaredField(fieldName);
            field.setAccessible(true);

            itemCollection.document(this.getId()).update(fieldName, field.get(this)).get();
            return true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public boolean deleteFromDb() {
        try {
            itemCollection.document(getId()).delete().get();
            FirebaseConnection.deleteCachedDocument(itemCollection, itemCollection.document(getId()));
            return true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public static List<Item> getAllItem() {
        try {
            List<QueryDocumentSnapshot> documents = Item.itemCollection.get().get().getDocuments();

            List<Item> items = new ArrayList<>();
            for (QueryDocumentSnapshot doc : documents) {
                items.add(Item.getFromDb(doc.getId()));
            }

            return items;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    @Override
    public String toString(){
        String idPlaceHolder;
        if (getId() == null) idPlaceHolder = ansi().fgYellow().a("[Unassigned]").reset().toString(); else idPlaceHolder = ansi().fgYellow().a(getId()).reset().toString();

        return "-------------------------------------------" + "\n" +
                "ITEM ID            : " + idPlaceHolder + "\n" +
                "ITEM Name          : " + ansi().fgBlue().a(getName()).reset().toString() + "\n" +
                "ITEM Unit Price    : " + ansi().fgBrightMagenta().a(getUnitPrice()) + "\n" +
                "ITEM Quantity      : " + ansi().fgBrightMagenta().a(getQuantity()).reset() + "\n\n" +
                "ITEM Supplier Info\n" + getSupplier() + "\n\n" +
                "ITEM ItemCategory Info\n" + getItemCategory() + "\n" +
                ansi().reset();
    }
}
