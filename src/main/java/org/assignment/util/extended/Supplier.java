package org.assignment.util.extended;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import org.assignment.util.DbMethods;
import org.assignment.util.FirebaseConnection;
import org.assignment.util.base.DatabaseOperations;
import org.assignment.util.base.Person;
import org.assignment.util.validationCheck;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.*;

public class Supplier extends Person implements DatabaseOperations {
    protected LocalDate contractStartDate;
    protected LocalDate contractEndDate;
    protected static final int contractYear = 2;
    private static final CollectionReference supplierCollection = FirebaseConnection.getDb().collection("Supplier");

    // Constructors
    public Supplier() {
    }

    public Supplier(String name, String contactNumber, String address, String email, LocalDate contractStartDate) {
        super(name, contactNumber, email, address);
        if (contactNumber != null){
            System.out.println("successful Constructor");
            setContractStartDate(contractStartDate);
            setContractEndDate(contractStartDate.plusYears(contractYear));
        }
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
        this.contractEndDate = contractEndDate;
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
            docValues.put("name", getName());
            docValues.put("contactNumber", getContactNumber());
            docValues.put("address", getAddress());
            docValues.put("email", getEmail());
            // TODO: it needs to be stored as either String or Timestamp in db, below need change
            docValues.put("contractStartDate", getContractStartDate().toString());
            docValues.put("contractEndDate", getContractEndDate().toString());

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
            fetchedSupplier.setName(Optional.ofNullable(docData.get("name")).orElse("").toString());
            fetchedSupplier.setContactNumber(Optional.ofNullable(docData.get("contactNumber")).orElse("").toString());
            fetchedSupplier.setEmail(Optional.ofNullable(docData.get("email")).orElse("").toString());
            fetchedSupplier.setAddress(Optional.ofNullable(docData.get("address")).orElse("").toString());
            // TODO: ur contract stuff (idk correct or not), can make them into a method, and call it here,so less redundant

            String contractStartDateStr = docData.get("contractStartDate").toString();
            LocalDate contractStartDate = contractStartDateStr != null ? LocalDate.parse(contractStartDateStr) : null;

            String contractEndDateStr = docData.get("contractEndDate").toString();
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
            FirebaseConnection.deleteCachedDocument(supplierCollection, supplierCollection.document(this.id));
            return true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    // Extend Contract
    public void extendContract() {
        setContractEndDate(getContractEndDate().plusYears(contractYear));
    }

    // Contract Renewal
    // Only allow contract renewal within 2 - 6 months before the current contract ends
    public int checkRenewContract () {
        LocalDate today = LocalDate.now();
        LocalDate renewPeriodStart = this.contractEndDate.minusMonths(6);
        LocalDate renewPeriodEnd = this.contractEndDate;

        if (today.isBefore(renewPeriodStart))
            return -1;  // Not reach renew period yet
        else if (today.isAfter(renewPeriodEnd))
            return 1;   // Over renew period, unable to renew
        else
            return 0;   // Able to extend contract
    }

    // Check Contract Availability
    public boolean checkContractAvailability() {
        LocalDate today = LocalDate.now();
        return (!today.isBefore(this.contractStartDate) && !today.isAfter(this.contractEndDate));
    }

    @Override
    public String toString(){
        // TODO: toString for Supplier
        // return super.toString() + "SUPPLIER DETAILS\n";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        String startDate = this.contractStartDate != null ? this.contractStartDate.format(formatter) : "N/A";
        String endDate = this.contractEndDate != null ? this.contractEndDate.format(formatter) : "N/A";

        return super.toString() + String.format("\nContract Start Date: %s\nContract End Date: %s", startDate, endDate);
    }

    // Update Data Fields (Setters)
    public boolean setName(String name, boolean updateToDb) {
        try{
            String formattedName = validationCheck.checkAndFormatName(name);
            super.setName(formattedName);

            if (updateToDb) {
                return this.updateToDb("name");
            }
            return true;

        } catch (IllegalArgumentException e) {
            // Handle exception or provide user feedback
            System.out.println(e.getMessage());
            // You might want to rethrow or handle the error according to your application's needs
            return false;
        }
    }

    public boolean setContactNumber(String contactNumber, boolean updateToDb) {
        try {
            validationCheck.checkContactNumber(contactNumber);
            super.setContactNumber(contactNumber);
            if (updateToDb) {
                return this.updateToDb("contactNumber");
            }
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean setEmail(String email, boolean updateToDb) {
        try {
            validationCheck.checkEmail(email);
            super.setEmail(email);
            if (updateToDb) {
                return this.updateToDb("email");
            }
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean setAddress(String address, boolean updateToDb) {
        try {
            validationCheck.checkAddress(address);
            super.setAddress(address);
            if (updateToDb) {
                return this.updateToDb("address");
            }
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }


    public static List<Supplier> getAllItem() {
        try {
            List<QueryDocumentSnapshot> documents = Supplier.supplierCollection.get().get().getDocuments();

            List<Supplier> suppliers = new ArrayList<>();
            for (QueryDocumentSnapshot doc : documents) {
                suppliers.add(Supplier.getFromDb(doc.getId()));
            }

            return suppliers;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
}
