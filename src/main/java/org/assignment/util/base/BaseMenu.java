package org.assignment.util.base;

import java.util.HashMap;
import java.util.Scanner;

public abstract class BaseMenu {
    protected static final Scanner scanner = new Scanner(System.in);

    public void start() {};

    protected static void displayOptions(HashMap<String, Option> options) {
        options.forEach((optionIdentifier, option) -> {
            System.out.printf("%s - %s\n", optionIdentifier, option.description);
        });
    }

    protected static void getUserOptions(HashMap<String, Option> options) {
        while (true) {
            System.out.print("> ");
            final String enterValue = scanner.next();

            if (options.containsKey(enterValue)) {
                options.get(enterValue).execute();
                break;
            }
            System.out.println("Invalid option, please try again.");
        }
    }

    protected record Option(String description, Runnable execute) {}
}
