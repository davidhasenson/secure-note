package Systementor.ui;

import Systementor.model.Note;
import Systementor.model.User;
import Systementor.service.AuthService;
import Systementor.service.NoteService;

import java.util.List;

public class ConsoleMenu {

    private final AuthService service = new AuthService();
    private final NoteService noteService = new NoteService();

    public void start() {
        boolean running = true;

        while (running) {
            System.out.println("\n--- Secure note---");
            System.out.println("1. Register new user");
            System.out.println("2. Log in");
            System.out.println("3. Exit");

            String choice = ConsoleHelper.enterString("Chose an option ");
            switch (choice) {
                case "1" -> register();
                case "2" -> login();
                case "3" -> running = false;
                default -> System.out.println("Invalid choice");
            }
        }
    }

    private void register() {
        String username = ConsoleHelper.enterString("Enter username: ");
        String password = ConsoleHelper.enterString("Enter password: ");

        boolean success = service.register(username, password);
        if (success) {
            System.out.println("User registered successfully");

        } else {
            System.out.println("Could not save user");
        }
    }

    private void login() {
        String username = ConsoleHelper.enterString("Enter username: ");
        String password = ConsoleHelper.enterString("Enter password: ");

        User user = service.login(username, password);

        if (user != null) {
            System.out.println(user.getUsername() + " successfully logged in");
            switch (user.getRole()) {
                case "USER" -> userMenu(user);
                case "ADMIN" -> adminMenu(user);
                default -> System.out.println("Invalid role");
            }
        } else {
            System.out.println("Login failed");
        }
    }

    public void userMenu(User loggedInUser) {
        //boolean userMenu = true;
        boolean loggedIn = true;
        while (loggedIn) {
            // while (userMenu) {
            System.out.println("\n--- Secure note user---");
            System.out.println("1. Add note");
            System.out.println("2. View notes");
            System.out.println("3. Edit note");
            System.out.println("4. Delete note");
            System.out.println("5. Change password");
            System.out.println("6. Exit");

            String choice = ConsoleHelper.enterString("Chose an option ");
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
        //  boolean adminMenu = true;
        boolean loggedIn = true;
        while (loggedIn) {
            //while (adminMenu) {
            System.out.println("\n--- Secure note admin---");
            System.out.println("1. Add note");
            System.out.println("2. View notes");
            System.out.println("3. view all notes");
            System.out.println("4. Edit note");
            System.out.println("5. Delete note");
            System.out.println("6. Change password");
            System.out.println("7. Exit");

            String choice = ConsoleHelper.enterString("Choice an option ");

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
        String oldPassword = ConsoleHelper.enterString("Enter old password: ");
        String newPassword = ConsoleHelper.enterString("Enter new password: ");
        String confirmNewPassword = ConsoleHelper.enterString("Enter new password again: ");
        boolean success = service.changePassword(loggedInUser, oldPassword, newPassword, confirmNewPassword);
        if (success) {
            System.out.println("Password changed successfully");
        } else {
            System.out.println("Password change failed");
        }
    }

    public void addNote(User user) {
        String title = ConsoleHelper.enterString("Enter title: ");
        String content = ConsoleHelper.enterString("Enter content: ");

        boolean success = noteService.addNote(user, title, content);
        if (success) {
            System.out.println("Note added successfully");
        } else {
            System.out.println("Note could not be saved");
        }
    }

    public void viewNotesByUser(User user) {
        List<Note> notes = noteService.getNotesByUser(user);
        printNotes(notes);
    }

    public void viewNotesAsAdmin(User user) {
        List<Note> notes = noteService.getAllNotesAsAdmin(user);
        printNotes(notes);
    }

    public void printNotes(List<Note> notes) {
        if (notes.isEmpty()) {
            System.out.println("No notes where found");
            return;
        }
        for (int i = 0; i < notes.size(); i++) {
            Note note = notes.get(i);
            System.out.println((i + 1) + " " + note);
        }
    }

    public void editNoteAsUser(User user) {
      //  viewNotesByUser(user);
        int noteNumber = ConsoleHelper.enterInt("Enter the number of the note you want to edit: ");

        Note note = noteService.getNoteByNumber(noteNumber,user);
        if (note == null) {
            System.out.println("Note could not be found");
            return;
        }
        //kontrollera om man får redigera eller inte
        note = noteService.getNote(user, note.getId());

        if (note == null) {
            System.out.println("Note could not be found");
            return;
        }

        System.out.println("You chosen to edit note: \n" + note);

        String newTitle = ConsoleHelper.updateValue("title",note.getTitle());
        String newContent = ConsoleHelper.updateValue("content",note.getContent());

        if (ConsoleHelper.choseYesOrNo("Do you want to save the note?")) {
            boolean success = noteService.updateNote(user, newTitle, newContent, note.getId());
            if (success) {
                System.out.println("Note edited successfully");
            } else {
                System.out.println("Note could not be saved");
            }
        } else {
            System.out.println("Changes discarded.");
        }
    }

    public void deleteNote(User user) {
        int noteId = ConsoleHelper.enterInt("Enter the ID of the note you want to delete: ");

        Note noteToDelete = noteService.getNote(user, noteId);
        if (noteToDelete == null) {
            System.out.println("Note could not be found");
            return;
        }

        System.out.println(noteToDelete);
        if (ConsoleHelper.choseYesOrNo("Are you sure you want to delete this note?")) {
            boolean success = noteService.deleteNote(user, noteToDelete.getId());
            if (success) {
                System.out.println("Note deleted successfully");
            } else {
                System.out.println("Note could not be deleted");
            }
        } else {
            System.out.println("Deletion canceled.");
        }
    }
}
