package com.example.recipesavesharevsp24.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipesavesharevsp24.Activities.EditMyPostActivity;
import com.example.recipesavesharevsp24.Activities.PostInteraction;
import com.example.recipesavesharevsp24.Activities.RecipeShareSave;
import com.example.recipesavesharevsp24.Activities.User;
import com.example.recipesavesharevsp24.DB.RecipeShareSaveDAO;
import com.example.recipesavesharevsp24.R;

import java.util.List;

public class                                    MyPostAdapter extends RecyclerView.Adapter<MyPostAdapter.PostViewHolder> {

    private List<RecipeShareSave> mPostList;
    private RecipeShareSaveDAO mRecipeShareSaveDAO;

    private Context mContext;

    private SharedPreferences mPreferences;

    private static final String USER_ID_KEY = "com.example.recipesavesharevsp24.userIdKey";
    private static final String PREFERENCES_KEY = "com.example.recipesavesharevsp24.PREFERENCES_KEY";
    private RecipeShareSave post;


    public MyPostAdapter(Context context, List<RecipeShareSave> postList, RecipeShareSaveDAO recipeShareSaveDAO) {
        mContext = context;
        mPostList = postList;
        mRecipeShareSaveDAO = recipeShareSaveDAO;
        mPreferences = mContext.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_item_post, parent, false);
        return new PostViewHolder(view);
    }

    public interface OnEditClickListener {
        void onEditClick(int postId);
    }

    private OnEditClickListener mOnEditClickListener;

    public void setOnEditClickListener(OnEditClickListener listener) {
        mOnEditClickListener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        RecipeShareSave post = mPostList.get(position);
        User user = mRecipeShareSaveDAO.getUserByUserId(post.getUserId());
        String username = user != null ? user.getUserName() : "Unknown User";
        holder.postTextView.setText("Username: " + username + "\n" + post);

        // Get the current user's interaction with the post
        PostInteraction postInteraction = mRecipeShareSaveDAO.getPostInteractionByUserIdAndPostId(getCurrentUserId(), post.getLogId());
        final int[] interactionType = {postInteraction != null ? postInteraction.getInteractionType() : 0};

        // Set the initial like and dislike count texts
        //updateLikeDislikeCounts(holder, post);

        // Set the initial state of the like button
//        holder.likeButton.setBackgroundColor(interactionType[0] == 1 ? ContextCompat.getColor(mContext, R.color.liked_color) : ContextCompat.getColor(mContext, R.color.default_color));
//
//        holder.likeButton.setOnClickListener(v -> {
//            if (interactionType[0] == 1) {
//                // User already liked the post, so remove the like
//                mRecipeShareSaveDAO.deletePostInteraction(getCurrentUserId(), post.getLogId());
//                interactionType[0] = 0;
//                holder.likeButton.setSelected(false);
//            } else {
//                // User hasn't liked the post yet or previously disliked it, so add a like
//                PostInteraction interaction = new PostInteraction(getCurrentUserId(), post.getLogId(), 1);
//                mRecipeShareSaveDAO.insertOrUpdatePostInteraction(interaction);
//                interactionType[0] = 1;
//                holder.likeButton.setSelected(true);
//            }
//            //updateLikeDislikeCounts(holder, post);
//            holder.likeButton.setBackgroundColor(interactionType[0] == 1 ? ContextCompat.getColor(mContext, R.color.liked_color) : ContextCompat.getColor(mContext, R.color.default_color));
//        });

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


        holder.editButton.setOnClickListener(v -> {
            // Start the EditMyPostActivity and pass the post data
            Intent intent = new Intent(mContext, EditMyPostActivity.class);
            intent.putExtra("postId", post.getLogId());
            mContext.startActivity(intent);
        });

        holder.editButton.setOnClickListener(v -> {
            if (mOnEditClickListener != null) {
                mOnEditClickListener.onEditClick(post.getLogId());
            }
        });
    }



//    private void updateLikeDislikeCounts(PostViewHolder holder, RecipeShareSave post) {
//        int likeCount = mRecipeShareSaveDAO.getLikeCount(post.getLogId());
//        int dislikeCount = mRecipeShareSaveDAO.getDislikeCount(post.getLogId());
//        holder.likeCountTextView.setText(String.valueOf(likeCount));
//        holder.dislikeCountTextView.setText(String.valueOf(dislikeCount));
//    }

    private int getCurrentUserId() {
        // Get the current user's ID (implement this based on your authentication system)
        // For example, you can retrieve it from SharedPreferences or a logged-in user object
        return mPreferences.getInt(USER_ID_KEY, -1);
    }

    @Override
    public int getItemCount() {
        return mPostList.size();
    }

    public void setPosts(List<RecipeShareSave> postList) {
        mPostList = postList;
        notifyDataSetChanged();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        Button likeButton;
        Button deleteButton;
        TextView likeCountTextView;
        TextView postTextView;
        Button editButton;

        PostViewHolder(@NonNull View itemView) {
            super(itemView);
            likeButton = itemView.findViewById(R.id.likeButton); // Initialize likeButton
            deleteButton = itemView.findViewById(R.id.deleteButton);
            likeCountTextView = itemView.findViewById(R.id.likeCountTextView);
            postTextView = itemView.findViewById(R.id.postTextView);
            editButton = itemView.findViewById(R.id.editButton);

            // Check if likeButton is null after initialization
            if (likeButton == null) {
                // Handle the case when likeButton is null (e.g., remove it from the layout)
            }
        }
    }
}