package org.assignment.util.menu;

import org.assignment.util.DbMethods;
import org.assignment.util.base.BaseMenu;
import org.assignment.util.extended.Supplier;
import org.assignment.util.validationCheck;

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
        Supplier supplier = new Supplier();
        boolean confirmed = false;

        while (!confirmed) {
            // Name
            System.out.println("Supplier Name       : ");
            supplier.setName(validationCheck.checkAndFormatName(scanner.nextLine()));

            // Contact number
            System.out.println("Contact Number      : ");
            supplier.setContactNumber(scanner.nextLine());

            // Email
            System.out.println("Email               : ");
            supplier.setEmail(scanner.nextLine());
            // Validation Check

            // Address
            System.out.println("Address             : ");
            supplier.setAddress(scanner.nextLine());

            // Ask for confirmation,
            // TODO: if confirm skip,else run clear and loop (maybe done???)
            System.out.print("Confirm to add supplier? (Y/N): ");
            char confirmation = Character.toUpperCase(scanner.nextLine().charAt(0));

            if (confirmation == 'Y')
                confirmed = true;
            else {
                // Clear the supplier details if not confirmed
                supplier.clear();
                System.out.println("Please re-enter the details.\n");
            }
        }

        supplier.setId(DbMethods.dbCreateNewId("Supplier","SUP0",4));
        supplier.setContractStartDate(LocalDate.now());


//        Supplier supplier = new Supplier( name, contactNumber, email, address, contractStartDate);
        // add to database
        supplier.addToDb();
        System.out.println("\nSuccessfully add supplier.");
    }

    public void removeSupplier() {
        // Search for supplier
        Supplier supplier = searchSupplier();

        // confirmation to remove
        char yn = ' ';
        do {
            System.out.print("Are you sure you want to remove the supplier? (Y/N) ");
            yn = Character.toUpperCase(scanner.nextLine().charAt(0));

            if (yn == 'Y') {
                searchSupplier().deleteFromDb();
                System.out.println("\nSuccessfully delete supplier.");
            }
            else if (yn == 'N') {
                return;
            }
            else
            System.out.println("\nInvalid input. Y/N only.\n");
        } while (yn != 'Y');
    }

    public void listSupplier() {
        // get all supplier info from database
        // list out all suppliers
    }

    public void editSupplier() {
        // search for supplier
        Supplier supplier = searchSupplier();

        if (supplier != null) {
            System.out.println(" 1 | Name");
            System.out.println(" 2 | Gender");
            System.out.println(" 3 | Contact Number");
            System.out.println(" 4 | Email");
            System.out.println(" 5 | Address");

            System.out.println("Select 1 - 5 to edit: ");
            int selection = scanner.nextInt();

            switch (selection) {
                case 1:
                    System.out.println("\nSupplier Name: " + supplier.getName());
                    System.out.println("Enter new name: ");

                    supplier.setName(validationCheck.checkAndFormatName(scanner.nextLine()));


            }
        }
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
                supplier = Supplier.getFromDb(id);
            }
        } while (!id.equals("XXX"));

        if (id.equals("XXX")) {
            System.out.println("Returning to the menu...");
            return null;
        }
        else
            return supplier;
    }

    public void renewSupplierContract() {
        // search supplier
        Supplier supplier = searchSupplier();

        // validate if supplier contract is able to be renewed
        int ableToRenew = supplier.checkRenewContract();
        char yn = ' ';

        if (ableToRenew == 0) {
            do {
                System.out.printf("\n\nThe contract of supplier (%s) is able to be renewed.\n", supplier.getName());
                System.out.print("Are you sure to renew contract? (Y/N) ");

                yn = Character.toUpperCase(scanner.nextLine().charAt(0));

                if (yn == 'Y') {
                    supplier.extendContract();
                    System.out.println("\nContract successfully renewed.\n");
                }
                else if (yn != 'N') {
                    return;
                }
                else {
                    System.out.println("\nInvalid input. Y/N only\n");
                }
            } while(yn != 'Y');
        }
        else if (ableToRenew == 1)
            System.out.println("\nContract expired. Unable to renew contract.\n");
        else if (ableToRenew == -1)
            System.out.println(("\nNot within renew period. Unable to renew contract.\n"));
    }

    public void returnToMenu() {
        
    }
}
