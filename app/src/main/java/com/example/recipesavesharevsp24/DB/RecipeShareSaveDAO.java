package com.example.recipesavesharevsp24.DB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.recipesavesharevsp24.Activities.PostInteraction;
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

    @Update
    void updateRecipeShareSave(RecipeShareSave recipeShareSave);

    @Delete
    void delete(RecipeShareSave... recipeShareSaves);

    @Query("SELECT * FROM " + AppDataBase.RECIPESHARESAVE_TABLE + " ORDER BY mDate desc")
    LiveData<List<RecipeShareSave>> getAllRecipeShareSave();

    @Query("SELECT * FROM " + AppDataBase.RECIPESHARESAVE_TABLE + " WHERE mLogId = :logId")
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

    @Query("SELECT * FROM post_interactions WHERE user_id = :userId AND post_id = :postId")
    PostInteraction getPostInteractionByUserIdAndPostId(int userId, int postId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdatePostInteraction(PostInteraction postInteraction);

    @Query("DELETE FROM post_interactions WHERE user_id = :userId AND post_id = :postId")
    void deletePostInteraction(int userId, int postId);

    @Query("SELECT COUNT(*) FROM post_interactions WHERE post_id = :postId AND interaction_type = 1")
    int getLikeCount(int postId);

    @Query("SELECT COUNT(*) FROM post_interactions WHERE post_id = :postId AND interaction_type = -1")
    int getDislikeCount(int postId);

    @Query("SELECT * FROM " + AppDataBase.RECIPESHARESAVE_TABLE + " WHERE mUserId = :userId ORDER BY mDate DESC")
    List<RecipeShareSave> getPostsByUserId(int userId);

    @Query("SELECT * FROM " + AppDataBase.RECIPESHARESAVE_TABLE + " WHERE mUserId = :userId ORDER BY mDate DESC")
    LiveData<List<RecipeShareSave>> getPostsByUserIdLiveData(int userId);

    @Query("SELECT * FROM " + AppDataBase.RECIPESHARESAVE_TABLE + " WHERE mLogId IN (SELECT post_id FROM post_interactions WHERE user_id = :userId AND interaction_type = 1)")
    List<RecipeShareSave> getLikedPostsByUserId(int userId);

    @Query("DELETE FROM post_interactions WHERE user_id = :userId AND post_id = :postId")
    void removeLike(int userId, int postId);
}
