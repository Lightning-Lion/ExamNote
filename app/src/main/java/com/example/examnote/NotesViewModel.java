package com.example.examnote;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

public class NotesViewModel extends AndroidViewModel {
    private NotesRepository repository;
    private LiveData<List<Folder>> allFolders;
    private LiveData<List<Note>> notesByFolder;

    public NotesViewModel(@NonNull Application application) {
        super(application);
        repository = new NotesRepository(application);
        allFolders = repository.getAllFolders();
    }

    public LiveData<List<Folder>> getAllFolders() {
        return allFolders;
    }

    public LiveData<List<Note>> getNotesByFolder(int folderId) {
        notesByFolder = repository.getNotesByFolder(folderId);
        return notesByFolder;
    }

    public LiveData<Note> getNoteById(int noteId) {
        return repository.getNoteById(noteId);
    }

    public void insertFolder(String name) {
        Folder folder = new Folder(name);
        repository.insertFolder(folder);
    }

    public void insertNote(String title, String content, int folderId, String imageUri) {
        Note note = new Note(title, content, folderId, null, imageUri);
        repository.insertNote(note);
    }

    public void updateFolder(Folder folder) {
        repository.updateFolder(folder);
    }

    public void deleteFolder(Folder folder) {
        repository.deleteFolder(folder);
    }

    public void updateNote(Note note) {
        note.setUpdatedDate(System.currentTimeMillis());
        repository.updateNote(note);
    }

    public void deleteNote(Note note) {
        repository.deleteNote(note);
    }

    // 搜索和日期范围方法
    public LiveData<List<Note>> searchNotesAscending(String query) {
        return repository.searchNotesAscending(query);
    }

    public LiveData<List<Note>> searchNotesDescending(String query) {
        return repository.searchNotesDescending(query);
    }

    public LiveData<List<Note>> getNotesByDateRange(long startDate, long endDate) {
        return repository.getNotesByDateRange(startDate, endDate);
    }
}