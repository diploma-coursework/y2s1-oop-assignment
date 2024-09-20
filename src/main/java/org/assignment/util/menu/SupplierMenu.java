package org.assignment.util.menu;

import org.assignment.util.IdUtils;
import org.assignment.util.base.BaseMenu;
import org.assignment.util.extended.Supplier;
import org.assignment.util.ValidationCheck;

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

        exitFlag = false;
    }

    @Override
    public void start() {
        while (!exitFlag) {
            clearScreen();
            System.out.println("Supplier Menu\n=============");
            displayOptions(supplierMenuOptions);
            getUserOptions(supplierMenuOptions);
        }
    }

    public void addSupplier() {
        Supplier supplier = new Supplier();
        boolean confirmed = false;

        while (!confirmed) {
            clearScreen();

            // Name
            while (true) {
                System.out.print("Supplier Name       : ");
                String name = scanner.nextLine();
                try {
                    if (supplier.setName(ValidationCheck.checkAndFormatName(name), false))
                        break;
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid name. Please try again.");
                }
            }

            // Contact number
            while (true) {
                System.out.print("Contact Number      : ");
                String contactNumber = scanner.nextLine();
                try {
                    if (supplier.setContactNumber(contactNumber, false))
                        break;
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid contact number. Please try again.");
                }
            }

            // Email
            while (true) {
                System.out.print("Email               : ");
                String email = scanner.nextLine();
                try {
                    if (supplier.setEmail(email, false))
                        break;
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid email. Please try again.");
                }
            }

            // Address
            while (true) {
                System.out.print("Address             : ");
                String address = scanner.nextLine();
                try {
                    if (supplier.setAddress(address, false))
                        break;
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid address. Please try again.");
                }
            }

            // Ask for confirmation,
            System.out.print("\nConfirm to add supplier? (Y/N): ");
            char confirmation = Character.toUpperCase(scanner.nextLine().charAt(0));

            if (confirmation == 'Y')
                confirmed = true;
            else {
                // Clear the supplier details if not confirmed
                supplier.clear();
                System.out.println("Please re-enter the details.\n");
                pause();
            }
        }

        supplier.setId(IdUtils.dbCreateNewId("Supplier","SUP0",4));
        supplier.setContractStartDate(LocalDate.now());
        supplier.setContractEndDate(supplier.getContractStartDate().plusYears(2));

//        Supplier supplier = new Supplier( name, contactNumber, email, address, contractStartDate);
        // add to database
        supplier.addToDb();
        System.out.println("\nSuccessfully add supplier.");
        pause();
    }

    public void removeSupplier() {
        clearScreen();
        // Search for supplier
        Supplier supplier = searchSupplier();

        if (supplier == null) {
            System.out.println("No supplier found.");
        }

        // confirmation to remove
        char yn = ' ';
        do {
            System.out.print("Are you sure you want to remove the supplier? (Y/N) ");
            yn = Character.toUpperCase(scanner.nextLine().charAt(0));

            if (yn == 'Y') {
                supplier.deleteFromDb();
                System.out.println("\nSuccessfully delete supplier.");
            }
            else if (yn == 'N') {
                return;
            }
            else
                System.out.println("\nInvalid input. Y/N only.\n");
        } while (yn != 'Y');

        pause();
    }

    public void listSupplier() {
        clearScreen();
        List<Supplier> supplierList = Supplier.getAllItem();
        if (supplierList == null) System.out.println("No supplier. Please add a supplier.");
        else {
            for (Supplier supplier : supplierList) {
                System.out.println(supplier.toString() + "\n");
            }
        }
        pause();
    }

    public void editSupplier() {
        clearScreen();
        // search for supplier
        Supplier supplier = searchSupplier();

        // Select data field to edit (except contract info and id cuz they are user-unchangeable)
        if (supplier != null) {
            System.out.println(" 1 | Name           | " + supplier.getName());
            System.out.println(" 2 | Contact Number | " + supplier.getContactNumber());
            System.out.println(" 3 | Email          | " + supplier.getEmail());
            System.out.println(" 4 | Address        | " + supplier.getAddress());
            System.out.println("---+----------------+------------------------------");
            System.out.println(" 0 | Return to Menu");

            System.out.println("Select 0 - 4 to edit: ");
            int selection = scanner.nextInt();
            scanner.nextLine();

            boolean updateResult = false;

            switch (selection) {
                // Edit Name
                case 1:
                    while (true) {
                        clearScreen();
                        System.out.println("\nSupplier Name: " + supplier.getName());
                        System.out.print("Enter new name: ");

                        String newName = scanner.nextLine();

                        try {
                            updateResult = supplier.setName(newName, true);
                            if (updateResult)
                                break;
                        } catch (IllegalArgumentException e) {
                            System.out.println("Invalid name. Please try again.");
                        }
                    }
                    break;

                // Edit contact number
                case 2:
                    while (true) {
                        System.out.println("\nContact Number: " + supplier.getContactNumber());
                        System.out.println("Enter new contact number: ");

                        String newContactNum = scanner.nextLine();

                        try {
                            updateResult = supplier.setContactNumber(newContactNum, true);
                            if (updateResult)
                                break;
                        } catch (IllegalArgumentException e) {
                            System.out.println("Invalid contact number. Please try again.");
                        }
                    }
                    break;

                // Edit email
                case 3:
                    while (true) {
                        System.out.println("\nEmail: " + supplier.getEmail());
                        System.out.println("Enter new email: ");

                        String newEmail = scanner.nextLine();

                        try {
                            updateResult = supplier.setEmail(newEmail, true);
                            if (updateResult)
                                break;
                        } catch (IllegalArgumentException e) {
                            System.out.println("Invalid email. Please try again.");
                        }
                    }
                    break;

                // Edit address
                case 4:
                    while (true) {
                        System.out.println("\nAddress: " + supplier.getEmail());
                        System.out.println("Enter new address: ");

                        String newAddress = scanner.nextLine();

                        try {
                            updateResult = supplier.setAddress(newAddress, true);
                            if (updateResult)
                                break;
                        } catch (IllegalArgumentException e) {
                            System.out.println("Invalid address. Please try again.");
                        }
                    }
                    break;

                case 0:
                    System.out.println("Returning to supplier menu...");
                    pause();
                    return;

                default:
                    System.out.println("Invalid choice");
                    return;
            }

            if (updateResult)
                System.out.println("Updated successfully!");
            else
                System.out.println("Update failed.");
        }

        pause();
    }

    public Supplier searchSupplier() {
        clearScreen();

        System.out.print("Please enter the ID of the supplier: ");
        String id = (scanner.nextLine()).toUpperCase();

        if (!id.equals("XXX")) {
            return Supplier.getFromDb(id);
        }

        return null;
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
                    supplier.setContractEndDate(supplier.getContractEndDate(), true);
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

        pause();
    }

    public void returnToMenu() {
        exitFlag = true;
    }
}
