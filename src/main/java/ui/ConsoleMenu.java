package ui;

import helper.Helper;
import model.Note;
import model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.AuthService;
import service.NoteService;

import java.util.List;
import java.util.UUID;

public class ConsoleMenu {

    private final AuthService service = new AuthService();
    private final NoteService noteService = new NoteService();
    private static final Logger logger = LogManager.getLogger(ConsoleMenu.class);

    public void start() {
        logger.info("Menu started");
        boolean running = true;

        while (running) {
            System.out.println("\n--- Secure note---");
            System.out.println("1. Register new user");
            System.out.println("2. Log in");
            System.out.println("3. Exit");

            String choice = Helper.enterString("Chose an option ");
            switch (choice) {
                case "1" -> register();
                case "2" -> login();
                case "3" -> running = false;
                default -> System.out.println("Invalid choice");
            }
        }
    }

    private void register() {
        logger.info("Register new user");
        boolean notRegisterd = true;
        while (notRegisterd) {
            String username = Helper.enterString("Enter username: ");
            String password = Helper.enterString("Enter password: ");

            boolean success = service.register(username, password);
            if (success) {
                System.out.println("User registered successfully");
                logger.info("New user created");
                notRegisterd = false;
            } else {
                System.out.println("Could not save user");
                logger.info("Could not create user");
                System.out.println("Do you want to try again?");
                String tryAgain = Helper.enterString("Do you want to try again? (y/n) ");
                if (!tryAgain.equalsIgnoreCase("y")) {
                    notRegisterd = false;
                }
                logger.info("Trying again");
            }
        }
    }

    private void login() {
        logger.info("Logging in");
        String username = Helper.enterString("Enter username: ");
        String password = Helper.enterString("Enter password: ");

        User user = service.login(username, password);

        if (user != null) {
            System.out.println(user.getUsername() + " successfully logged in");
            logger.info("Logged in successfully");
            switch (user.getRole()) {
                case "USER" -> userMenu(user);
                case "ADMIN" -> adminMenu(user);
                default -> System.out.println("Invalid role");
            }
        } else {
            System.out.println("Login failed");
            logger.info("Login failed");
        }
    }

    public void userMenu(User loggedInUser) {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n--- Secure note user---");
            System.out.println("1. Add note");
            System.out.println("2. View notes");
            System.out.println("3. Edit note");
            System.out.println("4. Delete note");
            System.out.println("5. Change password");
            System.out.println("6. Exit");

            String choice = Helper.enterString("Chose an option ");
            switch (choice) {
                case "1" -> addNote(loggedInUser);
                case "2" -> viewNotesByUser(loggedInUser);
                case "3" -> editNoteAsUser(loggedInUser);
                case "4" -> deleteNote(loggedInUser);
                case "5" -> changePassword(loggedInUser);
                case "6" -> loggedIn = false;
                default -> System.out.println("Invalid choice");
            }
        }
    }

    public void adminMenu(User loggedInUser) {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n--- Secure note admin---");
            System.out.println("1. Add note");
            System.out.println("2. View notes");
            System.out.println("3. View all notes");
            System.out.println("4. Edit note");
            System.out.println("5. Delete note");
            System.out.println("6. Change password");
            System.out.println("7. Exit");

            String choice = Helper.enterString("Choice an option ");

            switch (choice) {
                case "1" -> addNote(loggedInUser);
                case "2" -> viewNotesByUser(loggedInUser);
                case "3" -> viewNotesAsAdmin(loggedInUser);
                case "4" -> editNoteAsUser(loggedInUser);
                case "5" -> deleteNote(loggedInUser);
                case "6" -> changePassword(loggedInUser);
                case "7" -> loggedIn = false;
                default -> System.out.println("Invalid choice");
            }
        }
    }

    public void changePassword(User loggedInUser) {
        logger.info("Changing password");
        String oldPassword = Helper.enterString("Enter old password: ");
        String newPassword = Helper.enterString("Enter new password: ");
        String confirmNewPassword = Helper.enterString("Enter new password again: ");
        boolean success = service.changePassword(loggedInUser, oldPassword, newPassword, confirmNewPassword);
        if (success) {
            System.out.println("Password changed successfully");
            logger.info("Password changed successfully");
        } else {
            System.out.println("Password change failed");
            logger.info("Password change failed");
        }
    }

    public void addNote(User user) {
        logger.info("Adding note");
        String title = Helper.enterString("Enter title: ");
        String content = Helper.enterString("Enter content: ");

        boolean success = noteService.addNote(user, title, content);
        if (success) {
            System.out.println("Note added successfully");
            logger.info("Note added successfully");
        } else {
            System.out.println("Note could not be saved");
            logger.info("Note could not be saved");
        }
    }

    public void viewNotesByUser(User user) {
        logger.info("View notes by user");
        List<Note> notes = noteService.getNotesByUser(user);
        Helper.printNotes(notes);
    }

    public void viewNotesAsAdmin(User user) {
        logger.info("View notes as admin");
        List<Note> notes = noteService.getAllNotesAsAdmin(user);
        Helper.printNotes(notes);
    }

    public void editNoteAsUser(User user) {
        logger.info("Edit note");
        UUID uuid = Helper.enterUUID("Enter the uuid of the note you want to edit: ");
        if (uuid == null) {
            return;
        }

        Note note = noteService.getNote(user, uuid);
        if (note == null) {
            System.out.println("Note could not be found");
            logger.warn("Note could not be found");
            return;
        }

        System.out.println("You chosen to edit note: \n" + note);

        String newTitle = Helper.updateValue("title", note.getTitle());
        String newContent = Helper.updateValue("content", note.getContent());

        if (Helper.choseYesOrNo("Do you want to save the note?")) {
            logger.info("Updating note");
            boolean success = noteService.updateNote(user, newTitle, newContent, note.getUUID());
            if (success) {
                System.out.println("Note edited successfully");
                logger.info("Note edited successfully");
            } else {
                System.out.println("Note could not be saved");
                logger.warn("Note could not be saved");
            }
        } else {
            System.out.println("Changes discarded.");
            logger.info("Changes discarded.");
        }
    }

    public void deleteNote(User user) {
        logger.info("Deleting note");
        UUID uuid = Helper.enterUUID("Enter the uuid of the note you want to delete: ");
        if (uuid == null) {
            return;
        }
        Note noteToDelete = noteService.getNote(user, uuid);
        if (noteToDelete == null) {
            System.out.println("Note could not be found");
            logger.info("Note could not be found");
            return;
        }
        System.out.println(noteToDelete);
        if (Helper.choseYesOrNo("Are you sure you want to delete this note?")) {
            boolean success = noteService.deleteNote(user, noteToDelete.getUUID());
            if (success) {
                System.out.println("Note deleted successfully");
                logger.info("Note deleted successfully");
            } else {
                System.out.println("Note could not be deleted");
                logger.warn("Note could not be deleted");
            }
        } else {
            System.out.println("Deletion canceled.");
            logger.info("Deletion canceled.");
        }
    }
}
