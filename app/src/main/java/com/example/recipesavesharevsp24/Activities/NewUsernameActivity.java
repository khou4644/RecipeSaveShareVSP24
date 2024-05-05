package com.example.recipesavesharevsp24.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recipesavesharevsp24.DB.AppDataBase;
import com.example.recipesavesharevsp24.DB.RecipeShareSaveDAO;
import com.example.recipesavesharevsp24.R;

public class NewUsernameActivity extends AppCompatActivity {
    private static final String USER_ID_KEY = "com.example.recipesavesharevsp24.userIdKey";
    private EditText mOldUsernameEditText;
    private EditText mNewUsernameEditText;
    private Button mConfirmButton;
    private RecipeShareSaveDAO mRecipeShareSaveDAO;
    private int mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_username);

        mOldUsernameEditText = findViewById(R.id.old_username_edit_text);
        mNewUsernameEditText = findViewById(R.id.new_username_edit_text);
        mConfirmButton = findViewById(R.id.confirm_button);

        mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);
        mRecipeShareSaveDAO = AppDataBase.getInstance(this).RecipeShareSaveDAO();

        mConfirmButton.setOnClickListener(v -> {
            String oldUsername = mOldUsernameEditText.getText().toString();
            String newUsername = mNewUsernameEditText.getText().toString();

            User user = mRecipeShareSaveDAO.getUserByUserId(mUserId);
            if (user != null && user.getUserName().equals(oldUsername)) {
                if (!newUsername.equals(oldUsername)) {
                    user.setUserName(newUsername);
                    mRecipeShareSaveDAO.update(user);
                    Toast.makeText(NewUsernameActivity.this, "Username changed successfully", Toast.LENGTH_SHORT).show();
                    // Add the new username as an extra to the Intent
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("NEW_USERNAME", newUsername);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                    Intent intent = new Intent(NewUsernameActivity.this, LandingPage.class);
                    intent.putExtra(USER_ID_KEY, mUserId);
                    startActivity(intent);
                } else {
                    Toast.makeText(NewUsernameActivity.this, "Please use a new username", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(NewUsernameActivity.this, "Invalid old username", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
