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

    protected static synchronized void getUserOptions(HashMap<String, Option> options) {
        while (true) {
            System.out.print("> ");
            final String enterValue = scanner.nextLine();

            if (options.containsKey(enterValue)) {
                options.get(enterValue).execute.run();
                break;
            }
            // TODO: instead of printing this,move pointer up and remove the currentLine
            System.out.println(ansi().bgBrightRed().a("Invalid option, please try again.").reset());
        }
    }

    protected static void clearScreen() {
        System.out.print("\033[H\033[2J"); // ANSI escape code to clear screen and reset cursor
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
