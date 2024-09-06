package org.assignment;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.FirebaseDatabase;
import org.assignment.util.Utility.DbMethods;
import org.assignment.util.extended.*;
import org.assignment.util.enums.*;

import java.time.LocalDate;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
//        Supplier supplier = new Supplier("Sup","Place","ContactNo.","Joel@email");
//        ItemCategory itemCategory = new ItemCategory("Cat","Category");
//        itemCategory.dbCreateNewCategory();
//        supplier.dbCreateNewSupplier();
//
//        Item item = new Item("Test", 2.00, 2, itemCategory, supplier);
//        item.dbCreateNewItem();
//        System.out.println(item);
//        Item.modifyInDb("ITEM0001",1,"Chair");
//        System.out.println(item.getClass().getSimpleName());

        Item item = DbMethods.deserialized("ITEM0001", Item.class);
        System.out.println("Made it here");
        ItemCategory itemCategory = new ItemCategory("Test", "test", item);
        System.out.println("Item is "+ item);
        itemCategory.addItem(item);
        itemCategory.dbCreateNewCategory();

        // TODO: reason of error, Item no have ItemCategory

        // TODO: item-> deserialized, enter ItemCategory as respective class, serialized into documentReference in db
    }

}