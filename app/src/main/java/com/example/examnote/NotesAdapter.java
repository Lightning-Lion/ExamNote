package com.example.examnote;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.app.AlertDialog;
import android.widget.EditText;
import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {
    private List<Note> notes = new ArrayList<>();
    private Context context;
    private NotesViewModel notesViewModel;

    public NotesAdapter(Context context, NotesViewModel notesViewModel) {
        this.context = context;
        this.notesViewModel = notesViewModel;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item, parent, false);
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = notes.get(position);
        if (note.getTitle().isEmpty()) {
            holder.noteTitleTextView.setText("新笔记");
        } else {
            holder.noteTitleTextView.setText(note.getTitle());
        }

        holder.itemViewButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, NoteEditActivity.class);
            intent.putExtra("noteId", note.getId());
            context.startActivity(intent);
        });

        holder.renameButton.setOnClickListener(v -> showRenameDialog(note));
        holder.deleteButton.setOnClickListener(v -> showDeleteConfirmationDialog(note));
    }

    private void showRenameDialog(Note note) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("重命名笔记");

        final EditText input = new EditText(context);
        input.setText(note.getTitle());
        builder.setView(input);

        builder.setPositiveButton("保存", (dialog, which) -> {
            String newTitle = input.getText().toString();
            if (!newTitle.isEmpty()) {
                note.setTitle(newTitle);
                notesViewModel.updateNote(note);
                notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("取消", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void showDeleteConfirmationDialog(Note note) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("删除笔记");
        builder.setMessage("确认删除吗？");
        builder.setPositiveButton("删除", (dialog, which) -> {
            notesViewModel.deleteNote(note);
            notifyDataSetChanged();
        });
        builder.setNegativeButton("不删除", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView noteTitleTextView;
        Button itemViewButton;
        Button renameButton;
        Button deleteButton;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            noteTitleTextView = itemView.findViewById(R.id.noteTitleTextView);
            itemViewButton = itemView.findViewById(R.id.itemViewButton);
            renameButton = itemView.findViewById(R.id.renameButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}