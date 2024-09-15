package org.assignment.util.base;

import java.util.HashMap;
import java.util.Optional;
import java.util.Scanner;
import static org.fusesource.jansi.Ansi.ansi;

public abstract class BaseMenu {
    protected static final Scanner scanner = new Scanner(System.in);
    protected boolean exitFlag;
    protected boolean actionExit;

    public void start() {};

    protected static synchronized void displayOptions(HashMap<String, Option> options) {
        options.forEach((optionIdentifier, option) -> {
            if (optionIdentifier.equals("0")) return;
            System.out.printf("%s - %s\n", optionIdentifier, option.description);
        });
        if (options.containsKey("0")) System.out.printf("0 - %s\n", options.get("0").description);
    }

    protected static synchronized Option getUserOptions(HashMap<String, Option> options) {
        while (true) {
            System.out.print("> ");
            final String enterValue = scanner.nextLine();

            if (options.containsKey(enterValue)) {
                options.get(enterValue).execute.run();
                return options.get(enterValue);
            }

            System.out.print(ansi().bgBrightRed().a("Invalid option, please try again.").reset()
                    .cursorUp(1).cursorLeft(33).a(" ".repeat(40)).cursorLeft(40));
        }
    }

    protected static boolean getYesNoOption() {
        System.out.print("> ");

        while (true) {
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes")) return true;
            if (input.equalsIgnoreCase("n") || input.equalsIgnoreCase("no")) return false;

            System.out.print(ansi().cursorUp(1).eraseLine().a("> "));
        }
    }

    protected static void clearScreen() {
        System.out.print(ansi().eraseScreen().cursor(0, 0));
        System.out.print("\033[3J"); // Clear scrollback buffer
        System.out.flush();
    }

    protected static void pause() {
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

    protected static void timedPause(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static<T> Optional<T> acceptExitAction(T result){
        if (result instanceof String && result.toString().equalsIgnoreCase("exit")){
            return Optional.empty();
        }
        else if (result instanceof Number && (result.equals(-1)) || result.equals(-1.0)){
            return Optional.empty();
        }
        return Optional.of(result);
    }

    public record Option(String description, Runnable execute) {}
}
