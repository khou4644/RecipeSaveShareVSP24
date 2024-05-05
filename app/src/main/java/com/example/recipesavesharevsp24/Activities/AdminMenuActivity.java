package com.example.recipesavesharevsp24.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;


import com.example.recipesavesharevsp24.DB.AppDataBase;
import com.example.recipesavesharevsp24.DB.RecipeShareSaveDAO;
import com.example.recipesavesharevsp24.R;

public class AdminMenuActivity extends AppCompatActivity {

    private static final String USER_ID_KEY = "com.example.recipesavesharevsp24.userIdKey";
    private static final String PREFERENCES_KEY = "com.example.recipesavesharevsp24.PREFERENCES_KEY";
    private RecipeShareSaveDAO mRecipeShareSaveDAO;
    private SharedPreferences mPreferences = null;
    private int mUserId;
    private Button viewAllPostsButton;
    private Button viewAllUsersButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu);

        // Initialize the RecipeShareSaveDAO
        getPrefs();
        getDataBase();

        viewAllPostsButton = findViewById(R.id.viewAllPostsButton);
        viewAllUsersButton = findViewById(R.id.viewAllUsersButton);

        viewAllPostsButton.setOnClickListener(v -> {
            // Launch the activity to view all posts
            Intent intent = new Intent(AdminMenuActivity.this, AdminViewPostsActivity.class);
            startActivity(intent);
        });

        viewAllUsersButton.setOnClickListener(v -> {
            // Launch the activity to view all users
            Intent intent = new Intent(AdminMenuActivity.this, AdminViewUsersActivity.class);
            startActivity(intent);
        });
    }

    private void getPrefs() {
        mPreferences = this.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
    }
    private void getDataBase() {
        mRecipeShareSaveDAO = AppDataBase.getInstance(this).RecipeShareSaveDAO();
        mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}