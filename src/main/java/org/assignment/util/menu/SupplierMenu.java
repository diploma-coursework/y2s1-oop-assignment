package org.assignment.util.menu;

import org.assignment.util.DbMethods;
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
        supplierMenuOptions.put("4", new Option("Edit Supplier", this::editSupplier));
        supplierMenuOptions.put("5", new Option("Renew Supplier's Contract", this::renewSupplierContract));
        supplierMenuOptions.put("0", new Option("Return to Menu", this::returnToMenu));
        // My idea was if user choose 0 back to last menu cuz supplier menu should be sub menu of staff...?
    }

    public void addSupplier() {
        // Name
        Supplier supplier = new Supplier();
        System.out.println("Supplier Name       : ");
        supplier.setName(scanner.nextLine());
        // Validation Check

        // Contact number
        System.out.println("Contact Number      : ");
        supplier.setContactNumber(scanner.nextLine());
        // Validation Check

        // Email
        System.out.println("Email               : ");
        supplier.setEmail(scanner.nextLine());
        // Validation Check

        // Address
        System.out.println("Address             : ");
        supplier.setAddress(scanner.nextLine());

        // Validation Check
        // Ask for confirmation,
        // TODO: if confirm skip,else run clear and loop
        supplier.clear();


        supplier.setId(DbMethods.dbCreateNewId("Supplier","SUP0",4));
        supplier.setContractStartDate(LocalDate.now());


//        Supplier supplier = new Supplier( name, contactNumber, email, address, contractStartDate);

        supplier.addToDb();
        // add to database
    }

    public void removeSupplier() {
        // Search for supplier
        searchSupplier();
        // confirmation to remove
        System.out.print("Are you sure you want to remove the supplier? (Y/N) ");
        char yn = Character.toUpperCase(scanner.nextLine().charAt(0));

        if (yn == 'Y') {
            // remove from database
        }
        else if (yn == 'N') {
            // return to menu
        }
        else {
            // invalid
        }

    }

    public void listSupplier() {
        // get all supplier info from database
        // list out all suppliers
    }

    public void editSupplier() {
        // search for supplier
        // select data field to edit (except id and contract infos)
    }

    public Supplier searchSupplier() {
        // search for supplier
        Supplier supplier = new Supplier();
        String id = " ";

        do {
            System.out.println("Please enter the ID of the supplier: ");
            System.out.print("(Key in XXX to cancel action): ");
            id = (scanner.nextLine()).toUpperCase();

            if (!id.equals("XXX")) {
                // Search Supplier
            }
        } while (!id.equals("XXX"));
        return supplier;
    }

    public void renewSupplierContract() {
        // search supplier
        // validate if supplier contract is able to be renewed
        // confirmation
        // update contract info
    }

    public void returnToMenu() {
        
    }
}
