package com.example.examnote;


import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NotesActivity extends AppCompatActivity {
    private NotesViewModel notesViewModel;
    private NotesAdapter notesAdapter;
    private int folderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        folderId = getIntent().getIntExtra("folderId", -1);

        notesViewModel = new ViewModelProvider(this).get(NotesViewModel.class);

        Button addNoteButton = findViewById(R.id.addNoteButton);
        addNoteButton.setOnClickListener(v -> {
            notesViewModel.insertNote("New Note", "Note content", folderId,"");
        });

        RecyclerView notesRecyclerView = findViewById(R.id.notesRecyclerView);
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        notesAdapter = new NotesAdapter(this, notesViewModel);
        notesRecyclerView.setAdapter(notesAdapter);

        notesViewModel.getNotesByFolder(folderId).observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                notesAdapter.setNotes(notes);
            }
        });
    }


}