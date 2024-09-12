package org.assignment.util.menu;

import org.assignment.util.base.BaseMenu;
import org.assignment.util.extended.Item;
import org.assignment.util.extended.ItemCategory;
import org.assignment.util.extended.Supplier;

import java.util.HashMap;
import java.util.Scanner;

public class ItemMenu extends BaseMenu {
    private static final Scanner scanner = new Scanner(System.in);
    private final HashMap<String, Option> menuOptions = new HashMap<>();

    public ItemMenu() {
        // Initialize menu options using createOption
        menuOptions.put("1", new Option("Add Item", this::addItem));
        menuOptions.put("2", new Option("Remove Item", this::removeItem));
        menuOptions.put("3", new Option("View Items", this::viewItems));
        menuOptions.put("0", new Option("Quit", this::quit));

    }

    @Override
    public void start() {
        while (true) {
            displayOptions(menuOptions);
            getUserOptions(menuOptions);
        }
    }

    private void addItem() {
//        timedPause(200);
        pause();
        clearScreen();
        System.out.println("Adding an item...");
        Item item = new Item();
        // TODO: request for fill up of constructor parameters
        System.out.println("Item Name :");
        item.setName(scanner.next());
        System.out.println("Item Unit Price :");
        item.setUnitPrice(scanner.nextDouble());
        System.out.println("Item Quantity :");
        item.setQuantity(scanner.nextInt());
        System.out.println("Supplier ID :");
        item.setSupplier(Supplier.getFromDb(scanner.next()));
        System.out.println("ItemCategory ID :");
        item.setItemCategory(ItemCategory.getFromDb(scanner.next()));
        scanner.next();
    }

    private void removeItem() {
        System.out.println("Removing an item...");
        // TODO: choose by id
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

//    public static boolean confirmSave(Item item){
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Enter 1 to confirm...");
//        if(scanner.nextInt()==1){
//            return true;
//        }
//        return false;
//    }
}