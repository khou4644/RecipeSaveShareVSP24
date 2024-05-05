package com.example.recipesavesharevsp24.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.recipesavesharevsp24.DB.AppDataBase;
import com.example.recipesavesharevsp24.DB.RecipeShareSaveDAO;
import com.example.recipesavesharevsp24.R;

public class AdminMenuActivity extends AppCompatActivity {

    private RecipeShareSaveDAO recipeShareSaveDAO;
    private Button viewAllPostsButton;
    private Button viewAllUsersButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu);

        // Initialize the RecipeShareSaveDAO
        recipeShareSaveDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .RecipeShareSaveDAO();

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


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}