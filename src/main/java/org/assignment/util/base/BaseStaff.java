package org.assignment.util.base;

import org.assignment.util.enums.StaffPosition;
import java.time.LocalDate;

public class BaseStaff {
    private String id;
    private String name;
    private char gender;
    private StaffPosition position;
    private String contactNumber;
    private String email;
    private LocalDate dateJoined;
    private int salary;
    private String address;

    public BaseStaff() {
        this.id = null;
        this.name = null;
        this.gender = '\u0000';
        this.position = null;
        this.contactNumber = null;
        this.email = null;
        this.dateJoined = null;
        this.salary = 0;
        this.address = null;
    }

    public BaseStaff(String id, String name, char gender, StaffPosition position, String contactNumber, String email, LocalDate dateJoined, int salary, String address) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.position = position;
        this.contactNumber = contactNumber;
        this.email = email;
        this.dateJoined = dateJoined;
        this.salary = salary;
        this.address = address;
    }

    // setters
    public void setId(String id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setGender(char gender) {
        this.gender = gender;
    }
    public void setPosition(StaffPosition position) {
        this.position = position;
    }
    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setDateJoined(LocalDate dateJoined) {
        this.dateJoined = dateJoined;
    }
    public void setSalary(int salary) {
        this.salary = salary;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    // getters
    public String getId() {
        return this.id;
    }
    public String getName() {
        return this.name;
    }
    public char getGender() {
        return this.gender;
    }
    public StaffPosition getPosition() {
        return this.position;
    }
    public String getContactNumber() {
        return this.contactNumber;
    }
    public String getEmail() {
        return this.email;
    }
    public LocalDate getDateJoined() {
        return this.dateJoined;
    }
    public int getSalary() {
        return this.salary;
    }
    public String getAddress() {
        return this.address;
    }
}