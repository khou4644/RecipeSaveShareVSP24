package com.example.recipesavesharevsp24.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipesavesharevsp24.Activities.RecipeShareSave;
import com.example.recipesavesharevsp24.Activities.User;
import com.example.recipesavesharevsp24.DB.RecipeShareSaveDAO;
import com.example.recipesavesharevsp24.R;

import java.util.List;

public class AdminViewPostAdapter extends RecyclerView.Adapter<AdminViewPostAdapter.AdminViewPostViewHolder> {

    private List<RecipeShareSave> mPostList;
    private RecipeShareSaveDAO mRecipeShareSaveDAO;
    private Context mContext;

    public AdminViewPostAdapter(Context context, RecipeShareSaveDAO recipeShareSaveDAO) {
        mContext = context;
        mRecipeShareSaveDAO = recipeShareSaveDAO;
    }

    @NonNull
    @Override
    public AdminViewPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_post, parent, false);
        return new AdminViewPostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminViewPostViewHolder holder, int position) {
        RecipeShareSave post = mPostList.get(position);
        User user = mRecipeShareSaveDAO.getUserByUserId(post.getUserId());
        String username = user != null ? user.getUserName() : "Unknown User";
        holder.postTextView.setText("Username: " + username + "\n" + post);
        // Set the reported status text
        holder.reportedTextView.setText(post.isReported() ? "REPORTED POST" : "");
        if (post.isReported()) {
            holder.reportButton.setText("Undo report");
            holder.reportButton.setBackgroundColor(Color.LTGRAY);
        } else {
            holder.reportButton.setText("Report");
            holder.reportButton.setBackgroundColor(Color.RED);
        }

        // Show or hide the "Reported" text based on the post's reported status
        holder.reportedTextView.setVisibility(post.isReported() ? View.VISIBLE : View.GONE);

        holder.editButton.setOnClickListener(v -> {
            // Handle edit button click
        });

        holder.deleteButton.setOnClickListener(v -> {
            // Handle delete button click
        });

        holder.reportButton.setOnClickListener(v -> {
            boolean currentlyReported = post.isReported();
            boolean newReportedStatus = !currentlyReported;

            // Update the reported status in the database
            mRecipeShareSaveDAO.updateReportedStatus(post.getLogId(), newReportedStatus);

            // Update the UI
            post.setReported(newReportedStatus);
            holder.reportedTextView.setVisibility(newReportedStatus ? View.VISIBLE : View.GONE);
        });
    }

    @Override
    public int getItemCount() {
        return mPostList != null ? mPostList.size() : 0;
    }

    public void setPosts(List<RecipeShareSave> postList) {
        mPostList = postList;
        notifyDataSetChanged();
    }

    static class AdminViewPostViewHolder extends RecyclerView.ViewHolder {
        TextView postTextView;
        Button editButton;
        Button deleteButton;
        Button reportButton;
        TextView reportedTextView;


        AdminViewPostViewHolder(@NonNull View itemView) {
            super(itemView);
            postTextView = itemView.findViewById(R.id.postTextView);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            reportButton = itemView.findViewById(R.id.reportButton);
            reportedTextView = itemView.findViewById(R.id.reportedTextView);
        }
    }
}