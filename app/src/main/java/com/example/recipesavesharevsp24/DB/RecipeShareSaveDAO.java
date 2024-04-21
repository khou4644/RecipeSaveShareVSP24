package com.example.recipesavesharevsp24.DB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.recipesavesharevsp24.RecipeShareSave;
import com.example.recipesavesharevsp24.User;

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
    List<RecipeShareSave> getAllGymLogs();

    @Query("SELECT * FROM " + AppDataBase.RECIPESHARESAVE_TABLE + " Where mLogId = :logId ")
    List<RecipeShareSave> getGymLogsById(int logId);

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


}
