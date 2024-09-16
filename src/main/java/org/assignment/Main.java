package org.assignment;

import com.google.cloud.firestore.DocumentSnapshot;
import org.assignment.util.FirebaseConnection;
import org.assignment.util.base.BaseMenu;
import org.assignment.util.extended.Item;
import org.assignment.util.extended.ItemCategory;
import org.assignment.util.extended.Staff;
import org.assignment.util.extended.Supplier;
import org.assignment.util.menu.*;

import java.time.LocalDate;
import java.util.Scanner;

public class Main extends BaseMenu {

    public static void main(String[] args) {
        // TODO: Dedicated place for adding Dummy Data
//        ItemCategory itemCategory = new ItemCategory("horticultural tools","Essentials for  cultivating and maintaining a lush, thriving garden");
//        Supplier supplier = new Supplier("Yong Eng","010-654-0440","GeorgeTown","CoffeeYupYupYup",LocalDate.of(2023, 9, 14));
//        Item item = new Item("Default",3,5,itemCategory,supplier);
//        itemCategory.addToDb();
//        supplier.addToDb();
//        item.addToDb();
//
//        new MainMenu().start();

//        ItemMenu itemMenu = new ItemMenu();
//        itemMenu.start();

//        new ItemCategoryMenu().start();


//          StaffMenu staffMenu = new StaffMenu();
//          staffMenu.start();


        while (true) {
            System.out.println("Temporary Menu");
            System.out.println("1 for Item,2 for ItemCat,3 for Staff, 4 for Supplier");
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
                default:
                    break;
            }
            clearScreen();
        }
    }

}
