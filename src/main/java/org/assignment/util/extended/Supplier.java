package org.assignment.util.extended;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import org.assignment.util.FirebaseConnection;
import org.assignment.util.base.DatabaseOperations;
import org.assignment.util.base.Person;

import java.time.LocalDate;
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
        this("", "", "", "", "", null);
    }

    public Supplier(String id, String name, String contactNumber, String address, String email, LocalDate contractStartDate) {
        super(id, name, contactNumber, email, address);
        this.contractStartDate = contractStartDate;
        extendContract();
    }

    // Getters
    public LocalDate getContractStartDate() { return contractStartDate; }
    public LocalDate getContractEndDate()   { return contractEndDate;   }
    public int getContractYear()            { return contractYear;      }
    public static CollectionReference getSupplierCollection() { return Supplier.supplierCollection; }

    // Setter
    public void setContractStartDate(LocalDate contractStartDate) {
        this.contractStartDate = contractStartDate;
    }

    // database operations implementation
    public boolean addToDb() {
        // add self to db
        return true;
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
            // TODO: ur contract thing maybe

            // cache and return
            FirebaseConnection.cacheNewDocument(supplierCollection, supplierCollection.document(id), fetchedSupplier);
            return fetchedSupplier;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public boolean updateToDb(String fieldName) {
        // update self to db
        return true;
    }

    public boolean deleteFromDb() {
        // delete from db where id is matching
        return true;
    }

    // Extend Contract
    public void extendContract() {
        this.contractEndDate = this.contractStartDate.plusYears(contractYear);
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

    public ArrayList<Item> getSupplyingItems() {
        return new ArrayList<>();
    }
}
