package com.example.recipesavesharevsp24.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.recipesavesharevsp24.DB.AppDataBase;
import com.example.recipesavesharevsp24.DB.RecipeShareSaveDAO;
import com.example.recipesavesharevsp24.R;
import com.example.recipesavesharevsp24.RecyclerView.PostAdapter;

import java.util.ArrayList;
import java.util.List;

public class LikedPostActivity extends AppCompatActivity {

    private static final String PREFERENCES_KEY = "com.example.recipesavesharevsp24.PREFERENCES_KEY";

    private static final String USER_ID_KEY = "com.example.recipesavesharevsp24.userIdKey";
    private RecyclerView mLikedPostRecyclerView;

    private PostAdapter mPostAdapter;
    private RecipeShareSaveDAO mRecipeShareSaveDAO;
    private SharedPreferences mPreferences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liked_post);
        getPrefs();
        mRecipeShareSaveDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .RecipeShareSaveDAO();
        mLikedPostRecyclerView = findViewById(R.id.likedPostRecyclerView);
        mLikedPostRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mPostAdapter = new PostAdapter(this, new ArrayList<>(), mRecipeShareSaveDAO);
        mLikedPostRecyclerView.setAdapter(mPostAdapter);

        // Get the current user's ID from shared preferences
        int userId = mPreferences.getInt(USER_ID_KEY, -1);

        List<LikedPost> likedPosts;
        List<RecipeShareSave> likedRecipes = new ArrayList<>();

        try {
            likedPosts = mRecipeShareSaveDAO.getLikedPostsByUserId(userId);

            for (LikedPost likedPost : likedPosts) {
                List<RecipeShareSave> recipe = mRecipeShareSaveDAO.getLikedRecipeShareSaveById(likedPost.getPostId());
                if (recipe != null) {
                    likedRecipes.add((RecipeShareSave) recipe);
                }
            }

            mPostAdapter.setPosts(likedRecipes);
        } catch (Exception e) {
            Log.e("LikedPostActivity", "Error retrieving liked posts and recipes", e);
            // Handle the error, e.g., show a error message to the user
        }
    }

    private void getPrefs() {
        mPreferences = this.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
    }
}