package com.example.examnote;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private NotesViewModel notesViewModel;
    private FoldersAdapter foldersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notesViewModel = new ViewModelProvider(this).get(NotesViewModel.class);

        Button addFolderButton = findViewById(R.id.addFolderButton);
        addFolderButton.setOnClickListener(v -> {
            notesViewModel.insertFolder("New Folder");
        });

        RecyclerView foldersRecyclerView = findViewById(R.id.foldersRecyclerView);
        foldersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        foldersAdapter = new FoldersAdapter(this, notesViewModel); // 传递Context和NotesViewModel
        foldersRecyclerView.setAdapter(foldersAdapter);

        notesViewModel.getAllFolders().observe(this, new Observer<List<Folder>>() {
            @Override
            public void onChanged(List<Folder> folders) {
                foldersAdapter.setFolders(folders);
            }
        });
    }
}