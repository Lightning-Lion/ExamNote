package com.example.examnote;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NoteDao {
    @Insert
    void insertNote(Note note);

    @Query("SELECT * FROM notes WHERE folderId = :folderId")
    LiveData<List<Note>> getNotesByFolder(int folderId);

    @Query("SELECT * FROM notes WHERE id = :noteId")
    LiveData<Note> getNoteById(int noteId);

    @Update
    void updateNote(Note note);

    @Delete
    void deleteNote(Note note);
}