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
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.recipesavesharevsp24.DB.AppDataBase;
import com.example.recipesavesharevsp24.DB.RecipeShareSaveDAO;
import com.example.recipesavesharevsp24.R;
import com.example.recipesavesharevsp24.RecyclerView.AdminViewPostAdapter;

import java.util.List;

public class AdminViewPostsActivity extends AppCompatActivity {

    private static final String PREFERENCES_KEY = "com.example.recipesavesharevsp24.PREFERENCES_KEY";
    private static final String USER_ID_KEY = "com.example.recipesavesharevsp24.userIdKey";
    private RecyclerView mPostRecyclerView;
    private AdminViewPostAdapter mAdminViewPostAdapter;
    private RecipeShareSaveDAO mRecipeShareSaveDAO;
    private SharedPreferences mPreferences = null;
    private int mUserId;
    private User mUser;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_posts);

        getPrefs();
        getDataBase();

        mPostRecyclerView = findViewById(R.id.adminPostRecyclerView);
        mPostRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdminViewPostAdapter = new AdminViewPostAdapter(this, mRecipeShareSaveDAO);
        mPostRecyclerView.setAdapter(mAdminViewPostAdapter);

        LiveData<List<RecipeShareSave>> postList = mRecipeShareSaveDAO.getAllRecipeShareSave();
        postList.observe(this, posts -> {
            mAdminViewPostAdapter.setPosts(posts);
        });

        mAdminViewPostAdapter.setOnEditClickListener(postId -> {
            openEditAnyPostFragment(postId);
        });

    }

    private void openEditAnyPostFragment(int postId) {
        EditAnyPostFragment editAnyPostFragment = EditAnyPostFragment.newInstance(postId);
        editAnyPostFragment.show(getSupportFragmentManager(), "EditAnyPostFragment");
    }

    public void refreshPostList() {
        List<RecipeShareSave> posts = mRecipeShareSaveDAO.getAllRecipeShareSaves();
        mAdminViewPostAdapter.setPosts(posts);
    }
    private void getDataBase() {
        mRecipeShareSaveDAO = AppDataBase.getInstance(this).RecipeShareSaveDAO();
        mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);
    }

    private void logoutUser(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        alertBuilder.setMessage(R.string.logout);

        alertBuilder.setPositiveButton(getString(R.string.yes),
                (dialog, which) -> {
                    Intent intent = new Intent(AdminViewPostsActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    clearUserFromPref();
                });
        alertBuilder.setNegativeButton(getString(R.string.no),
                (dialog, which) -> {

                });
        alertBuilder.create().show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
        MenuItem likedPostsItem = menu.findItem(R.id.liked_posts);
        likedPostsItem.setVisible(false);
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
    private void clearUserFromPref() {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.remove(USER_ID_KEY);
        editor.apply();
//        addUserToPreference(-1);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.item1) {
            logoutUser();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    private void getPrefs() {
        mPreferences = this.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
    }

}