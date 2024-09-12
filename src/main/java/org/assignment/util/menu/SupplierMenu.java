package org.assignment.util.menu;

import org.assignment.util.base.BaseMenu;
import org.assignment.util.extended.Supplier;

import java.time.LocalDate;
import java.util.*;

public class SupplierMenu extends BaseMenu {
    private final HashMap<String, Option> supplierMenuOptions = new HashMap<>();

    public SupplierMenu() {
        /* Initialize menu options using createOption */
        supplierMenuOptions.put("1", new Option("Add Supplier", this::addSupplier));
        supplierMenuOptions.put("2", new Option("Remove Supplier", this::removeSupplier));
        supplierMenuOptions.put("3", new Option("List Suppliers", this::listSupplier));
        supplierMenuOptions.put("4", new Option("Search Supplier", this::searchSupplier));
        supplierMenuOptions.put("5", new Option("Renew Supplier's Contract", this::renewSupplierContract));
        supplierMenuOptions.put("0", new Option("Return to Menu", this::returnToMenu));
        // My idea was if user choose 0 back to last menu cuz supplier menu should be sub menu of staff...?
    }

    public void addSupplier() {
        // Name
        System.out.println("Supplier Name  : ");
        String name = scanner.nextLine();
        // Validation Check

        // Contact number
        System.out.println("Contact Number : ");
        String contactNumber = scanner.nextLine();
        // Validation Check

        // Email
        System.out.println("Email          : ");
        String email = scanner.nextLine();
        // Validation Check

        // Address
        System.out.println("Address        : ");
        String address = scanner.nextLine();
        // Validation Check

        // where the create id function idk
        String id = "";
        LocalDate contractStartDate = LocalDate.now();

        Supplier supplier = new Supplier(id, name, contactNumber, email, address, contractStartDate);
        // add to database
    }

    public void removeSupplier() {

    }

    public void listSupplier() {

    }

    public void searchSupplier() {

    }

    public void renewSupplierContract() {

    }

    public void returnToMenu() {

    }
}
