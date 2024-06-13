package com.example.examnote;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FolderDao {
    @Insert
    void insertFolder(Folder folder);

    @Query("SELECT * FROM folders")
    LiveData<List<Folder>> getAllFolders();

    @Update
    void updateFolder(Folder folder);

    @Delete
    void deleteFolder(Folder folder);
}