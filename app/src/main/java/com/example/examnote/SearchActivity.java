package com.example.examnote;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {
    private NotesViewModel notesViewModel;
    private NotesAdapter notesAdapter;
    private EditText searchEditText;
    private EditText startDateEditText;
    private EditText endDateEditText;
    private RadioGroup sortRadioGroup;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        notesViewModel = new ViewModelProvider(this).get(NotesViewModel.class);

        searchEditText = findViewById(R.id.searchEditText);
        startDateEditText = findViewById(R.id.startDateEditText);
        endDateEditText = findViewById(R.id.endDateEditText);
        sortRadioGroup = findViewById(R.id.sortRadioGroup);
        searchButton = findViewById(R.id.searchButton);

        RecyclerView notesRecyclerView = findViewById(R.id.notesRecyclerView);
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        notesAdapter = new NotesAdapter(this, notesViewModel);
        notesRecyclerView.setAdapter(notesAdapter);

        searchButton.setOnClickListener(v -> performSearch());
    }

    private void performSearch() {
        String query = searchEditText.getText().toString().trim();
        String startDateStr = startDateEditText.getText().toString().trim();
        String endDateStr = endDateEditText.getText().toString().trim();
        boolean ascending = ((RadioButton) findViewById(sortRadioGroup.getCheckedRadioButtonId())).getText().toString().equals("Ascending");

        long startDate = 0;
        long endDate = Long.MAX_VALUE;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
if (query.isEmpty() ) {
    Toast.makeText(this,"请输入搜索关键词",Toast.LENGTH_SHORT).show();

        }
        if (!TextUtils.isEmpty(startDateStr)) {
            try {
                Date date = sdf.parse(startDateStr);
                if (date != null) {
                    startDate = date.getTime();
                }
            } catch (ParseException e) {
                Toast.makeText(this, "日期格式需要是yyyy-MM-dd", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (!TextUtils.isEmpty(endDateStr)) {
            try {
                Date date = sdf.parse(endDateStr);
                if (date != null) {
                    endDate = date.getTime();
                }
            } catch (ParseException e) {
                Toast.makeText(this, "日期格式需要是yyyy-MM-dd", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (!TextUtils.isEmpty(query)) {
            if (ascending) {
                long finalStartDate = startDate;
                long finalEndDate = endDate;
                notesViewModel.searchNotesAscending("%" + query + "%").observe(this, new Observer<List<Note>>() {
                    @Override
                    public void onChanged(List<Note> notes) {
                        notesAdapter.setNotes(filterNotesByDateRange(notes, finalStartDate, finalEndDate));
                    }
                });
            } else {
                long finalStartDate1 = startDate;
                long finalEndDate1 = endDate;
                notesViewModel.searchNotesDescending("%" + query + "%").observe(this, new Observer<List<Note>>() {
                    @Override
                    public void onChanged(List<Note> notes) {
                        notesAdapter.setNotes(filterNotesByDateRange(notes, finalStartDate1, finalEndDate1));
                    }
                });
            }
        } else {
            notesViewModel.getNotesByDateRange(startDate, endDate).observe(this, new Observer<List<Note>>() {
                @Override
                public void onChanged(List<Note> notes) {
                    notesAdapter.setNotes(notes);
                }
            });
        }
    }

    private List<Note> filterNotesByDateRange(List<Note> notes, long startDate, long endDate) {
        for (int i = notes.size() - 1; i >= 0; i--) {
            Note note = notes.get(i);
            if (note.getCreatedDate() < startDate || note.getCreatedDate() > endDate) {
                notes.remove(i);
            }
        }
        return notes;
    }
}