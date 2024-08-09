package org.assignment;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.FirebaseDatabase;
import org.assignment.util.extended.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
//        Staff.dbCreateNewStaff();
//        Item test = new Item();
//        test.dbCreateNewItem();
        loginMenu();

    }

    private static void loginMenu(){
        while(true){
            Scanner scanner = new Scanner(System.in);
            System.out.println("----------------------");
            System.out.println("Staff ID :");
            String userName = scanner.next();
            System.out.println("Password :");
            String passWord = scanner.next();
            if (!validAccount(userName, passWord)){
                System.out.println("Staff ID or Password is Wrong.\nPlease try again.");
                try {
                    Thread.sleep(2000); // Pause for 2 seconds (2000 milliseconds)
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                new ClearScreen();
            }
            else {
                System.out.print("----------------------");
                break;
            }
            System.out.print("----------------------");
        }
    }


    private static boolean validAccount(String iDString, String passString){
        //        Do checking from Database
        if (iDString.compareToIgnoreCase("AAA") != 0 || passString.compareTo("AAA") != 0){
            return false;
        }
        return true;
    }

}