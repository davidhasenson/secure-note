package service;

import model.Note;
import model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import repository.NoteRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NoteService {

    private final NoteRepository noteRepository = new NoteRepository();
    private static final Logger logger = LogManager.getLogger(NoteService.class);

    public boolean addNote(User user, String title, String content) {
        logger.info("Adding Note");
        if (user == null) {
            logger.warn("User is null");
            return false;
        }
        if (title == null || title.isBlank()) {
            logger.warn("Title can not be empty");
            return false;
        }
        if (content == null || content.isBlank()) {
            logger.warn("Content can not be empty");
            return false;
        }
        if (title.length() > 250) {
            logger.warn("Title too long");
            return false;
        }
        if (content.length() > 10000) {
            logger.warn("Content too long");
            return false;
        }
        UUID uuid = UUID.randomUUID();
        return noteRepository.createNote(user.getId(), title, content, uuid);
    }

    public List<Note> getNotesByUser(User user) {
        logger.info("Getting Notes");
        if (user == null) {
            logger.warn("User is null");
            return new ArrayList<>();
        }
        return noteRepository.getNotesByUser(user);
    }

    public List<Note> getAllNotesAsAdmin(User user) {
        logger.info("Getting All Notes");
        if (user == null) {
            logger.warn("User is null");
            return new ArrayList<>();
        }
        if (user.getRole().equalsIgnoreCase("ADMIN")) {
            return noteRepository.getAllNotesAsAdmin();
        } else {
            return new ArrayList<>();
        }
    }

    public Note getNote(User user, UUID uuid) {
        logger.info("Getting Note");
        if (user == null) {
            logger.warn("User is null");
            return null;
        }
        switch (user.getRole().toUpperCase()) { //ska man ha till .toUpperCase()
            case "ADMIN" -> {
                return noteRepository.getNoteAsAdmin(uuid);
            }
            case "USER" -> {
                return noteRepository.getNote(user, uuid);
            }
            default -> {
                return null;
            }
        }
    }

    public boolean updateNote(User user, String newTitle, String newContent, UUID uuid) {
        logger.info("Updating Note");
        if (user == null) {
            logger.warn("User is null");
            return false;
        }
        if (newTitle == null || newTitle.isBlank()) {
            logger.warn("Title can not be empty");
            return false;
        }
        if (newContent == null || newContent.isBlank()) {
            logger.warn("Content can not be empty");
            return false;
        }
        Note noteToBeUpdated = getNote(user, uuid);
        if (noteToBeUpdated == null) {
            logger.warn("Note not found");
            return false;
        }
        noteToBeUpdated.setTitle(newTitle);
        noteToBeUpdated.setContent(newContent);
        return noteRepository.updateNote(noteToBeUpdated);
    }

    public boolean deleteNote(User user, UUID uuid) {
        logger.info("Deleting Note");
        if (user == null) {
            logger.warn("User is null");
            return false;
        }
        Note note = getNote(user, uuid);
        if (note == null) {
            logger.warn("Note not found");
            return false;
        }
        switch (user.getRole().toUpperCase()) {
            case "ADMIN" -> {
                return noteRepository.deleteNoteAsAdmin(uuid); //vad händer här?
            }
            case "USER" -> {
                return noteRepository.deleteNote(user, uuid);
            }
            //Prepared to add more roles in the future.
            default -> {
                logger.warn("Role did not match");
                return false;
            }
        }
    }
}
