package org.assignment.util.extended;

import org.assignment.util.Utility.DbMethods;
import org.assignment.util.base.BaseSupplier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Supplier extends BaseSupplier {

    public Supplier(String name, String address, String contactNumber, String email){
        super(name, address, contactNumber, email);
    }

    // TODO: Db related stuff

    public void dbCreateNewSupplier(){
        this.setId(DbMethods.dbCreateNewId("Supplier","SUP",4));
        System.out.println("set id?");
        System.out.println(getId());
        DbMethods.writeToDb(createMap(), "Supplier", getId());
    }

    public Map<String, Object> createMap() {
        Map<String, Object> supInfo = new HashMap<>();
        supInfo.put("name", getName());
        supInfo.put("address", getAddress());
        supInfo.put("contactNumber", getContactNumber());
        supInfo.put("email",getEmail());
        // Array of Items
        return supInfo;
    }

    // Modify
    public <T> void modifyInDb(String id, int choice, T newValue){
        DbMethods.modifyInDb(id,"Item",editChoice(choice),newValue);
    }

    // TODO: Choice method
    // Will be used in DbMethods;
    public String editChoice(int choice) {
        switch (choice) {
            case 1:
                return "name";
            case 2:
                return "unitPrice";
            case 3:
                return "quantity";
            case 4:
                return "ItemCategory";
            case 5:
                return "Supplier";
            default:
                return null;
        }
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

