package org.assignment.util.extended;

import org.assignment.util.base.Person;
import java.util.ArrayList;

public class Supplier extends Person {
    public Supplier() {
        this("", "", "", "", "");
    }

    public Supplier(String id, String name, String contactNumber, String address, String email) {
        super(id, name, contactNumber, email, address);
    }

    public ArrayList<Item> getSupplyingItems() {
        return new ArrayList<>();
    }
}
