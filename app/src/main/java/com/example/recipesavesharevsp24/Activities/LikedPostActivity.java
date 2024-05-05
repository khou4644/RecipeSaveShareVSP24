package com.example.recipesavesharevsp24.Activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
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
    private static final String USER_ID_KEY = "com.example.recipesavesharevsp24.userIdKey";
    private static final String PREFERENCES_KEY = "com.example.recipesavesharevsp24.PREFERENCES_KEY";
    private RecyclerView mLikedPostRecyclerView;
    private PostAdapter mPostAdapter;
    private RecipeShareSaveDAO mRecipeShareSaveDAO;
    private SharedPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liked_post);

        mPreferences = getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
        mRecipeShareSaveDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .RecipeShareSaveDAO();

        mLikedPostRecyclerView = findViewById(R.id.likedPostRecyclerView);
        mLikedPostRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mPostAdapter = new PostAdapter(this, new ArrayList<>(), mRecipeShareSaveDAO);
        mLikedPostRecyclerView.setAdapter(mPostAdapter);

        loadLikedPosts();
    }

    private void loadLikedPosts() {
        int currentUserId = mPreferences.getInt(USER_ID_KEY, -1);
        List<RecipeShareSave> likedPosts = mRecipeShareSaveDAO.getLikedPostsByUserId(currentUserId);
        mPostAdapter.setPosts(likedPosts);
    }

    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.layout.liked_posts_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.remove_like) {
            showRemoveLikeDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showRemoveLikeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Remove Like");
        builder.setMessage("Are you sure you want to remove the like from this post?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            // Remove the like from the post
            int currentUserId = mPreferences.getInt(USER_ID_KEY, -1);
            RecipeShareSave post = mPostAdapter.getSelectedPost();
            mRecipeShareSaveDAO.removeLike(currentUserId, post.getLogId());
            loadLikedPosts();
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}