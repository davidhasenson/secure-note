package model;

import java.util.UUID;

public class Note {

    private int id;
    private String title;
    private String content;
    private int userId;
    private UUID uuid;

    public Note() {
    }

    public Note(int id, String title, String content, int userId,  UUID uuid) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.userId = userId;
        this.uuid = uuid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public UUID getUUID() {
        return uuid;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "UUID: " + this.uuid + "\n" +
                "Title: " + this.title + "\n" +
                "Content: " + this.content + "\n";
    }
}
