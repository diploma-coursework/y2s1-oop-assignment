package org.assignment.util.extended;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import org.assignment.util.DbMethods;
import org.assignment.util.FirebaseConnection;
import org.assignment.util.base.DatabaseOperations;
import org.assignment.util.base.Person;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Supplier extends Person implements DatabaseOperations {
    protected LocalDate contractStartDate;
    protected LocalDate contractEndDate;
    protected final int contractYear = 2;
    private static final CollectionReference supplierCollection = FirebaseConnection.getDb().collection("Supplier");

    // Constructors
    public Supplier() {
        this("", "", "", "", null);
    }

    public Supplier(String name, String contactNumber, String address, String email, LocalDate contractStartDate) {
        super(name, contactNumber, email, address);
        this.contractStartDate = contractStartDate;
        extendContract();
    }

    // Getters
    public LocalDate getContractStartDate() { return contractStartDate; }
    public LocalDate getContractEndDate()   { return contractEndDate;   }
    public int getContractYear()            { return contractYear;      }
    public ArrayList<Item> getSupplyingItems() { return new ArrayList<>(); }
    public static CollectionReference getSupplierCollection() { return Supplier.supplierCollection; }

    // Setter
    public void setContractStartDate(LocalDate contractStartDate) {
        this.contractStartDate = contractStartDate;
    }
    public void setContractEndDate(LocalDate contractEndDate) {
        this.contractEndDate = this.contractStartDate.plusYears(contractYear);
    }

    public void clear(){
        super.setId(null);
        super.setAddress(null);
        super.setContactNumber(null);
        super.setEmail(null);
    }

    // database operations implementation
    public boolean addToDb() {
        // add self to db
        try {
            HashMap<String, Object> docValues = new HashMap<>();
            docValues.put("name", this.name);
            docValues.put("contactNumber", getContactNumber());
            docValues.put("address", getAddress());
            docValues.put("email", getEmail());
            // TODO: it needs to be stored as either String or Timestamp in db, below need change
            docValues.put("contractStartDate", getContractStartDate().toString());
            docValues.put("contractedDate", getContractEndDate().toString());
            docValues.put("contractYear", getContractYear());

            this.id = DbMethods.dbCreateNewId("Supplier","SUP",4);
            supplierCollection.document(this.id).set(docValues).get();
            return true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public static Supplier getFromDb(String id) {
        try {
            // get from cache if available
            Supplier cachedSupplierObject = (Supplier) FirebaseConnection.getCachedDocument(supplierCollection, supplierCollection.document(id));
            if (cachedSupplierObject != null) return cachedSupplierObject;

            // fetch from db
            DocumentSnapshot doc = supplierCollection.document(id).get().get();
            if (!doc.exists()) return null;
            Map<String, Object> docData = doc.getData();
            if (docData == null) return null;

            // create new instance and assign values
            Supplier fetchedSupplier = new Supplier();
            fetchedSupplier.setId(doc.getId());
            fetchedSupplier.setName(Optional.ofNullable(docData.get("name").toString()).orElse(""));
            fetchedSupplier.setContactNumber(Optional.ofNullable(docData.get("contactNumber").toString()).orElse(""));
            fetchedSupplier.setEmail(Optional.ofNullable(docData.get("email").toString()).orElse(""));
            fetchedSupplier.setAddress(Optional.ofNullable(docData.get("address").toString()).orElse(""));
            // TODO: ur contract stuff (idk correct or not), can make them into a method, and call it here,so less redundant
//            Timestamp timestamp = (Timestamp) docData.get("contractStartDate");
//            LocalDate contractStartDate = Optional.ofNullable(timestamp)
//                    .map(ts -> ts.toDate().toInstant()
//                            .atZone(ZoneId.systemDefault())
//                            .toLocalDate())
//                    .orElse(null);
            // Convert the stored String back to LocalDate
            String contractStartDateStr = Optional.ofNullable(docData.get("contractStartDate").toString()).orElse(null);
            LocalDate contractStartDate = contractStartDateStr != null ? LocalDate.parse(contractStartDateStr) : null;

            String contractEndDateStr = Optional.ofNullable(docData.get("contractEndDate").toString()).orElse(null);
            LocalDate contractEndDate = contractEndDateStr != null ? LocalDate.parse(contractEndDateStr) : null;

            // Set contract dates
            fetchedSupplier.setContractStartDate(contractStartDate);
            fetchedSupplier.setContractEndDate(contractEndDate);


            // cache and return
            FirebaseConnection.cacheNewDocument(supplierCollection, supplierCollection.document(id), fetchedSupplier);
            return fetchedSupplier;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public boolean updateToDb(String fieldName)  {
        try {
            Field field = this.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            supplierCollection.document(this.id).update(fieldName, field.get(this)).get();
            return true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public boolean deleteFromDb()  {
        try {
            supplierCollection.document(this.id).delete().get();
            return true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    // Extend Contract
    public void extendContract() {
        this.contractEndDate = this.contractEndDate.plusYears(contractYear);
    }

    // Contract Renewal
    // Only allow contract renewal within 2 - 6 months before the current contract ends
    public int renewContract () {
        LocalDate today = LocalDate.now();
        LocalDate renewPeriodStart = this.contractEndDate.minusMonths(6);
        LocalDate renewPeriodEnd = this.contractEndDate.minusMonths(6);

        if (today.isBefore(renewPeriodStart))
            return -1;  // Not reach renew period yet
        else if (today.isAfter(renewPeriodEnd))
            return 1;   // Over renew period, unable to renew
        else {
            extendContract();
            return 0;   // Success renew contract
        }
    }

    // Check Contract Availability
    public boolean checkContractAvailability() {
        LocalDate today = LocalDate.now();
        return (!today.isBefore(this.contractStartDate) && !today.isAfter(this.contractEndDate));
    }
}
