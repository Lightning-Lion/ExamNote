package com.example.examnote;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "notes",
        foreignKeys = @ForeignKey(entity = Folder.class,
                parentColumns = "id",
                childColumns = "folderId",
                onDelete = ForeignKey.CASCADE),
        indices = {@Index(value = "folderId")})
public class Note {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String content;
    private int folderId;
    private String location;
    private String imageUri;
    private long createdDate;  // 添加字段
    private long updatedDate;  // 添加字段

    // Constructors, getters, and setters
    public Note(String title, String content, int folderId, String location, String imageUri) {
        this.title = title;
        this.content = content;
        this.folderId = folderId;
        this.location = location;
        this.imageUri = imageUri;
        this.createdDate = System.currentTimeMillis();
        this.updatedDate = System.currentTimeMillis();
    }

    // Getters and Setters
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
        this.updatedDate = System.currentTimeMillis();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        this.updatedDate = System.currentTimeMillis();
    }

    public int getFolderId() {
        return folderId;
    }

    public void setFolderId(int folderId) {
        this.folderId = folderId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
        this.updatedDate = System.currentTimeMillis();
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
        this.updatedDate = System.currentTimeMillis();
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public long getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(long updatedDate) {
        this.updatedDate = updatedDate;
    }
}