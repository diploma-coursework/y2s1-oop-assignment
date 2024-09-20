package org.assignment;

import org.assignment.util.base.BaseMenu;
import org.assignment.util.menu.*;
import java.util.Scanner;

// FIXME: testing Concurrent

public class Main extends BaseMenu {

    public static void main(String[] args) {
        while (true) {
            System.out.println("Temporary Menu");
            System.out.println("1 for Item,2 for ItemCat,3 for Staff, 4 for Supplier, 5 for ItemFlow");
            System.out.print("> ");
            Scanner scanner = new Scanner(System.in);
            switch (scanner.nextInt()) {
                case 1:
                    new ItemMenu().start();
                    break;
                case 2:
                    new ItemCategoryMenu().start();
                    break;
                case 3:
                    new MainMenu().start();
                    break;
                case 4:
                    new SupplierMenu().start();
                    break;
                case 5:
                    new ItemFlowMenu().start();
                    break;
                default:
                    break;
            }
            clearScreen();
        }
    }
}
