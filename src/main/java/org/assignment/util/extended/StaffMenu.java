package org.assignment.util.extended;

import org.assignment.util.base.BaseMenu;
import java.util.HashMap;
import java.util.Scanner;

public class StaffMenu extends BaseMenu {

    private final HashMap<String, BaseMenu.Option> menuOptions = new HashMap<>();

    public StaffMenu() {
        // Initialize menu options using createOption
        menuOptions.put("1", new Option("Signup", this::signUp));
        menuOptions.put("2", new Option("Login", this::Login));
        menuOptions.put("3", new Option("delete", this::delete));
        menuOptions.put("4", new Option("Quit", this::modify));
        menuOptions.put("5", new Option("Display List of staffs", this::display));
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
        System.out.println("Adding an item...");
        // Logic to add item
    }

    private void Login() {
        System.out.println("Removing an item...");
        // Logic to remove item
    }

    private void delete() {
        System.out.println("Viewing all items...");
        // Logic to view items
    }

    private void modify() {
        System.out.println("Exiting the menu...");
    }

    private void display() {
        System.out.println("display");
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
}
