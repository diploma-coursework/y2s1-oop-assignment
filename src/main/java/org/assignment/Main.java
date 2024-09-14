package org.assignment;

import org.assignment.util.extended.Item;
import org.assignment.util.extended.ItemCategory;
import org.assignment.util.extended.Supplier;
import org.assignment.util.menu.ItemMenu;
import org.assignment.util.menu.MainMenu;

import java.time.LocalDate;

public class Main {
    static {
        // TODO: Dedicated place for adding Dummy Data
//        ItemCategory itemCategory = new ItemCategory("horticultural tools","Essentials for  cultivating and maintaining a lush, thriving garden");
//        Supplier supplier = new Supplier("Yong Eng","010-654-0440","GeorgeTown","CoffeeYupYupYup",LocalDate.of(2023, 9, 14));
//        Item item = new Item("Default",3,5,itemCategory,supplier);
//        itemCategory.addToDb();
//        supplier.addToDb();
//        item.addToDb();
    }
    public static void main(String[] args) {
        new MainMenu().start();

        ItemMenu itemMenu = new ItemMenu();
        itemMenu.start();

    }

}
