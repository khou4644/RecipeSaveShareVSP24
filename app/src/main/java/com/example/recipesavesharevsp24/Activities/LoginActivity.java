package com.example.recipesavesharevsp24.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;


import com.example.recipesavesharevsp24.DB.AppDataBase;
import com.example.recipesavesharevsp24.DB.RecipeShareSaveDAO;
import com.example.recipesavesharevsp24.R;

public class LoginActivity extends AppCompatActivity {

    private static final String USER_ID_KEY = "com.example.recipesavesharevsp24.userIdKey";
    private static final String PREFERENCES_KEY = "com.example.recipesavesharevsp24.PREFERENCES_KEY";
    private EditText mUsernameField;

    private EditText mPasswordField;

    private Button mButton;

    private RecipeShareSaveDAO mRecipeShareSaveDAO;

    private String mUsername;
    private String mPassword;

    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        getDatabase();
        createDefaultUsers();
        loginDisplay();
    }

    private void createDefaultUsers() {
        // Check if the default users already exist in the database
        User existingDefaultUser = mRecipeShareSaveDAO.getUserByUsername("testuser1");
        User existingDefaultAdmin = mRecipeShareSaveDAO.getUserByUsername("admin2");

        if (existingDefaultUser == null) {
            // Create the default user if it doesn't exist
            User defaultUser = new User("testuser1", "testuser1", false, false);
            mRecipeShareSaveDAO.insert(defaultUser);
        }

        if (existingDefaultAdmin == null) {
            // Create the default admin if it doesn't exist
            User defaultAdmin = new User( "admin2", "admin2", true, false);
            mRecipeShareSaveDAO.insert(defaultAdmin);
        }
    }
    private void loginDisplay() {
        mUsernameField = findViewById(R.id.editTextCreateUserName);
        mPasswordField = findViewById(R.id.editTextCreatePassword);
        mButton = findViewById(R.id.buttonLogin);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getValuesFromDisplay();
                if (checkForUserInDatabase()) {
                    if (!validatePassword()) {
                        Toast.makeText(LoginActivity.this, "Invalid password", Toast.LENGTH_SHORT).show();
                    } else {
                        loginUser(mUser);
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loginUser(User user) {
        if (user != null) {
            SharedPreferences.Editor editor = getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE).edit();
            editor.putInt(USER_ID_KEY, user.getUserId());
            editor.apply();

            Intent intent = LandingPage.intentFactory(LoginActivity.this, user.getUserId());
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validatePassword(){
        return mUser.getPassword().equals(mPassword);
    }

    private void getValuesFromDisplay(){
        mUsername = mUsernameField.getText().toString();
        mPassword = mPasswordField.getText().toString();

    }

    private boolean checkForUserInDatabase() {
        mUser = mRecipeShareSaveDAO.getUserByUsername(mUsername);
        if (mUser != null) {
            // Check if the user is banned
            if (mUser.isMisBanned()) {
                Toast.makeText(LoginActivity.this, "This user has been banned", Toast.LENGTH_SHORT).show();
                return false;
            }
            return true;
        }
        return false;
    }

    private void getDatabase(){
        mRecipeShareSaveDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .RecipeShareSaveDAO();
    }

    public static Intent intentFactory(Context context){
        Intent intent = new Intent(context, LoginActivity.class);
            return intent;
    }
}