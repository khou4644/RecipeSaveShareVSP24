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
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipesavesharevsp24.Activities.User;
import com.example.recipesavesharevsp24.DB.RecipeShareSaveDAO;
import com.example.recipesavesharevsp24.R;

import java.util.List;

public class AdminViewUserAdapter extends RecyclerView.Adapter<AdminViewUserAdapter.UserViewHolder> {
    private List<User> users;
    private RecipeShareSaveDAO recipeShareSaveDAO;
    private Context context;

    private OnBanClickListener onBanClickListener;

    private OnDeleteClickListener onDeleteClickListener;

    public AdminViewUserAdapter(Context context, RecipeShareSaveDAO recipeShareSaveDAO) {
        this.context = context;
        this.recipeShareSaveDAO = recipeShareSaveDAO;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

//    @Override
//    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
//        User user = users.get(position);
//        holder.usernameTextView.setText(user.getUserName());
//
//        // Set the initial button state based on the user's ban status
//        if(user.isMisBanned()){
//            holder.banButton.setText("Unban User");
//            holder.banButton.setBackgroundColor(Color.LTGRAY);
//        }else{
//            //default text already exists
//            holder.banButton.setBackgroundColor(Color.RED);
//        }
//
//        holder.banButton.setOnClickListener(v -> {
//            showBanConfirmationDialog(user.getUserId(), user.getUserName(), !user.isMisBanned());
//        });
//    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = users.get(position);
        holder.usernameTextView.setText(user.getUserName());

        // Set the initial button state based on the user's ban status
        if (user.isMisBanned()) {
            holder.banButton.setText("Unban User");
            holder.banButton.setBackgroundColor(Color.LTGRAY);
        } else {
            holder.banButton.setBackgroundColor(Color.RED);
        }


        holder.banButton.setOnClickListener(v -> {
            showBanConfirmationDialog(user, !user.isMisBanned());
        });

//        holder.banButton.setOnClickListener(v -> {
//            if (onBanClickListener != null) {
//                onBanClickListener.onBanClick(user);
//            }
//        });

        holder.deleteButton.setOnClickListener(v -> {
            showDeleteConfirmationDialog(user);
        });

//        holder.deleteButton.setOnClickListener(v -> {
//            if (onDeleteClickListener != null) {
//                onDeleteClickListener.onDeleteClick(user);
//            }
//        });

    }


    @Override
    public int getItemCount() {
        return users != null ? users.size() : 0;
    }

    public void setUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }


    public void setOnBanClickListener(OnBanClickListener listener) {
        this.onBanClickListener = listener;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.onDeleteClickListener = listener;
    }

//    public interface OnBanClickListener {
//        void onBanClick(int userId, String username, boolean isBanned);
//    }

    public interface OnBanClickListener {
        void onBanClick(User user);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(User user);
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView;
        Button banButton;
        Button deleteButton;

        UserViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            banButton = itemView.findViewById(R.id.banButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

    private void showBanConfirmationDialog(User user, boolean isBanned) {
        String dialogTitle = isBanned ? "Ban User" : "Unban User";
        String dialogMessage = isBanned ? "Are you sure you want to ban " + user.getUserName() + "?" : "Are you sure you want to unban " + user.getUserName() + "?";

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(dialogTitle)
                .setMessage(dialogMessage)
                .setPositiveButton(dialogTitle, (dialog, which) -> {
                    if (onBanClickListener != null) {
                        onBanClickListener.onBanClick(user);
                        // Update the UI after the user is banned/unbanned
                        notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showDeleteConfirmationDialog(User user) {
        String dialogMessage = "Are you sure you want to delete " + user.getUserName() + "?";

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete User")
                .setMessage(dialogMessage)
                .setPositiveButton("Delete", (dialog, which) -> {
                    if (onDeleteClickListener != null) {
                        onDeleteClickListener.onDeleteClick(user);
                        // Remove the deleted user from the list and notify the adapter
                        users.remove(user);
                        notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
//    private void showBanConfirmationDialog(int userId, String username, boolean isBanned) {
//        String dialogTitle = isBanned ? "Ban User" : "Unban User";
//        String dialogMessage = isBanned ? "Are you sure you want to ban " + username + "?" : "Are you sure you want to unban " + username + "?";
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle(dialogTitle)
//                .setMessage(dialogMessage)
//                .setPositiveButton(dialogTitle, (dialog, which) -> {
//                    if (onBanClickListener != null) {
//                        onBanClickListener.onBanClick(userId, username, isBanned);
//                    }
//                })
//                .setNegativeButton("Cancel", null)
//                .show();
//    }
}