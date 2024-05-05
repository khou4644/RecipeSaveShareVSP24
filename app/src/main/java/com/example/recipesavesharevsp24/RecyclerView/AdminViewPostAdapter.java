package com.example.recipesavesharevsp24.RecyclerView;
import android.app.AlertDialog;
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

public class AdminViewPostAdapter extends RecyclerView.Adapter<AdminViewPostAdapter.PostViewHolder> {

    private List<RecipeShareSave> mPostList;
    private final RecipeShareSaveDAO mRecipeShareSaveDAO;
    private final Context mContext;
    private OnDeleteClickListener mOnDeleteClickListener;
    private OnEditClickListener mOnEditClickListener;

    public AdminViewPostAdapter(Context context, RecipeShareSaveDAO recipeShareSaveDAO) {
        mContext = context;
        mRecipeShareSaveDAO = recipeShareSaveDAO;

    }

    public void setOnEditClickListener(OnEditClickListener listener) {
        mOnEditClickListener = listener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
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


        //EditAnyPostFragment
        holder.editButton.setOnClickListener(v -> {
            if (mOnEditClickListener != null) {
                int postId = mPostList.get(position).getLogId();
                mOnEditClickListener.onEditClick(postId);
            }
        });

        if (holder.deleteButton != null) {
            holder.deleteButton.setOnClickListener(v -> {
                // Delete the post from the database
                mRecipeShareSaveDAO.delete(post);

                // Remove the post from the list and notify the adapter
                mPostList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, mPostList.size());
            });
        }

        holder.deleteButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Delete Post");
            builder.setMessage("Are you sure you want to delete this post?");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                // Delete the post from the database
                mRecipeShareSaveDAO.delete(post);

                // Remove the post from the list and notify the adapter
                mPostList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, mPostList.size());
            });
            builder.setNegativeButton("No", (dialog, which) -> {
                // Do nothing
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
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

    public interface OnEditClickListener {
        void onEditClick(int postId);
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        mOnDeleteClickListener = listener;
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(RecipeShareSave post);
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        final TextView postTextView;
        final Button editButton;
        final Button deleteButton;
        Button reportButton;
        TextView reportedTextView;

        PostViewHolder(@NonNull View itemView) {
            super(itemView);
            postTextView = itemView.findViewById(R.id.postTextView);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            reportButton = itemView.findViewById(R.id.reportButton);
            reportedTextView = itemView.findViewById(R.id.reportedTextView);
        }
    }
}