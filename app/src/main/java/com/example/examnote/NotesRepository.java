package com.example.examnote;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NotesRepository {
    private FolderDao folderDao;
    private NoteDao noteDao;
    private ExecutorService executorService;

    public NotesRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        folderDao = database.folderDao();
        noteDao = database.noteDao();
        executorService = Executors.newFixedThreadPool(2);
    }

    public void insertFolder(Folder folder) {
        executorService.execute(() -> folderDao.insertFolder(folder));
    }

    public LiveData<List<Folder>> getAllFolders() {
        return folderDao.getAllFolders();
    }

    public void insertNote(Note note) {
        executorService.execute(() -> noteDao.insertNote(note));
    }

    public LiveData<List<Note>> getNotesByFolder(int folderId) {
        return noteDao.getNotesByFolder(folderId);
    }

    public LiveData<Note> getNoteById(int noteId) {
        return noteDao.getNoteById(noteId);
    }

    public void updateFolder(Folder folder) {
        executorService.execute(() -> folderDao.updateFolder(folder));
    }

    public void deleteFolder(Folder folder) {
        executorService.execute(() -> folderDao.deleteFolder(folder));
    }

    public void updateNote(Note note) {
        executorService.execute(() -> noteDao.updateNote(note));
    }

    public void deleteNote(Note note) {
        executorService.execute(() -> noteDao.deleteNote(note));
    }
}