package org.assignment.util.extended;

import org.assignment.util.base.BaseMenu;

import java.util.HashMap;

public class ItemMenu extends BaseMenu {

    private final HashMap<String, Option> menuOptions = new HashMap<>();

    public ItemMenu() {
        // Initialize menu options using createOption
        menuOptions.put("1", new Option("Add Item", this::addItem));
        menuOptions.put("2", new Option("Remove Item", this::removeItem));
        menuOptions.put("3", new Option("View Items", this::viewItems));
        menuOptions.put("q", new Option("Quit", this::quit));

    }

    @Override
    public void start() {
        while (true) {
            displayOptions(menuOptions);
            getUserOptions(menuOptions);
        }
    }

    private void addItem() {
        System.out.println("Adding an item...");
        // Logic to add item
    }

    private void removeItem() {
        System.out.println("Removing an item...");
        // Logic to remove item
    }

    private void viewItems() {
        System.out.println("Viewing all items...");
        // Logic to view items
    }

    private void quit() {
        System.out.println("Exiting the menu...");
        System.exit(0);
    }
}