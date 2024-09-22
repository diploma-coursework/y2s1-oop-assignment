package org.assignment.util.base;

import static org.fusesource.jansi.Ansi.ansi;

public abstract class Person {
    protected String id;
    protected String name;
    protected String contactNumber;
    protected String email;
    protected String address;

    public Person() { this("", "", "", "");}

    public Person(String name, String contactNumber, String email, String address) {
        this.name = name;
        this.contactNumber = contactNumber;
        this.email = email;
        this.address = address;
    }

    // setters
    public void setId(String id) { this.id = id; }
    public void setName(String name) {
        this.name = name;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    // getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getContactNumber() { return contactNumber; }
    public String getEmail() { return email; }
    public String getAddress() { return address; }

    @Override
    public String toString(){
        String idPlaceHolder;
        if (getId() == null) idPlaceHolder = ansi().fgYellow().a("[Unassigned]").reset().toString(); else idPlaceHolder = ansi().fgYellow().a(getId()).reset().toString();

        return "-------------------------------------------" + "\n" +
                "ID            : " + idPlaceHolder + "\n" +
                "Name          : " + ansi().fgBlue().a(getName()).reset().toString() + "\n" +
                "Contact Number    : " + ansi().fgBrightMagenta().a(getContactNumber()) + "\n" +
                "Email      : " + ansi().fgBrightMagenta().a(getEmail()).reset() + "\n" +
                "Address:" + ansi().fgBrightMagenta().a(getAddress()).reset() + "\n" +
                ansi().reset();
    }
}
