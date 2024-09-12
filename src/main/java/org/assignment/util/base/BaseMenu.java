package org.assignment.util.base;

import java.util.HashMap;
import java.util.Optional;
import java.util.Scanner;

public abstract class BaseMenu {
    protected static final Scanner scanner = new Scanner(System.in);

    public void start() {};

    protected static synchronized void displayOptions(HashMap<String, Option> options) {
        options.forEach((optionIdentifier, option) -> {
            System.out.printf("%s - %s\n", optionIdentifier, option.description);
        });
    }

    protected static synchronized void getUserOptions(HashMap<String, Option> options) {
        while (true) {
            System.out.print("> ");
            final String enterValue = scanner.next();

            if (options.containsKey(enterValue)) {
                options.get(enterValue).execute.run();
                break;
            }
            System.out.println("Invalid option, please try again.");
        }
    }

    protected static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    protected static void pause() {
        try {
            Optional.of(System.in.read()).ifPresent(value -> {});

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected static void timedPause(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public record Option(String description, Runnable execute) {}
}
