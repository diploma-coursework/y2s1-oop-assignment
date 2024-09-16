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

        String staffMenuGreet = "Welcome to Staff Menu " + loggedInStaff.getName() + "!\n==========================";

        while (!exitFlag) {
            clearScreen();
            System.out.println(ansi().cursor(0, 0).fgBrightGreen().a(staffMenuGreet).reset());
            displayOptions(StaffMenuOptions);
            getUserOptions(StaffMenuOptions);
        }
    }

    private void signUp() {

        Scanner scanner = new Scanner(System.in);

        try {
            timedPause(300);
            clearScreen();

            Staff staff = new Staff();

            System.out.println(ansi().cursor(0, 0).fgBrightGreen().a("Please enter ALL necessary info\n===============================\n").reset());



            while (true) {
                System.out.print(ansi().fgRgb(8552916).a("Enter Name: "));
                String name = scanner.nextLine();
                try {
                    name = validationCheck.checkAndFormatName(name);
                    staff.setName(name);
                    break; // Exit loop if name is valid
                } catch (IllegalArgumentException e) {
                    System.out.println(ansi().bgBrightRed().a(e.getMessage() +"\n").reset());
                }
            }

            newline();

            while (true) {
                System.out.print(ansi().fgRgb(8552916).a("Enter Gender (M/F): "));
                char gender = scanner.next().toUpperCase().charAt(0);
                try {
                    staff.setGender(validationCheck.checkGender(gender));
                    break; // Exit loop if gender is valid
                } catch (IllegalArgumentException e) {
                    System.out.println(ansi().bgBrightRed().a(e.getMessage() + "\n").reset());
                }
            }

            newline();

            int positionNumber;
            StaffPosition.printPosition();
            while (true) {
                System.out.print(ansi().fgRgb(8552916).a("Enter the number corresponding to the position: "));
                positionNumber = scanner.nextInt();
                try {
                    staff.setPosition(StaffPosition.fromNumber(positionNumber));
                    break; // Exit loop if position is valid
                } catch (IllegalArgumentException e) {
                    System.out.println(ansi().bgBrightRed().a(e.getMessage() + "\n").reset());
                }
            }

            newline();

            while(true) {
                System.out.print(ansi().fgRgb(8552916).a("Enter Contact Number: "));
                String contact = scanner.next();
                try {
                    staff.setContactNumber(contact);
                    break;
                }catch(IllegalArgumentException e) {
                    System.out.println(ansi().bgBrightRed().a(e.getMessage() + "\n").reset());
                }
            }

            newline();

            while(true) {
                System.out.print(ansi().fgRgb(8552916).a("Enter Email: "));
                String email = scanner.next();
                try {
                    staff.setEmail(email);
                    break;
                }catch(IllegalArgumentException e) {
                    System.out.println(ansi().bgBrightRed().a(e.getMessage() + "\n").reset());
                }
            }

            newline();

            while(true) {
                System.out.print(ansi().fgRgb(8552916).a("Enter Address: "));
                String address = scanner.next();
                try{
                    staff.setAddress(address);
                    break;
                }catch(IllegalArgumentException e){
                    System.out.println(ansi().bgBrightRed().a(e.getMessage()).reset());
                    System.out.println("\n");
                }
            }

            newline();

            while (true) {
                System.out.print(ansi().fgRgb(8552916).a("Enter Salary: "));

                try {
                    // Handle non-integer inputs
                    if (!scanner.hasNextInt()) {
                        System.out.println(ansi().bgBrightRed().a("Invalid input. Please enter a valid number.\n").reset());
                        scanner.next(); // Clear the invalid input
                        continue;
                    }

                    int salary = scanner.nextInt();

                    // Validation check
                    try {
                        staff.setSalary(salary);
                        break;
                    } catch (IllegalArgumentException e) {
                        System.out.println(ansi().bgBrightRed().a(e.getMessage() + "\n").reset());
                    }

                } catch (IllegalArgumentException e) {
                    System.out.println(ansi().bgBrightRed().a("Invalid input. Please enter a numeric value.\n").reset());
                    scanner.next(); // Clear the invalid input
                }
            }

            newline();
            scanner.nextLine();

            while(true) {
                System.out.print(ansi().fgRgb(8552916).a("Enter a password: "));
                String password = scanner.nextLine();
                try{
                    staff.setPassword(password);
                    break;
                }catch(IllegalArgumentException e) {
                    System.out.println(ansi().bgBrightRed().a(e.getMessage() + "\n").reset());
                }
            }

            newline();
            System.out.print(ansi().reset());

            staff.setDateJoined(LocalDate.now()); // Set the date joined to the current date

            while (true) {
                System.out.print(ansi().bgBrightGreen().a("To Confirm Adding (Enter 1) else (Enter 0): ").reset());
                int i = scanner.nextInt();

                if (i == 1) {
                    // Confirm and add to the database
                    boolean isAdded = staff.addToDb();

                    if (isAdded) {
                        System.out.println(ansi().bgBrightGreen().a("Staff added successfully!").reset());
                        pause();
                    } else {
                        System.out.println(ansi().bgBrightRed().a("Failed to add staff.").reset());
                    }
                    break; // Break out of the loop after adding the staff
                } else if (i == 0) {
                    System.out.println(ansi().bgBrightRed().a("Action terminated.").reset());
                    pause();
                    break; // Break out of the loop if action is terminated
                } else {
                    System.out.println(ansi().bgBrightRed().a("Invalid input. Please enter 1 to confirm or 0 to cancel.").reset());
                }
            }

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    private void delete() {

        Scanner scanner = new Scanner(System.in);

        try {
            timedPause(300);
            clearScreen();

            System.out.println(ansi().cursor(0, 0).fgBrightGreen().a("Delete a Staff Record\n=======================\n").reset());

            while (true) {
                System.out.print(ansi().fgRgb(8552916).a("Enter Staff ID to delete: "));
                String staffId = scanner.nextLine();

                try {
                    // Retrieve staff object from the database
                    Staff staff = Staff.getFromDb(staffId);

                    if (staff == null) {
                        // If the staff is not found, print error and prompt again
                        System.out.println(ansi().bgBrightRed().a("Staff ID not found. Please try again.\n").reset());
                    } else {
                        // Confirm deletion
                        System.out.println(ansi().fgBrightYellow().a("Staff found: " + staff.getName() + "\n").reset());

                        while (true) {
                            System.out.print(ansi().bgBrightGreen().a("Confirm deletion (Enter 1 to delete, 0 to cancel): ").reset());
                            int confirmation = scanner.nextInt();

                            if (confirmation == 1) {
                                // Proceed with deletion
                                if (staff.deleteFromDb()) {
                                    System.out.println(ansi().bgBrightGreen().a("Staff record deleted successfully!").reset());
                                } else {
                                    System.out.println(ansi().bgBrightRed().a("Failed to delete staff record.").reset());
                                }
                                pause();
                                return;
                            } else if (confirmation == 0) {
                                System.out.println(ansi().bgBrightRed().a("Action terminated.").reset());
                                pause();
                                return;
                            } else {
                                // Invalid input for confirmation
                                System.out.println(ansi().bgBrightRed().a("Invalid input. Please enter 1 to confirm or 0 to cancel.\n").reset());
                            }
                        }
                    }
                } catch (IllegalArgumentException e) {
                    // Handle any unexpected validation issues
                    System.out.println(ansi().bgBrightRed().a(e.getMessage() + "\n").reset());
                }
            }

        } catch (Exception e) {
            System.out.println(ansi().bgBrightRed().a("An unexpected error occurred: " + e.getMessage()).reset());
        }
    }

    private void view() {

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
