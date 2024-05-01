package com.example.recipesavesharevsp24.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.recipesavesharevsp24.DB.AppDataBase;
import com.example.recipesavesharevsp24.DB.RecipeShareSaveDAO;
import com.example.recipesavesharevsp24.R;
import com.example.recipesavesharevsp24.RecyclerView.MyPostAdapter;
import com.example.recipesavesharevsp24.RecyclerView.PostAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyPostActivity extends AppCompatActivity {

    private static final String MENU_ITEM_LIKED_POSTS = "liked_posts";
    private static final String USER_ID_KEY = "com.example.recipesavesharevsp24.userIdKey";
    private static final String PREFERENCES_KEY = "com.example.recipesavesharevsp24.PREFERENCES_KEY";

    private RecyclerView mPostRecyclerView;
    //private PostAdapter mPostAdapter;
    private MyPostAdapter mMyPostAdapter;
    private RecipeShareSaveDAO mRecipeShareSaveDAO;

    private SharedPreferences mPreferences = null;

    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypost);
        getPrefs();
        mRecipeShareSaveDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .RecipeShareSaveDAO();

        mPostRecyclerView = findViewById(R.id.postRecyclerView);
        mPostRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        int currentUserId = mPreferences.getInt(USER_ID_KEY, -1);
        mUser = mRecipeShareSaveDAO.getUserByUserId(currentUserId);

//        mPostAdapter = new PostAdapter(this, new ArrayList<>(), mRecipeShareSaveDAO);
//        mPostRecyclerView.setAdapter(mPostAdapter);
//
//        List<RecipeShareSave> userPosts = mRecipeShareSaveDAO.getPostsByUserId(currentUserId);
//        mPostAdapter.setPosts(userPosts);

        mMyPostAdapter = new MyPostAdapter(this, new ArrayList<>(), mRecipeShareSaveDAO);
        mPostRecyclerView.setAdapter(mMyPostAdapter);

        List<RecipeShareSave> userPosts = mRecipeShareSaveDAO.getPostsByUserId(currentUserId);
        mMyPostAdapter.setPosts(userPosts);
    }

    // Include the rest of the methods from PostActivity, such as onCreateOptionsMenu, onPrepareOptionsMenu, onOptionsItemSelected, logoutUser, clearUserFromPref, and getPrefs

    // You might also want to add a menu item or button to navigate to this activity from other activities (e.g., LandingPage)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mUser != null){
            MenuItem item = menu.findItem(R.id.item1);
            item.setTitle(mUser.getUserName());

        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.item1) {
            logoutUser();
            return true;
        } else if (item.getTitle().equals(MENU_ITEM_LIKED_POSTS)) {
            Intent intent = new Intent(MyPostActivity.this, LikedPostActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.back_previous_page) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logoutUser(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        alertBuilder.setMessage(R.string.logout);

        alertBuilder.setPositiveButton(getString(R.string.yes),
                (dialog, which) -> {
//                    clearUserFromIntent();
                    Intent intent = new Intent(MyPostActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    clearUserFromPref();
//                    mUserId = -1;
//                    checkForUser();
                });
        alertBuilder.setNegativeButton(getString(R.string.no),
                (dialog, which) -> {

                });
        alertBuilder.create().show();

    }
    private void clearUserFromPref() {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.remove(USER_ID_KEY);
        editor.apply();
//        addUserToPreference(-1);
    }

    private void getPrefs() {
        mPreferences = this.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
    }

}