package org.assignment.util.extended;

import org.assignment.util.Utility.DbMethods;
import org.assignment.util.base.BaseSupplier;

import java.util.*;

public class Supplier extends BaseSupplier {
    private List<String> item;

    public Supplier(){
    }

    public Supplier(String name, String address, String contactNumber, String email, String... item){
        super(name, address, contactNumber, email);
        this.item = new ArrayList<>(Arrays.asList(item));
    }

    // Getter
    public List<String> getItems() {
        return this.item;
    }

    public int getSize(){
        return getItems().size();
    }

    // Setter
    public void setItems(List<String> items) {
        this.item = new ArrayList<>(items);
    }

    // add an item to existing list
    public void addItem(String item) {
        this.item.add(item);
    }

    // TODO: Db related stuff
    public HashMap<String, Object> toHashMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", getName());
        map.put("address", getAddress());
        map.put("contactNumber", getContactNumber());
        map.put("email",getEmail());
        if(getItems() != null){
            for (String item : getItems()) {
                if (DbMethods.deserialized(item, Item.class) != null) {
                    map.put("item", getItems());
                }
            }
        }
        return map;
    }

    public void dbCreateNewSupplier(){
        this.setId(DbMethods.dbCreateNewId("Supplier","SUP",4));
        DbMethods.writeToDb(toHashMap(), "Supplier", getId());
    }

    // Modify
    public <T> void modifyInDb(String id, int choice, T newValue){
        DbMethods.modifyInDb(id,"Item",editChoice(choice),newValue);
    }

    // TODO: Choice method, change to fit ur Supplier Class
    // Will be used in DbMethods;
    public String editChoice(int choice) {
        switch (choice) {
            case 1:
                return "name";
            case 2:
                return "address";
            case 3:
                return "contactNumber";
            case 4:
                return "email";
            default:
                return null;
        }
    }

    // Search
    public ArrayList<Supplier> searchSupplier (String keyword) {
        ArrayList<Supplier> result = new ArrayList<>();

        // Get database info idk (next line will be changed)
        ArrayList<Supplier> suppliers = new ArrayList<>();
        for (Supplier supplier : suppliers) {
            String name = supplier.getName();
            String id = supplier.getId();
            if (name.contains(keyword))
                result.add(supplier);
            else if (id.contains(keyword));
                result.add(supplier);
         }

        return result;
    }

    // Renew Contract
    public void renewContract() {
        contractStartDate = contractEndDate;
        contractEndDate = contractStartDate.plusYears(2);
    }

    // Shift letters and big/small + shift numbers to encrypt/decrypt
    public String encryptData (String data) {
        StringBuilder encrypted = new StringBuilder();

        for (char c : data.toCharArray()) {
            if (Character.isUpperCase(c)) {
                char switchedChar = (c == 'Z' ? 'A' : (char) (c + 1));
                encrypted.append(Character.toLowerCase(switchedChar));
            }
            else if (Character.isLowerCase(c)) {
                char switchedChar = (c == 'z' ? 'a' : (char) (c + 1));
                encrypted.append(Character.toUpperCase(switchedChar));
            }
            else if (Character.isDigit(c))
                encrypted.append(c == '9' ? '0' : (char) (c + 1));
            else
                encrypted.append(c);
        }

        return encrypted.toString();
    }

    public String decryptData (String encrypted) {
        StringBuilder decrypted = new StringBuilder();

        for (char c : encrypted.toCharArray()) {
            if (Character.isUpperCase(c)) {
                char switchedChar = (c == 'A' ? 'Z' : (char) (c - 1));
                decrypted.append(Character.toLowerCase(switchedChar));
            }
            else if (Character.isLowerCase(c)) {
                char switchedChar = (c == 'a' ? 'z' : (char) (c - 1));
                decrypted.append(Character.toUpperCase(switchedChar));
            }
            else if (Character.isDigit(c))
                decrypted.append(c == '0' ? '9' : (char) (c - 1));
            else
                decrypted.append(c);
        }

        return decrypted.toString();
    }
}

