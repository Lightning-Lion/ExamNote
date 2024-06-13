package com.example.examnote;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class FoldersAdapter extends RecyclerView.Adapter<FoldersAdapter.FolderViewHolder> {
    private List<Folder> folders = new ArrayList<>();
    private Context context;
    private NotesViewModel notesViewModel;

    public FoldersAdapter(Context context, NotesViewModel notesViewModel) {
        this.context = context;
        this.notesViewModel = notesViewModel;
    }

    @NonNull
    @Override
    public FolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.folder_item, parent, false);
        return new FolderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderViewHolder holder, int position) {
        Folder folder = folders.get(position);
        holder.folderNameTextView.setText(folder.getName());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, NotesActivity.class);
            intent.putExtra("folderId", folder.getId());
            context.startActivity(intent);
        });

        holder.renameButton.setOnClickListener(v -> showRenameDialog(folder));

        holder.deleteButton.setOnClickListener(v -> showDeleteConfirmationDialog(folder));
    }

    private void showRenameDialog(Folder folder) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("重命名笔记夹");

        final EditText input = new EditText(context);
        input.setText(folder.getName());
        builder.setView(input);

        builder.setPositiveButton("保存", (dialog, which) -> {
            String newName = input.getText().toString();
            if (!newName.isEmpty()) {
                folder.setName(newName);
                notesViewModel.updateFolder(folder);
                notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("取消", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void showDeleteConfirmationDialog(Folder folder) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("删除笔记夹");
        builder.setMessage("确认删除吗？");
        builder.setPositiveButton("删除", (dialog, which) -> {
            notesViewModel.deleteFolder(folder);
            notifyDataSetChanged();
        });
        builder.setNegativeButton("不删除", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    @Override
    public int getItemCount() {
        return folders.size();
    }

    public void setFolders(List<Folder> folders) {
        this.folders = folders;
        notifyDataSetChanged();
    }

    static class FolderViewHolder extends RecyclerView.ViewHolder {
        TextView folderNameTextView;
        Button renameButton;
        Button deleteButton;

        public FolderViewHolder(@NonNull View itemView) {
            super(itemView);
            folderNameTextView = itemView.findViewById(R.id.folderNameTextView);
            renameButton = itemView.findViewById(R.id.renameButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}