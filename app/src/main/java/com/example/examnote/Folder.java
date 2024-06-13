package com.example.examnote;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "folders")
public class Folder {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;

    // Constructors, getters, and setters
    public Folder(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}