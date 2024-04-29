package com.example.recipesavesharevsp24.DB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.recipesavesharevsp24.Activities.DislikedPost;
import com.example.recipesavesharevsp24.Activities.LikedPost;
import com.example.recipesavesharevsp24.Activities.RecipeShareSave;
import com.example.recipesavesharevsp24.Activities.User;

import java.util.List;

@Dao
public interface RecipeShareSaveDAO {
    @Insert
    void insert(RecipeShareSave recipeShareSave);
    @Insert
    void insert(RecipeShareSave... recipeShareSaves);

    @Update
    void update(RecipeShareSave... recipeShareSaves);

    @Delete
    void delete(RecipeShareSave... recipeShareSaves);

    @Query("SELECT * FROM " + AppDataBase.RECIPESHARESAVE_TABLE + " ORDER BY mDate desc")
    LiveData<List<RecipeShareSave>> getAllRecipeShareSave();

    @Query("SELECT * FROM " + AppDataBase.RECIPESHARESAVE_TABLE + " Where mLogId = :logId ")
    List<RecipeShareSave> getRecipeShareSaveById(int logId);

    @Query("SELECT * FROM " + AppDataBase.RECIPESHARESAVE_TABLE + " Where mLogId = :logId LIMIT 1")
    List<RecipeShareSave> getLikedRecipeShareSaveById(int logId);

    @Query("SELECT * FROM " + AppDataBase.RECIPESHARESAVE_TABLE + " Where mUserId = :userId ORDER BY mDate desc")
    List<RecipeShareSave> getRecipeShareSaveByUserId(int userId);

    @Query("SELECT mIsAdmin FROM " + AppDataBase.USER_TABLE + " WHERE mUserId = :userId")
    boolean isUserAdmin(int userId);

    @Insert
    void insert(User...users);

    @Update
    void update(User...users);

    @Delete
    void delete(User...users);

    @Query("SELECT * FROM " + AppDataBase.USER_TABLE)
    List<User> getAllUsers();

    @Query("SELECT * FROM " + AppDataBase.USER_TABLE + " Where mUserName = :username")
    User getUserByUsername(String username);

    @Query("SELECT * FROM " + AppDataBase.USER_TABLE + " Where mUserId = :userId")
    User getUserByUserId(int userId);

    @Insert
    void insertLikedPost(LikedPost likedPost);

    @Query("SELECT * FROM liked_posts WHERE user_id = :userId")
    List<LikedPost> getLikedPostsByUserId(int userId);


    @Query("SELECT * FROM liked_posts WHERE user_id = :userId AND post_id = :postId")
    List<LikedPost> getLikedPostsByUserIdAndPostId(int userId, int postId);

    @Query("DELETE FROM liked_posts WHERE user_id = :userId AND post_id = :postId")
    void deleteLikedPost(int userId, int postId);

    @Insert
    void insertDislikedPost(DislikedPost dislikedPost);

    @Query("SELECT * FROM disliked_posts WHERE user_id = :userId AND post_id = :postId")
    List<DislikedPost> getDislikedPostsByUserIdAndPostId(int userId, int postId);

    @Query("DELETE FROM disliked_posts WHERE user_id = :userId AND post_id = :postId")
    void deleteDislikedPost(int userId, int postId);
}
