package helper;

import model.Note;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class Helper {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Logger logger = LogManager.getLogger(Helper.class);

    private Helper() {
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

    public static UUID enterUUID(String message) {
        String input = Helper.enterString(message);
        UUID uuid;
        try {
            return uuid = UUID.fromString(input);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid uuid");
            logger.warn("Invalid uuid");
            return null;
        }
    }

    public static boolean choseYesOrNo(String message) {
        logger.info("Option: " + message);
        System.out.println(message + " (y/n)");
        return "y".equalsIgnoreCase(scanner.nextLine().trim());
    }

    public static String updateValue(String valueName, String currentValue) {
        logger.info("Update " + valueName);
        System.out.println("Do you want to enter a new" + valueName + " ?" + " (y/n)");
        if ("y".equalsIgnoreCase(scanner.nextLine().trim())) {
            System.out.print("Enter new " + valueName + ": ");
            return scanner.nextLine();
        }
        return currentValue;
    }

    public static void printNotes(List<Note> notes) {
        logger.info("Printing " + notes.size() + " notes");
        if (notes.isEmpty()) {
            System.out.println("No notes where found");
            logger.info("No notes where found");
            return;
        }
        System.out.println();
        for (int i = 0; i < notes.size(); i++) {
            Note note = notes.get(i);
            System.out.println(note);
        }
        logger.info("Notes have been printed");
    }

    public static Note resultSetToNote(ResultSet resultSet) throws SQLException {
        logger.info("ResultSet to Note");
        Note note = new Note();
        note.setId(resultSet.getInt("id"));
        note.setTitle(resultSet.getString("title"));
        note.setContent(resultSet.getString("content"));
        note.setUserId(resultSet.getInt("user_id"));
        note.setUUID(UUID.fromString(resultSet.getString("UUID")));
        return note;
    }


}
