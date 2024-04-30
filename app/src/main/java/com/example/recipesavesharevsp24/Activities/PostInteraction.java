package com.example.recipesavesharevsp24.Activities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "post_interactions")
public class PostInteraction {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "user_id")
    private int userId;

    @ColumnInfo(name = "post_id")
    private int postId;

    @ColumnInfo(name = "interaction_type")
    private int interactionType; // 1 for like, -1 for dislike, 0 for neutral

    // Constructor
    public PostInteraction(int userId, int postId, int interactionType) {
        this.userId = userId;
        this.postId = postId;
        this.interactionType = interactionType;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getInteractionType() {
        return interactionType;
    }

    public void setInteractionType(int interactionType) {
        this.interactionType = interactionType;
    }
}
