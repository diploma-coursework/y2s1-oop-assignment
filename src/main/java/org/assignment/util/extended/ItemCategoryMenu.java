package org.assignment.util.extended;

import java.util.Scanner;

public class ItemCategoryMenu {
    public static boolean confirmSave(ItemCategory itemCategory) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter 1 to confirm...");
        if(scanner.nextInt()==1){
            return true;
        }
        return false;
    }

}
