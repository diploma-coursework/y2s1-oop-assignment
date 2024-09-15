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

import static org.fusesource.jansi.Ansi.ansi;


public class StaffMenu extends BaseMenu {

    private final HashMap<String, BaseMenu.Option> StaffMenuOptions = new HashMap<>();
    private final Staff loggedInStaff;

    public StaffMenu(Staff loggedInStaff) {

        this.loggedInStaff = loggedInStaff;

        // Initialize menu options using createOption
        StaffMenuOptions.put("1", new Option("Signup", () -> restrictedAction(this::signUp, AccessLevel.ADMIN)));
        StaffMenuOptions.put("2", new Option("Delete", () -> restrictedAction(this::delete, AccessLevel.ADMIN)));
        StaffMenuOptions.put("3", new Option("Modify", () -> restrictedAction(this::modify, AccessLevel.MANAGER)));
        StaffMenuOptions.put("4", new Option("Display List of staffs", () -> restrictedAction(this::view, AccessLevel.MANAGER)));
        StaffMenuOptions.put("0", new Option(ansi().fgRed().a("Quit").reset().toString(), this::quit));

        exitFlag = false;
        actionExit = false;
    }

    @Override
    public void start() {
        while (!exitFlag) {
            displayOptions(StaffMenuOptions);
            getUserOptions(StaffMenuOptions);
        }
    }

    private void signUp() {

        Scanner scanner = new Scanner(System.in);

        try {
            pause();
            clearScreen();

            Staff staff = new Staff();

            System.out.println("Please enter ALL necessary info");
            System.out.println("=======================================" + "\n");
            System.out.print("Enter Name: ");
            staff.setName(scanner.nextLine());

            newline();

            System.out.print("Enter Gender (M/F): ");
            char gender = scanner.next().toUpperCase().charAt(0);
            staff.setGender(validationCheck.checkGender(gender));

            newline();

            StaffPosition.printPosition();
            System.out.print("Enter the number corresponding to the position: ");
            staff.setPosition(StaffPosition.fromNumber(scanner.nextInt()));

            newline();

            System.out.print("Enter Contact Number: ");
            staff.setContactNumber(scanner.next());

            newline();

            System.out.print("Enter Email: ");
            staff.setEmail(scanner.next());

            scanner.nextLine(); // Consume newline
            newline();

            System.out.print("Enter Address: ");
            staff.setAddress(scanner.nextLine());

            newline();

            System.out.print("Enter Salary: ");
            staff.setSalary(scanner.nextInt());

            scanner.nextLine(); // Consume newline before reading password
            newline();

            System.out.print("Enter a password: ");
            staff.setPassword(scanner.nextLine());

            newline();

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

    //TODO: debug and find any possible errors
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

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    private void delete() {

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Staff ID to delete: ");
        String staffId = scanner.nextLine();

        Staff staff = Staff.getFromDb(staffId);
        if (staff == null) {
            System.out.println("Staff ID not found.");
            return;
        }

        if (staff.deleteFromDb()) {
            System.out.println("Staff record deleted successfully.");
        } else {
            System.out.println("Failed to delete staff record.");
        }
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

    public void newline() {
        System.out.println();
    }
}
