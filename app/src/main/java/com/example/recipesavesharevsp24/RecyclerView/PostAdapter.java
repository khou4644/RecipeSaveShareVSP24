package com.example.recipesavesharevsp24.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipesavesharevsp24.Activities.PostInteraction;
import com.example.recipesavesharevsp24.Activities.RecipeShareSave;
import com.example.recipesavesharevsp24.Activities.User;
import com.example.recipesavesharevsp24.DB.RecipeShareSaveDAO;
import com.example.recipesavesharevsp24.R;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<RecipeShareSave> mPostList;
    private RecipeShareSaveDAO mRecipeShareSaveDAO;

    private Context mContext;

    private SharedPreferences mPreferences;

    private static final String USER_ID_KEY = "com.example.recipesavesharevsp24.userIdKey";
    private static final String PREFERENCES_KEY = "com.example.recipesavesharevsp24.PREFERENCES_KEY";
    private RecipeShareSave post;


    public PostAdapter(Context context, List<RecipeShareSave> postList, RecipeShareSaveDAO recipeShareSaveDAO) {
        mContext = context;
        mPostList = postList;
        mRecipeShareSaveDAO = recipeShareSaveDAO;
        mPreferences = mContext.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
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

        //new variable to keep track of the previous interaction type:
        final int[] previousInteractionType = {interactionType[0]};

        // Set the initial like and dislike count texts
        updateLikeDislikeCounts(holder, post);

        // Set the initial state of the like and dislike buttons
        holder.likeButton.setBackgroundColor(interactionType[0] == 1 ? ContextCompat.getColor(mContext, R.color.liked_color) : ContextCompat.getColor(mContext, R.color.default_color));
        holder.dislikeButton.setBackgroundColor(interactionType[0] == -1 ? ContextCompat.getColor(mContext, R.color.disliked_color) : ContextCompat.getColor(mContext, R.color.default_color));

        holder.likeButton.setOnClickListener(v -> {
            if (interactionType[0] == 1) {
                // User already liked the post, so remove the like
                mRecipeShareSaveDAO.deletePostInteraction(getCurrentUserId(), post.getLogId());
                interactionType[0] = 0;
                holder.likeButton.setSelected(false);
            } else if (interactionType[0] == -1) {
                // User previously disliked the post, so remove the dislike and add a like
                mRecipeShareSaveDAO.deletePostInteraction(getCurrentUserId(), post.getLogId());
                PostInteraction interaction = new PostInteraction(getCurrentUserId(), post.getLogId(), 1);
                mRecipeShareSaveDAO.insertOrUpdatePostInteraction(interaction);
                interactionType[0] = 1;
                holder.likeButton.setSelected(true);
                holder.dislikeButton.setSelected(false);
            } else {
                // User hasn't liked or disliked the post yet, so add a like
                PostInteraction interaction = new PostInteraction(getCurrentUserId(), post.getLogId(), 1);
                mRecipeShareSaveDAO.insertOrUpdatePostInteraction(interaction);
                interactionType[0] = 1;
                holder.likeButton.setSelected(true);
            }

            updateLikeDislikeCounts(holder, post);

            // Update the button colors
            holder.likeButton.setBackgroundColor(interactionType[0] == 1 ? ContextCompat.getColor(mContext, R.color.liked_color) : ContextCompat.getColor(mContext, R.color.default_color));
            holder.dislikeButton.setBackgroundColor(interactionType[0] == -1 ? ContextCompat.getColor(mContext, R.color.disliked_color) : ContextCompat.getColor(mContext, R.color.default_color));
        });

        holder.dislikeButton.setOnClickListener(v -> {
            if (interactionType[0] == -1) {
                // User already disliked the post, so remove the dislike
                mRecipeShareSaveDAO.deletePostInteraction(getCurrentUserId(), post.getLogId());
                interactionType[0] = 0;
                holder.dislikeButton.setSelected(false);
            } else if (interactionType[0] == 1) {
                // User previously liked the post, so remove the like and add a dislike
                mRecipeShareSaveDAO.deletePostInteraction(getCurrentUserId(), post.getLogId());
                PostInteraction interaction = new PostInteraction(getCurrentUserId(), post.getLogId(), -1);
                mRecipeShareSaveDAO.insertOrUpdatePostInteraction(interaction);
                interactionType[0] = -1;
                holder.dislikeButton.setSelected(true);
                holder.likeButton.setSelected(false);
            } else {
                // User hasn't liked or disliked the post yet, so add a dislike
                PostInteraction interaction = new PostInteraction(getCurrentUserId(), post.getLogId(), -1);
                mRecipeShareSaveDAO.insertOrUpdatePostInteraction(interaction);
                interactionType[0] = -1;
                holder.dislikeButton.setSelected(true);
            }

            updateLikeDislikeCounts(holder, post);
            // Update the button colors
            holder.likeButton.setBackgroundColor(interactionType[0] == 1 ? ContextCompat.getColor(mContext, R.color.liked_color) : ContextCompat.getColor(mContext, R.color.default_color));
            holder.dislikeButton.setBackgroundColor(interactionType[0] == -1 ? ContextCompat.getColor(mContext, R.color.disliked_color) : ContextCompat.getColor(mContext, R.color.default_color));
        });
    }

    private void updateLikeDislikeCounts(PostViewHolder holder, RecipeShareSave post) {
        int likeCount = mRecipeShareSaveDAO.getLikeCount(post.getLogId());
        int dislikeCount = mRecipeShareSaveDAO.getDislikeCount(post.getLogId());
        holder.likeCountTextView.setText(String.valueOf(likeCount));
        holder.dislikeCountTextView.setText(String.valueOf(dislikeCount));
    }

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
        Button dislikeButton;
        TextView likeCountTextView;
        TextView dislikeCountTextView;
        TextView postTextView;

        PostViewHolder(@NonNull View itemView) {
            super(itemView);
            likeButton = itemView.findViewById(R.id.likeButton);
            dislikeButton = itemView.findViewById(R.id.dislikeButton);
            likeCountTextView = itemView.findViewById(R.id.likeCountTextView);
            dislikeCountTextView = itemView.findViewById(R.id.dislikeCountTextView);
            postTextView = itemView.findViewById(R.id.postTextView);
        }
    }
}