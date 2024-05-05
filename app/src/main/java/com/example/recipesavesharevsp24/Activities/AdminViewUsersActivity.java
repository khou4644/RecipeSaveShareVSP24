package com.example.recipesavesharevsp24.Activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.recipesavesharevsp24.DB.AppDataBase;
import com.example.recipesavesharevsp24.DB.RecipeShareSaveDAO;
import com.example.recipesavesharevsp24.R;
import com.example.recipesavesharevsp24.RecyclerView.AdminViewUserAdapter;

import java.util.List;

public class AdminViewUsersActivity extends AppCompatActivity {

    private RecyclerView userRecyclerView;
    private AdminViewUserAdapter adminUserAdapter;
    private RecipeShareSaveDAO recipeShareSaveDAO;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_admin_view_users);

    // Initialize the RecipeShareSaveDAO
    recipeShareSaveDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
            .allowMainThreadQueries()
            .build()
            .RecipeShareSaveDAO();

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