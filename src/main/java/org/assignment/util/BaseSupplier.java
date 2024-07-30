package org.assignment.util;

class BaseSupplier {
    private String id;
    private String name;
    private String address;
    private String contactNumber;
    private String email;

    public BaseSupplier() {
        this.id = null;
        this.name= null;
        this.address = null;
        this.contactNumber = null;
        this.email = null;
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
