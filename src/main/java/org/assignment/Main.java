package org.assignment;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.FirebaseDatabase;
import org.assignment.util.extended.*;
import org.assignment.util.enums.*;

import java.time.LocalDate;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

//        Item item = new Item();
//        item.dbCreateNewItem();
//        item.editInDb("ITEM0004",1,"Chair");
//        System.out.println(item.getClass().getSimpleName());

        Staff staff = new Staff("Ong Jia E", 'M', StaffPosition.INVENTORY_CLERK, "1234567890", "john.doe@example.com", LocalDate.now(), 50000, "71-k Lintang...");
        staff.writeToDb();
    }

}