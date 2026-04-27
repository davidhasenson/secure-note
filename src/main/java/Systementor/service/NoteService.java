package Systementor.service;

import Systementor.model.Note;
import Systementor.model.User;
import Systementor.repository.NoteRepository;

import java.util.ArrayList;
import java.util.List;

public class NoteService {

    private final NoteRepository noteRepository = new NoteRepository();
    private List<Note> currentNotes = new ArrayList<>();

    public boolean addNote(User user, String title, String content) {
        if (user == null) {
            System.out.println("User is null"); //skriv till logg
            return false;
        }
        if (title == null || title.isBlank()) {
            System.out.println("Title can not be empty");
            return false;
        }
        if (content == null || content.isBlank()) {
            System.out.println("Content can not be empty");
            return false;
        }
        if (title.length() > 250) {
            System.out.println("Title too long");
            return false;
        }
        return noteRepository.createNote(user.getId(), title, content);
    }

    public List<Note> getNotesByUser(User user) {

        if (user == null) {
            System.out.println("User is null");
            return new ArrayList<>();
        }
        currentNotes = noteRepository.getNotesByUser(user);
        return currentNotes;
    }

    public List<Note> getAllNotesAsAdmin(User user) {

        if (user == null) {
            System.out.println("User is null");
            return new ArrayList<>();
        }
        if (user.getRole().equalsIgnoreCase("ADMIN")) {
            currentNotes = noteRepository.getAllNotesAsAdmin();
            return currentNotes;
        } else {
            return new ArrayList<>();
        }
    }

    public Note getNote(User user, int noteId) {
        if (user == null) {
            System.out.println("User is null");
            return null;
        }
        if (noteId < 0) {
            System.out.println("NoteId can not be negative");
            return null;
        }
        switch (user.getRole().toUpperCase()) { //ska man ha till .toUpperCase()
            case "ADMIN" -> {
                return noteRepository.getNoteAsAdmin(noteId);
            }
            case "USER" -> {
                return noteRepository.getNote(user, noteId);
            }
            default -> {
                return null;
            }
        }
    }

    public Note getNoteByNumber(int noteNumber, User user) {
        getNotesByUser(user);
        int index = noteNumber - 1;
        if (index < 0 || index >= currentNotes.size()) {
            System.out.println("Invalid note number");
            return null;
        }
        return currentNotes.get(index);
    }

    public boolean updateNote(User user, String newTitle, String newContent, int noteId) {
        if (user == null) {
            System.out.println("User is null");
            return false;
        }
        if (newTitle == null || newTitle.isBlank()) {
            System.out.println("Title can not be empty");
            return false;
        }
        if (newContent == null || newContent.isBlank()) {
            System.out.println("Content can not be empty");
            return false;
        }
        if (noteId < 0) {
            System.out.println("NoteId can not be negative");
            return false;
        }

        Note noteToBeUpdated = getNote(user, noteId);
        if (noteToBeUpdated == null) {
            System.out.println("Note not found");
            return false;
        }
        noteToBeUpdated.setTitle(newTitle);
        noteToBeUpdated.setContent(newContent);

        return noteRepository.updateNote(noteToBeUpdated);
    }

    public boolean deleteNote(User user, int noteId) {
        if (user == null) {
            System.out.println("User is null");
            return false;
        }

        Note note = getNote(user, noteId);
        if (note == null) {
            System.out.println("Note not found");
            return false;
        }

        switch (user.getRole().toUpperCase()) {
            case "ADMIN" -> {
                return noteRepository.deleteNoteAsAdmin(noteId);
            }
            case "USER" -> {
                boolean success = noteRepository.deleteNote(user, noteId);
                if (success) {
                    getNotesByUser(user);
                }
                return success;
            }
            default -> {
                return false;
            }
        }
    }
}
