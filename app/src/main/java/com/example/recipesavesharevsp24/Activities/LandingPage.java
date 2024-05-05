package com.example.recipesavesharevsp24.Activities;

import static com.example.recipesavesharevsp24.Activities.MyPostActivity.EDIT_POST_REQUEST_CODE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import org.jetbrains.annotations.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.example.recipesavesharevsp24.DB.AppDataBase;
import com.example.recipesavesharevsp24.DB.RecipeShareSaveDAO;
import com.example.recipesavesharevsp24.R;


import java.util.List;

public class LandingPage extends AppCompatActivity {

    private static final String USER_ID_KEY = "com.example.recipesavesharevsp24.userIdKey";
    private static final String PREFERENCES_KEY = "com.example.recipesavesharevsp24.PREFERENCES_KEY";

    private static final int REQUEST_CODE_NEW_USERNAME = 1; //default request code to update username on options menu(top right)

    private Button mAdminButton;
    private Button mViewPostsButton;
    private Button mViewMyPostsButton;
    private Button mCreateNewPostButton;
    private Button mNewUsernameButton;
    private Button mNewPasswordButton;

    private RecipeShareSaveDAO mRecipeShareSaveDAO;

    private int mUserId = -1;

    private SharedPreferences mPreferences = null;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_landing);

        getPrefs();

        getDataBase();

        mAdminButton = findViewById(R.id.adminButton);
        mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);
        if (mUserId != -1) {
            loginUser(mUserId);
            checkAdminUser();
        } else {
            // Handle the case when no user ID is found in the intent
            // For example, you can redirect to the login screen
            Intent intent = LoginActivity.intentFactory(this);
            startActivity(intent);
            finish();
            return;
        }

        mViewPostsButton = findViewById(R.id.viewPostsButton);
        mViewPostsButton.setOnClickListener(v -> {
            Intent intent = new Intent(LandingPage.this, PostActivity.class);
            intent.putExtra(USER_ID_KEY, mUserId);
            startActivity(intent);
        });
        mViewMyPostsButton = findViewById(R.id.viewMyPostsButton);
        mViewMyPostsButton.setOnClickListener(v -> {
            Intent intent = new Intent(LandingPage.this, MyPostActivity.class);
            intent.putExtra(USER_ID_KEY, mUserId);
            startActivity(intent);
        });

        mCreateNewPostButton = findViewById(R.id.createNewPostButton);
        mCreateNewPostButton.setOnClickListener(v -> {
            Intent intent = new Intent(LandingPage.this, CreatePostActivity.class);
            intent.putExtra(USER_ID_KEY, mUserId);
            startActivity(intent);
        });

        mNewUsernameButton = findViewById(R.id.NewUsernameButton);
        mNewUsernameButton.setOnClickListener(v -> {
            Intent intent = new Intent(LandingPage.this, NewUsernameActivity.class);
            intent.putExtra(USER_ID_KEY, mUserId);
            startActivityForResult(intent, REQUEST_CODE_NEW_USERNAME); // Used to keep update username on top right of options menu after creating new username in database
        });

        mNewPasswordButton = findViewById(R.id.NewPasswordButton);
        mNewPasswordButton.setOnClickListener(v -> {
            Intent intent = new Intent(LandingPage.this, NewPasswordActivity.class);
            intent.putExtra(USER_ID_KEY, mUserId);
            startActivity(intent);
        });
    }

    private void loginUser(int userId) {
        mUser = mRecipeShareSaveDAO.getUserByUserId(userId);
        if (mUser == null) {
            // Check if we have any users at all
            List<User> users = mRecipeShareSaveDAO.getAllUsers();

            // Retrieve the user again
            mUser = mRecipeShareSaveDAO.getUserByUserId(userId);
            if (mUser == null) {
                // Handle the case when the user is still not found
                Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
                Intent intent = LoginActivity.intentFactory(this);
                startActivity(intent);
                finish();
                return;
            }
        }
        invalidateOptionsMenu();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mUser != null){
            MenuItem item = menu.findItem(R.id.item1);
            item.setTitle(mUser.getUserName());

        }
        return super.onPrepareOptionsMenu(menu);
    }


    private void getDataBase() {
        mRecipeShareSaveDAO = AppDataBase.getInstance(this).RecipeShareSaveDAO();
    }


    private void getPrefs() {
        mPreferences = this.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
    }

    private void logoutUser(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        alertBuilder.setMessage(R.string.logout);

        alertBuilder.setPositiveButton(getString(R.string.yes),
                (dialog, which) -> {
//                    clearUserFromIntent();
                    Intent intent = new Intent(LandingPage.this, MainActivity.class);
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

    private void checkAdminUser() {
        if (mUser != null && mRecipeShareSaveDAO.isUserAdmin(mUser.getUserId())) {
            mAdminButton.setVisibility(View.VISIBLE);
            mAdminButton.setOnClickListener(v -> {
                Intent intent = new Intent(LandingPage.this, AdminMenuActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            });
        } else {
            mAdminButton.setVisibility(View.INVISIBLE);
        }
    }
    private void clearUserFromIntent(){
        getIntent().putExtra(USER_ID_KEY, -1);
    }

    private void clearUserFromPref() {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.remove(USER_ID_KEY);
        editor.apply();
//        addUserToPreference(-1);
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.item1) {
            logoutUser();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static Intent intentFactory(Context context, int userId){
        Intent intent = new Intent(context, LandingPage.class);
        intent.putExtra(USER_ID_KEY, userId);
        return intent;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (data != null) {
                String newUsername = data.getStringExtra("NEW_USERNAME");
                if (newUsername != null) {
                    // Update the mUser object with the new username
                    mUser.setUserName(newUsername);
                    invalidateOptionsMenu(); // Refresh the menu with the new username
                }
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }
}