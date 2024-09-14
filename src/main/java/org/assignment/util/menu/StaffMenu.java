package org.assignment.util.menu;

import org.assignment.util.base.BaseMenu;
import org.assignment.util.enums.StaffPosition;
import org.assignment.util.enums.AccessLevel;
import org.assignment.util.extended.Staff;
import org.assignment.util.validationCheck;


import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;
import java.time.LocalDate;


public class StaffMenu extends BaseMenu {

    private final HashMap<String, BaseMenu.Option> menuOptions = new HashMap<>();
    private Staff loggedInStaff;

    public StaffMenu() {
        // Initialize menu options using createOption
        menuOptions.put("1", new Option("Signup", () -> restrictedAction(this::signUp, AccessLevel.ADMIN)));
        menuOptions.put("3", new Option("Delete", () -> restrictedAction(this::delete, AccessLevel.MANAGER)));
        menuOptions.put("4", new Option("Modify", () -> restrictedAction(this::modify, AccessLevel.MANAGER)));
        menuOptions.put("5", new Option("Display List of staffs", () -> restrictedAction(this::view, AccessLevel.MANAGER)));
        menuOptions.put("q", new Option("Quit", this::quit));
    }

    @Override
    public void start() {
        while (true) {
            displayOptions(menuOptions);
            getUserOptions(menuOptions);
        }
    }

    private void signUp() {

        Scanner scanner = new Scanner(System.in);

        try {
            pause();
            clearScreen();

            Staff staff = new Staff();

            System.out.println("Please enter ALL necessary infos");
            System.out.println("=======================================");

            System.out.print("Enter Name: ");
            staff.setName(scanner.nextLine());

            System.out.print("Enter Gender (M/F): ");
            char gender = scanner.next().toUpperCase().charAt(0);
            staff.setGender(validationCheck.checkGender(gender));

            StaffPosition.printPosition();
            System.out.print("Enter the number corresponding to the position: ");
            staff.setPosition(StaffPosition.fromNumber(scanner.nextInt()));

            System.out.print("Enter Contact Number: ");
            staff.setContactNumber(scanner.next());

            System.out.print("Enter Email: ");
            staff.setEmail(scanner.next());

            scanner.nextLine(); // Consume newline

            System.out.print("Enter Address: ");
            staff.setAddress(scanner.nextLine());

            System.out.print("Enter Salary: ");
            staff.setSalary(scanner.nextInt());

            scanner.nextLine(); // Consume newline before reading password

            System.out.print("Enter a password: ");
            staff.setPassword(scanner.nextLine());

            staff.setDateJoined(LocalDate.now()); // Set the date joined to the current date

            if (staff.addToDb()) {
                System.out.println("Staff added successfully!");
            } else {
                System.out.println("Failed to add staff.");
            }

            while (true) {

                // confirmation of adding staff
                System.out.print("To Confirm Adding(Enter 1) else (Enter 0): ");
                int i = scanner.nextInt();

                if (i == 1) {
                    // check whether staff is added to firestore
                    if (staff.addToDb()) {
                        System.out.println("Staff added successfully!");
                    } else {
                        System.out.println("Failed to add staff.");
                    }
                    break;

                } else if (i == 0) {
                    System.out.println("Action Terminated");
                    break;

                } else {
                    System.out.println("Invalid input. Please enter 1 to confirm or 0 to cancel.");
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    private void modify() {

        Scanner scanner = new Scanner(System.in);

        try {
            System.out.print("Enter Staff ID to modify: ");

            String staffId = scanner.nextLine();
            Staff staff = Staff.getFromDb(staffId);
            if (staff == null) {
                System.out.println("Staff ID not found.");
                return;
            }

            System.out.println("Which field would you like to modify?"
                    + "\n====================================="
                    + "\n1.  Name"
                    + "\n2.  Gender"
                    + "\n3.  Position"
                    + "\n4.  Contact Number"
                    + "\n5.  Email"
                    + "\n6.  Address"
                    + "\n7.  Salary"
                    + "\n8.  Password"
                    + "\n====================================="
                    + "\nChoice (1-8): ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            String fieldName = "";

            switch (choice) {
                case 1:
                    System.out.print("Enter new Name: ");
                    staff.setName(scanner.nextLine());
                    fieldName = "name";
                    break;
                case 2:
                    System.out.print("Enter new Gender (M/F): ");
                    char gender = scanner.next().toUpperCase().charAt(0);
                    staff.setGender(validationCheck.checkGender(gender));
                    fieldName = "gender";
                    break;
                case 3:
                    StaffPosition.printPosition();
                    System.out.print("Enter the number corresponding to the position: ");
                    staff.setPosition(StaffPosition.fromNumber(scanner.nextInt()));
                    fieldName = "position";
                    break;
                case 4:
                    System.out.print("Enter new Contact Number: ");
                    staff.setContactNumber(scanner.next());
                    fieldName = "contactNumber";
                    break;
                case 5:
                    System.out.print("Enter new Email: ");
                    staff.setEmail(scanner.next());
                    fieldName = "email";
                    break;
                case 6:
                    System.out.print("Enter new Address: ");
                    staff.setAddress(scanner.nextLine());
                    fieldName = "address";
                    break;
                case 7:
                    System.out.print("Enter new Salary: ");
                    staff.setSalary(scanner.nextDouble());
                    fieldName = "salary";
                    break;
                case 8:
                    System.out.print("Enter new Password: ");
                    staff.setPassword(scanner.nextLine());
                    fieldName = "password";
                    break;
                default:
                    System.out.println("Invalid choice.");
                    return;
            }

            // Update the field in the database
            if (staff.updateToDb(fieldName)) {
                System.out.println("Staff record updated successfully.");
            } else {
                System.out.println("Failed to update staff record.");
            }

        }catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    private void delete() {

    }

    private void view() {

    }

    private void quit() {
        System.out.println("Quit");
        System.exit(0);
    }

    private static boolean validAccount(String iDString, String passString) {
        // Check from database
        // Placeholder logic
        return iDString.equalsIgnoreCase("AAA") && passString.equals("AAA");
    }

    private Map<String, Object> convertStaffToMap(Staff staff) {

        Map<String, Object> staffData = new HashMap<>();

        staffData.put("name", staff.getName());
        staffData.put("gender", String.valueOf(staff.getGender())); // Convert char to string
        staffData.put("position", staff.getPosition().name()); // Enum to string
        staffData.put("contactNumber", staff.getContactNumber());
        staffData.put("email", staff.getEmail());
        staffData.put("dateJoined", staff.getDateJoined().toString()); // LocalDate to string
        staffData.put("salary", staff.getSalary());
        staffData.put("address", staff.getAddress());

        return staffData;
    }

    private void restrictedAction(Runnable action, AccessLevel requiredAccessLevel) {
        if (loggedInStaff != null) {
            // Check if the logged-in staff's access level is equal to or higher than the required access level
            if (loggedInStaff.getPosition().getAccessLevel().ordinal() >= requiredAccessLevel.ordinal()) {
                action.run();
            } else {
                System.out.println("Access Denied: Insufficient privileges.");
            }
        } else {
            System.out.println("No staff is currently logged in. Please log in first.");
        }
    }
}
