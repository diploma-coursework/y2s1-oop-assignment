package org.assignment;

import com.google.cloud.firestore.DocumentSnapshot;
import org.assignment.util.FirebaseConnection;
import org.assignment.util.extended.Item;
import org.assignment.util.extended.ItemCategory;
import org.assignment.util.extended.Staff;
import org.assignment.util.extended.Supplier;
import org.assignment.util.menu.ItemMenu;
import org.assignment.util.menu.MainMenu;

import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {
        // TODO: Dedicated place for adding Dummy Data
//        ItemCategory itemCategory = new ItemCategory("horticultural tools","Essentials for  cultivating and maintaining a lush, thriving garden");
//        Supplier supplier = new Supplier("Yong Eng","010-654-0440","GeorgeTown","CoffeeYupYupYup",LocalDate.of(2023, 9, 14));
//        Item item = new Item("Default",3,5,itemCategory,supplier);
//        itemCategory.addToDb();
//        supplier.addToDb();
//        item.addToDb();
//
        Staff staff = Staff.getFromDb("STF0000");
        if (staff == null) System.out.println("nulllll");
        else System.out.println(staff.getId());

//        try {
//            DocumentSnapshot doc = FirebaseConnection.getDb().collection("Staff").document("STF0000").get().get();
//            System.out.println(doc.getId());
//        } catch (Exception e) {
//            System.err.println(e.getMessage());
//        }
//        new MainMenu().start();

        ItemMenu itemMenu = new ItemMenu();
        itemMenu.start();

    }

}
