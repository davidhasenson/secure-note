package ui;

import model.Note;

import java.util.List;
import java.util.Scanner;

public class ConsoleHelper {

    private static final Scanner scanner = new Scanner(System.in);

    private ConsoleHelper() {
    }

    public static String enterString(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }

    public static int enterInt(String message) {
        while (true) {
            System.out.print(message);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("You must enter a number");
            }
        }
    }

    public static boolean choseYesOrNo(String message) {
        System.out.println(message + " (y/n)");
        return "y".equalsIgnoreCase(scanner.nextLine().trim());
    }

    public static String updateValue(String valueName, String currentValue) {
        System.out.println("Do you want to enter a new" + valueName + " ?" + " (y/n)");
        if ("y".equalsIgnoreCase(scanner.nextLine().trim())) {
            System.out.print("Enter new " + valueName + ": ");
            return scanner.nextLine();
        }
        return currentValue;
    }

    public static void printNotes(List<Note> notes) {
        if (notes.isEmpty()) {
            System.out.println("No notes where found");
            return;
        }
        for (int i = 0; i < notes.size(); i++) {
            Note note = notes.get(i);
            System.out.println("Note: " + (i + 1) + "\n" + note);
        }
    }

}
