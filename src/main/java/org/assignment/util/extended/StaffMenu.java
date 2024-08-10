package org.assignment.util.extended;

import java.util.Scanner;

public class StaffMenu {

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
