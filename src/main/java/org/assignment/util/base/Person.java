package org.assignment.util.base;

import org.assignment.util.validationCheck;

public abstract class Person {
    protected String id;
    protected String name;
    protected String contactNumber;
    protected String email;
    protected String address;

    public Person() { this("", "", "", "");}

    public Person(String name, String contactNumber, String email, String address) {
        this.id = id;
        this.name = name;
        this.contactNumber = contactNumber;
        this.email = email;
        this.address = address;
    }

    // setters
    public void setId(String id) { this.id = id; }
    public void setName(String name) {
        validationCheck.checkAndFormatName(name);
        this.name = name;
    }
    public void setEmail(String email) {
        validationCheck.checkAndFormatName(email);
        this.email = email;
    }
    public void setContactNumber(String contactNumber) {
        validationCheck.checkContactNumber(contactNumber);
        this.contactNumber = contactNumber;
    }
    public void setAddress(String address) {
        validationCheck.checkAddress(address);
        this.address = address;
    }

    // getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getContactNumber() { return contactNumber; }
    public String getEmail() { return email; }
    public String getAddress() { return address; }
}
