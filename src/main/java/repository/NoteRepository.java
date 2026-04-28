package repository;

import config.DatabaseConnection;
import helper.Helper;
import model.Note;
import model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NoteRepository {
    private static final Logger logger = LogManager.getLogger(NoteRepository.class);

    public boolean createNote(int userId, String title, String content, UUID uuid) {
        logger.info("Creating note");
        String sql = "INSERT INTO notes (title, content, user_id, uuid) VALUES (?,?,?,?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, title);
            statement.setString(2, content);
            statement.setInt(3, userId);
            statement.setString(4, uuid.toString());

            int rows = statement.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            logger.error("SQL Error ", e);
            return false;
        }
    }

    public boolean updateNote(Note note) {
        logger.info("Updating note");
        String sql = "UPDATE notes SET title=?, content=? WHERE uuid=?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, note.getTitle());
            statement.setString(2, note.getContent());
            statement.setString(3, note.getUUID().toString());

            int rows = statement.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            logger.error("SQL Error ", e);
            return false;
        }
    }

    public List<Note> getNotesByUser(User user) {
        logger.info("Getting notes by user");
        String sql = "SELECT * FROM notes WHERE user_id=?";
        List<Note> notes = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, user.getId());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                notes.add(Helper.resultSetToNote(resultSet));
            }

        } catch (SQLException e) {
            logger.error("SQL Error ", e);
        }
        return notes;
    }

    public List<Note> getAllNotesAsAdmin() {
        logger.info("Getting all notes as admin");
        String sql = "SELECT * FROM notes";
        List<Note> notes = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                notes.add(Helper.resultSetToNote(resultSet));
            }

        } catch (SQLException e) {
            logger.error("SQL Error ", e);
        }
        return notes;
    }

    public Note getNote(User user, UUID uuid) {
        logger.info("Getting note by user");
        String sql;
// lägg till kontroll lager i noteservice.

        sql = "SELECT * FROM notes WHERE user_id= ? AND uuid=?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, user.getId());
            statement.setString(2, uuid.toString());


            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Helper.resultSetToNote(resultSet);
            }

        } catch (SQLException e) {
            logger.error("SQL Error ", e);
        }
        return null;
    }

    public Note getNoteAsAdmin(UUID uuid) {
        logger.info("Getting note as admin");
        String sql = "SELECT * FROM notes WHERE uuid=?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, uuid.toString());

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Helper.resultSetToNote(resultSet);
            }

        } catch (SQLException e) {
            logger.error("SQL Error ", e);
        }
        return null;
    }

    public boolean deleteNote(User user, UUID uuid) {
        logger.info("Deleting note by user");
        String sql = "DELETE FROM notes WHERE user_id=? AND uuid=?";


        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, user.getId());
            statement.setString(2, uuid.toString());

            int rows = statement.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            logger.error("SQL Error ", e);
            return false;
        }
    }

    public boolean deleteNoteAsAdmin(UUID uuid) {
        logger.info("Deleting note as admin");
        String sql = "DELETE FROM notes WHERE uuid=?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, uuid.toString());

            int rows = statement.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            logger.error("SQL Error ", e);
            return false;
        }
    }


    public boolean existsUUID(UUID uuid) {
        logger.info("Checking if note exists by uuid");
        String sql = "SELECT uuid FROM notes WHERE uuid=?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, uuid.toString());

            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();

        } catch (SQLException e) {
            logger.error("SQL Error ", e);
            return false;
        }
    }
}


