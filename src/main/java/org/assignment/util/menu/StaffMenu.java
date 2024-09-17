package org.assignment.util.menu;

import org.assignment.util.base.BaseMenu;
import org.assignment.util.enums.StaffPosition;
import org.assignment.util.enums.AccessLevel;
import org.assignment.util.extended.Staff;
import org.assignment.util.validationCheck;

import java.util.HashMap;
import java.util.Scanner;
import java.time.LocalDate;
import java.util.List;

import static org.fusesource.jansi.Ansi.ansi;


public class StaffMenu extends BaseMenu {

    private final HashMap<String, BaseMenu.Option> StaffMenuOptions = new HashMap<>();
    private final Staff loggedInStaff;

    public StaffMenu(Staff loggedInStaff) {

        this.loggedInStaff = loggedInStaff;

        // Initialize menu options using createOption
        StaffMenuOptions.put("1", new Option("Signup", () -> restrictedAction(this::signUpStaff, AccessLevel.ADMIN)));
        StaffMenuOptions.put("2", new Option("Delete", () -> restrictedAction(this::deleteStaff, AccessLevel.ADMIN)));
        StaffMenuOptions.put("3", new Option("Modify", () -> restrictedAction(this::modifyStaff, AccessLevel.MANAGER)));
        StaffMenuOptions.put("4", new Option("Display List of staffs", () -> restrictedAction(this::viewStaff, AccessLevel.MANAGER)));
        StaffMenuOptions.put("0", new Option(ansi().fgRed().a("Quit").reset().toString(), this::quitStaffMenu));

        exitFlag = false;
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

    private void signUpStaff() {

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
                    if (staff.setName(name, false)) {
                        break; // Exit loop if name is valid
                    }
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
                    if (staff.setGender(validationCheck.checkGender(gender), false)) {
                        break; // Exit loop if gender is valid
                    }
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
                    if (staff.setPosition(StaffPosition.fromNumber(positionNumber), false)) {
                        break; // Exit loop if position is valid
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println(ansi().bgBrightRed().a(e.getMessage() + "\n").reset());
                }
            }

            newline();

            while(true) {
                System.out.print(ansi().fgRgb(8552916).a("Enter Contact Number: "));
                String contact = scanner.next();
                try {
                    if (staff.setContactNumber(contact, false)) {
                        break;
                    }
                }catch(IllegalArgumentException e) {
                    System.out.println(ansi().bgBrightRed().a(e.getMessage() + "\n").reset());
                }
            }

            newline();

            while(true) {
                System.out.print(ansi().fgRgb(8552916).a("Enter Email: "));
                String email = scanner.next();
                try {
                    if (staff.setEmail(email, false)) {
                        break;
                    }
                }catch(IllegalArgumentException e) {
                    System.out.println(ansi().bgBrightRed().a(e.getMessage() + "\n").reset());
                }
            }

            newline();

            while(true) {
                System.out.print(ansi().fgRgb(8552916).a("Enter Address: "));
                String address = scanner.next();
                try{
                    if (staff.setAddress(address, false)) {
                        break;
                    }
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
                        if (staff.setSalary(salary, false)) {
                            break;
                        }
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
                    if (staff.setPassword(password, false)) {
                        break;
                    }
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

    private void deleteStaff() {

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

    private void viewStaff() {
        try {
            timedPause(300);
            clearScreen();

            System.out.println(ansi().fgBrightGreen().a("View All Staff Records\n=======================\n").reset());

            // Retrieve all staff records from the database
            List<Staff> allStaff = Staff.getAllStaffInfo();

            if (allStaff.isEmpty()) {
                System.out.println(ansi().bgBrightRed().a("No staff records found in the database.").reset());
                return;
            }

            // Print details of each staff member
            for (Staff staff : allStaff) {
                System.out.println(ansi().fgRgb(8552916).a("Staff ID: " + staff.getId()));
                System.out.println("Name: " + staff.getName());
                System.out.println("Gender: " + staff.getGender());
                System.out.println("Position: " + staff.getPosition());
                System.out.println("Contact Number: " + staff.getContactNumber());
                System.out.println("Email: " + staff.getEmail());
                System.out.println("Address: " + staff.getAddress());
                System.out.println("Salary: $" + staff.getSalary());
                System.out.println("Date Joined: " + staff.getDateJoined());
                System.out.println("=======================================\n");
            }

            pause();

        } catch (Exception e) {
            System.out.println(ansi().bgBrightRed().a("An error occurred while retrieving staff records: " + e.getMessage()).reset());
        }
    }

    //TODO: debug and find any possible errors
    private void modifyStaff() {

        Scanner scanner = new Scanner(System.in);

        try {
            timedPause(300);
            clearScreen();

            System.out.println(ansi().cursor(0, 0).fgBrightGreen().a("Modify staff document\n=====================\n").reset());

            System.out.print(ansi().fgRgb(8552916).a("Enter Staff ID to modify: "));
            String staffId = scanner.nextLine();
            Staff staff = Staff.getFromDb(staffId);

            if (staff == null) {
                System.out.println(ansi().bgBrightRed().a("Staff ID not found.").reset());
                return;
            }

            System.out.println("""
                                Which field would you like to modify?
                                =====================================
                                1.  Name
                                2.  Gender
                                3.  Position
                                4.  Contact Number
                                5.  Email
                                6.  Address
                                7.  Salary
                                8.  Password
                                =====================================
                                """);
            System.out.print("\nChoice (1-8): ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character (prevent index out of bounds error)

            boolean updateResult;

            switch (choice) {
                case 1:
                    while (true) {
                        System.out.print(ansi().fgRgb(8552916).a("Enter new name: "));
                        String newName = scanner.nextLine();
                        newline();
                        try {
                            updateResult = staff.setName(newName, true);
                            break; // Exit loop if name is valid
                        } catch (IllegalArgumentException e) {
                            System.out.println(ansi().bgBrightRed().a(e.getMessage() + "\n").reset());
                        }
                    }
                    break;
                case 2:
                    while (true) {
                        System.out.print(ansi().fgRgb(8552916).a("Enter new gender (M/F): "));
                        char newGender = scanner.next().toUpperCase().charAt(0);
                        newline();
                        try {
                            updateResult = staff.setGender(validationCheck.checkGender(newGender), true);
                            break; // Exit loop if gender is valid
                        } catch (IllegalArgumentException e) {
                            System.out.println(ansi().bgBrightRed().a(e.getMessage() + "\n").reset());
                        }
                    }
                    break;
                case 3:
                    int positionNumber;
                    StaffPosition.printPosition();
                    while (true) {
                        System.out.print(ansi().fgRgb(8552916).a("Enter the number corresponding to the position: "));
                        positionNumber = scanner.nextInt();
                        newline();
                        try {
                            updateResult = staff.setPosition(StaffPosition.fromNumber(positionNumber), true);
                            break; // Exit loop if position is valid
                        } catch (IllegalArgumentException e) {
                            System.out.println(ansi().bgBrightRed().a(e.getMessage() + "\n").reset());
                        }
                    }
                    break;
                case 4:
                    while (true) {
                        System.out.print(ansi().fgRgb(8552916).a("Enter new contact number: "));
                        String newContact = scanner.nextLine();
                        newline();
                        try {
                            updateResult = staff.setContactNumber(newContact, true);
                            break; // Exit loop if contact number is valid
                        } catch (IllegalArgumentException e) {
                            System.out.println(ansi().bgBrightRed().a(e.getMessage() + "\n").reset());
                        }
                    }
                    break;
                case 5:
                    while (true) {
                        System.out.print(ansi().fgRgb(8552916).a("Enter new email: "));
                        String newEmail = scanner.nextLine();
                        newline();
                        try {
                            updateResult = staff.setEmail(newEmail, true);
                            break; // Exit loop if email is valid
                        } catch (IllegalArgumentException e) {
                            System.out.println(ansi().bgBrightRed().a(e.getMessage() + "\n").reset());
                        }
                    }
                    break;
                case 6:
                    while (true) {
                        System.out.print(ansi().fgRgb(8552916).a("Enter new address: "));
                        String newAddress = scanner.nextLine();
                        newline();
                        try {
                            updateResult = staff.setAddress(newAddress, true);
                            break; // Exit loop if address is valid
                        } catch (IllegalArgumentException e) {
                            System.out.println(ansi().bgBrightRed().a(e.getMessage() + "\n").reset());
                        }
                    }
                    break;
                case 7:
                    while (true) {
                        System.out.print(ansi().fgRgb(8552916).a("Enter new salary: "));
                        try {
                            if (!scanner.hasNextDouble()) {
                                System.out.println(ansi().bgBrightRed().a("Invalid input. Please enter a valid number.\n").reset());
                                scanner.next(); // Clear the invalid input
                                continue;
                            }

                            double newSalary = scanner.nextDouble();
                            scanner.nextLine(); // Consume newline
                            newline();
                            try {
                                updateResult = staff.setSalary(newSalary, true);
                                break; // Exit loop if salary is valid
                            } catch (IllegalArgumentException e) {
                                System.out.println(ansi().bgBrightRed().a(e.getMessage() + "\n").reset());
                            }
                        } catch (IllegalArgumentException e) {
                            System.out.println(ansi().bgBrightRed().a("Invalid input. Please enter a numeric value.\n").reset());
                            scanner.next(); // Clear the invalid input
                        }
                    }
                    break;
                case 8:
                    while (true) {
                        System.out.print(ansi().fgRgb(8552916).a("Enter new password: "));
                        String newPassword = scanner.nextLine();
                        newline();
                        try {
                            updateResult = staff.setPassword(newPassword, true);
                            break; // Exit loop if password is valid
                        } catch (IllegalArgumentException e) {
                            System.out.println(ansi().bgBrightRed().a(e.getMessage() + "\n").reset());
                        }
                    }
                    break;
                default:
                    System.out.println(ansi().bgBrightRed().a("Invalid choice.").reset());
                    return;
            }
            if (updateResult) {
                System.out.println(ansi().bgBrightGreen().a("Staff details updated successfully!").reset());
                pause();
            } else {
                System.out.println(ansi().bgBrightRed().a("Failed to update staff details.").reset());
                pause();
            }

        } catch (Exception e) {
            System.out.println(ansi().bgBrightRed().a("An unexpected error occurred: " + e.getMessage()).reset());
            pause();
        }
    }

    private void quitStaffMenu() {
        newline();
        System.out.println("See you again " + loggedInStaff.getName() +"! >:)");
        pause();
        exitFlag = true;
    }

    private void restrictedAction(Runnable action, AccessLevel requiredAccessLevel) {
        if (loggedInStaff != null) {
            // Check if the logged-in staff's access level is equal to or higher than the required access level
            if (loggedInStaff.getPosition().getAccessLevel().ordinal() >= requiredAccessLevel.ordinal()) {
                action.run();
            } else {
                System.out.println("Access Denied: Insufficient privileges.");
                pause();
            }
        } else {
            System.out.println("No staff is currently logged in. Please log in first.");
            pause();
        }
    }
}
