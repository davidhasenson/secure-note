package repository;

import config.DatabaseConnection;
import model.Note;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NoteRepository {

    public boolean createNote(int userId, String title, String content) {
        String sql = "INSERT INTO notes (title, content, user_id) VALUES (?,?,?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, title);
            statement.setString(2, content);
            statement.setInt(3, userId);

            int rows = statement.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateNote(Note note) {
        String sql = "UPDATE notes SET title = ?, content = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, note.getTitle());
            statement.setString(2, note.getContent());
            statement.setInt(3, note.getId());

            int rows = statement.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Note> getNotesByUser(User user) {
        String sql = "SELECT * FROM notes WHERE user_id= ?";
        List<Note> notes = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, user.getId());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                notes.add(resultSetToNote(resultSet));
            }

        } catch (SQLException e) {
            System.out.println("Database error");
        }
        return notes;
    }

    public List<Note> getAllNotesAsAdmin() {
        String sql = "SELECT * FROM notes";
        List<Note> notes = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                notes.add(resultSetToNote(resultSet));
            }

        } catch (SQLException e) {
            System.out.println("Database error");
        }
        return notes;
    }

    private Note resultSetToNote(ResultSet resultSet) throws SQLException {
        Note note = new Note();
        note.setId(resultSet.getInt("id"));
        note.setTitle(resultSet.getString("title"));
        note.setContent(resultSet.getString("content"));
        note.setUserId(resultSet.getInt("user_id"));
        return note;
    }

    public Note getNote(User user, int noteId) {
        String sql;
// lägg till kontroll lager i noteservice.

        sql = "SELECT * FROM notes WHERE user_id= ? AND id=?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, user.getId());
            statement.setInt(2, noteId);


            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSetToNote(resultSet);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Note getNoteAsAdmin(int noteId) {
        String sql = "SELECT * FROM notes WHERE id=?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, noteId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSetToNote(resultSet);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean deleteNote(User user, int noteId) {
        String sql = "DELETE FROM notes WHERE user_id = ? AND id = ?";


        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, user.getId());
            statement.setInt(2, noteId);

            int rows = statement.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteNoteAsAdmin(int noteId) {
        String sql = "DELETE FROM notes WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, noteId);

            int rows = statement.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}


