package org.assignment.util.base;

import java.util.ArrayList;

public class BaseSupplier {
    protected String id;
    protected String name;
    protected String address;
    protected String contactNumber;
    protected String email;
    protected ArrayList<BaseItem> items;

    public BaseSupplier() {
        this(null, null, null, null, null);
    }

    public BaseSupplier(String id, String name, String address, String contactNumber, String email) {
        this.id = id;
        this.name= name;
        this.address = address;
        this.contactNumber = contactNumber;
        this.email = email;
        this.items = new ArrayList<>();
    }

    // setters
    public void setId(String id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    // getters
    public String getId() {
        return this.id;
    }
    public String getName() {
        return this.name;
    }
    public String getAddress() {
        return this.address;
    }
    public String getContactNumber() {
        return this.contactNumber;
    }
    public String getEmail() {
        return this.email;
    }
}
