package com.example.recipesavesharevsp24.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipesavesharevsp24.Activities.DislikedPost;
import com.example.recipesavesharevsp24.Activities.LikedPost;
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

        // Check if the post is liked by the current user
        List<LikedPost> existingLikes = mRecipeShareSaveDAO.getLikedPostsByUserIdAndPostId(getCurrentUserId(), post.getLogId());
        boolean isLiked = !existingLikes.isEmpty();

        // Check if the post is disliked by the current user
        List<DislikedPost> existingDislikes = mRecipeShareSaveDAO.getDislikedPostsByUserIdAndPostId(getCurrentUserId(), post.getLogId());
        boolean isDisliked = !existingDislikes.isEmpty();

        // Set the initial like and dislike count texts
        holder.likeCountTextView.setText(String.valueOf(post.getLikeCount()));
        holder.dislikeCountTextView.setText(String.valueOf(post.getDislikeCount()));

        holder.likeButton.setOnClickListener(v -> {
            // Unlike the post if already liked
            if (isLiked) {
                mRecipeShareSaveDAO.deleteLikedPost(getCurrentUserId(), post.getLogId());
                post.setLikeCount(post.getLikeCount() - 1);
                holder.likeCountTextView.setText(String.valueOf(post.getLikeCount()));
            } else {
                // Like the post
                LikedPost likedPost = new LikedPost(getCurrentUserId(), post.getLogId());
                likePost(likedPost, post);
                holder.likeCountTextView.setText(String.valueOf(post.getLikeCount()));

                // Remove any existing dislike
                mRecipeShareSaveDAO.deleteDislikedPost(getCurrentUserId(), post.getLogId());
                post.setDislikeCount(post.getDislikeCount() - 1);
                holder.dislikeCountTextView.setText(String.valueOf(post.getDislikeCount()));
            }
        });

        holder.dislikeButton.setOnClickListener(v -> {
            // Undislike the post if already disliked
            if (isDisliked) {
                mRecipeShareSaveDAO.deleteDislikedPost(getCurrentUserId(), post.getLogId());
                post.setDislikeCount(post.getDislikeCount() - 1);
                holder.dislikeCountTextView.setText(String.valueOf(post.getDislikeCount()));
            } else {
                // Dislike the post
                DislikedPost dislikedPost = new DislikedPost(getCurrentUserId(), post.getLogId());
                dislikePost(dislikedPost, post);
                holder.dislikeCountTextView.setText(String.valueOf(post.getDislikeCount()));

                // Remove any existing like
                mRecipeShareSaveDAO.deleteLikedPost(getCurrentUserId(), post.getLogId());
                post.setLikeCount(post.getLikeCount() - 1);
                holder.likeCountTextView.setText(String.valueOf(post.getLikeCount()));
            }
        });
    }

    private boolean isPostLikedByUser(int postId, int userId) {
        // Query the database to check if the post is liked by the user
        List<LikedPost> likedPosts = mRecipeShareSaveDAO.getLikedPostsByUserIdAndPostId(userId, postId);
        return !likedPosts.isEmpty();
    }

    private void likePost(LikedPost likedPost, RecipeShareSave post) {
        // Check if the post is already liked by the current user
        List<LikedPost> existingLikes = mRecipeShareSaveDAO.getLikedPostsByUserIdAndPostId(getCurrentUserId(), post.getLogId());
        List<DislikedPost> existingDislikes = mRecipeShareSaveDAO.getDislikedPostsByUserIdAndPostId(getCurrentUserId(), post.getLogId());

        if (existingLikes.isEmpty() && existingDislikes.isEmpty()) {
            // Insert the liked post into the database
            mRecipeShareSaveDAO.insertLikedPost(likedPost);
            post.setLikeCount(post.getLikeCount() + 1);
        } else if (existingDislikes.isEmpty()) {
            // Post is already liked, do nothing
            return;
        } else {
            // Post is currently disliked, remove the dislike and add the like
            mRecipeShareSaveDAO.deleteDislikedPost(getCurrentUserId(), post.getLogId());
            post.setDislikeCount(post.getDislikeCount() - 1);
            mRecipeShareSaveDAO.insertLikedPost(likedPost);
            post.setLikeCount(post.getLikeCount() + 1);
        }
    }

    private void dislikePost(DislikedPost dislikedPost, RecipeShareSave post) {
        // Check if the post is already disliked by the current user
        List<DislikedPost> existingDislikes = mRecipeShareSaveDAO.getDislikedPostsByUserIdAndPostId(getCurrentUserId(), post.getLogId());
        List<LikedPost> existingLikes = mRecipeShareSaveDAO.getLikedPostsByUserIdAndPostId(getCurrentUserId(), post.getLogId());

        if (existingDislikes.isEmpty() && existingLikes.isEmpty()) {
            // Insert the disliked post into the database
            mRecipeShareSaveDAO.insertDislikedPost(dislikedPost);
            post.setDislikeCount(post.getDislikeCount() + 1);
        } else if (existingLikes.isEmpty()) {
            // Post is already disliked, do nothing
            return;
        } else {
            // Post is currently liked, remove the like and add the dislike
            mRecipeShareSaveDAO.deleteLikedPost(getCurrentUserId(), post.getLogId());
            post.setLikeCount(post.getLikeCount() - 1);
            mRecipeShareSaveDAO.insertDislikedPost(dislikedPost);
            post.setDislikeCount(post.getDislikeCount() + 1);
        }
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