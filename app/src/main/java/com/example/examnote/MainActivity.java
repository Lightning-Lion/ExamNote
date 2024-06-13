package com.example.examnote;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
        foldersAdapter = new FoldersAdapter(this, notesViewModel);
        foldersRecyclerView.setAdapter(foldersAdapter);

        notesViewModel.getAllFolders().observe(this, new Observer<List<Folder>>() {
            @Override
            public void onChanged(List<Folder> folders) {
                foldersAdapter.setFolders(folders);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}