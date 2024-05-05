package com.example.recipesavesharevsp24.Activities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.example.recipesavesharevsp24.DB.AppDataBase;


import java.util.Date;

@Entity(tableName = AppDataBase.RECIPESHARESAVE_TABLE)
public class RecipeShareSave {

    @PrimaryKey(autoGenerate = true)
    private int mLogId;

    private String mRecipe;
    private int mServes;
    private String mIngredients;

    private Date mDate;

    private int mUserId;

    private boolean isReported;

    private String reportReason;


    // Default constructor
    public RecipeShareSave() {
        this.isReported = false; // Set the default value to false
        this.reportReason = ""; // Set the default value to an empty string
    }

    // Constructor with all fields
    public RecipeShareSave(String recipe, int serves, String ingredients, int userId, boolean isReported, String reportReason) {
        this.mRecipe = recipe;
        this.mServes = serves;
        this.mIngredients = ingredients;
        this.mUserId = userId;
        mDate = new Date();
        this.isReported = isReported;
        this.reportReason = reportReason;
    }

    // Constructor without the isReported and reportReason fields (for backward compatibility)
    public RecipeShareSave(String recipe, int serves, String ingredients, int userId) {
        this.mRecipe = recipe;
        this.mServes = serves;
        this.mIngredients = ingredients;
        this.mUserId = userId;
        mDate = new Date();
        this.isReported = false; // Set the default value to false
        this.reportReason = ""; // Set the default value to an empty string
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

    @Override
    public String toString() {
        return "Post# " + mLogId + "\n" +
                "Recipe: " + mRecipe + "\n" +
                "Serves: " + mServes + "\n" +
                "Ingredients: " + "\n" + mIngredients + "\n" +
                "Date: " + mDate + "\n" +
                "User ID: " + mUserId + "\n" +
                "=-=-=-=-=-=-\n";
    }

    public int getLogId() {
        return mLogId;
    }

    public void setLogId(int logId) {
        mLogId = logId;
    }

    public String getRecipe() {
        return mRecipe;
    }

    public void setRecipe(String exercise) {
        mRecipe = exercise;
    }

    public int getServes() {
        return mServes;
    }

    public void setServes(int serves) {
        mServes = serves;
    }

    public String getIngredients() {
        return mIngredients;
    }

    public void setIngredients(String mIngredients) {
        this.mIngredients = mIngredients;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isReported() {
        return isReported;
    }

    public void setReported(boolean reported) {
        isReported = reported;
    }

    public String getReportReason() {
        return reportReason;
    }

    public void setReportReason(String reportReason) {
        this.reportReason = reportReason;
    }

}
