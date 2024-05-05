package com.example.recipesavesharevsp24.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipesavesharevsp24.DB.AppDataBase;
import com.example.recipesavesharevsp24.DB.RecipeShareSaveDAO;
import com.example.recipesavesharevsp24.R;
import com.example.recipesavesharevsp24.RecyclerView.AdminViewUserAdapter;

import java.util.List;

public class AdminViewUsersActivity extends AppCompatActivity {

    private static final String USER_ID_KEY = "com.example.recipesavesharevsp24.userIdKey";
    private static final String PREFERENCES_KEY = "com.example.recipesavesharevsp24.PREFERENCES_KEY";
    private RecyclerView userRecyclerView;
    private AdminViewUserAdapter adminUserAdapter;
    private RecipeShareSaveDAO recipeShareSaveDAO;
    private User mUser;
                                                                                                                                                                                private SharedPreferences mPreferences = null;
                                                                                                                                                                                private int mUserId;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_admin_view_users);

    // Initialize the RecipeShareSaveDAO
    getPrefs();
    getDataBase();

    // Initialize the RecyclerView and set up the adapter
    userRecyclerView = findViewById(R.id.userRecyclerView);
    userRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    adminUserAdapter = new AdminViewUserAdapter(this, recipeShareSaveDAO);
    userRecyclerView.setAdapter(adminUserAdapter);

    // Load and set the user data for the adapter
    List<User> users = recipeShareSaveDAO.getAllUsers();
    adminUserAdapter.setUsers(users);

    // Set up the "Ban" button click listener
    adminUserAdapter.setOnBanClickListener(user -> {
        user.setMisBanned(!user.isMisBanned());
        recipeShareSaveDAO.update(user); // Update the user in the database

        String action = user.isMisBanned() ? "banned" : "unbanned";
        Toast.makeText(this, "User " + user.getUserName() + " has been " + action, Toast.LENGTH_SHORT).show();
    });

    // Set up the "Delete" button click listener
    adminUserAdapter.setOnDeleteClickListener(user -> {
        recipeShareSaveDAO.delete(user); // Delete the user from the database
        Toast.makeText(this, "User " + user.getUserName() + " has been deleted", Toast.LENGTH_SHORT).show();
    });
}

    private void getDataBase() {
        recipeShareSaveDAO = AppDataBase.getInstance(this).RecipeShareSaveDAO();
        mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);
    }

    private void getPrefs() {
        mPreferences = this.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
    }
    private void logoutUser(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        alertBuilder.setMessage(R.string.logout);

        alertBuilder.setPositiveButton(getString(R.string.yes),
                (dialog, which) -> {
                    Intent intent = new Intent(AdminViewUsersActivity.this, MainActivity.class);
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
    @Override
    protected void onResume() {
        super.onResume();

        // Reload the user data when the activity is resumed
        List<User> users = recipeShareSaveDAO.getAllUsers();
        adminUserAdapter.setUsers(users);

        // Notify the adapter to update the button state
        adminUserAdapter.notifyDataSetChanged();
    }
}