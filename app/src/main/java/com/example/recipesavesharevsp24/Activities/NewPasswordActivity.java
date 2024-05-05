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

public class NewPasswordActivity extends AppCompatActivity {

    private static final String USER_ID_KEY = "com.example.recipesavesharevsp24.userIdKey";
    private EditText mOldPasswordEditText;
    private EditText mNewPasswordEditText;
    private Button mConfirmButton;
    private RecipeShareSaveDAO mRecipeShareSaveDAO;
    private int mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);

        mOldPasswordEditText = findViewById(R.id.old_password_edit_text);
        mNewPasswordEditText = findViewById(R.id.new_password_edit_text);
        mConfirmButton = findViewById(R.id.confirm_button);

        mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);
        mRecipeShareSaveDAO = AppDataBase.getInstance(this).RecipeShareSaveDAO();

        mConfirmButton.setOnClickListener(v -> {
            String oldPassword = mOldPasswordEditText.getText().toString();
            String newPassword = mNewPasswordEditText.getText().toString();

            User user = mRecipeShareSaveDAO.getUserByUserId(mUserId);
            if (user != null && user.getPassword().equals(oldPassword)) {
                if (!newPassword.equals(oldPassword)) {
                    user.setPassword(newPassword);
                    mRecipeShareSaveDAO.update(user);
                    Toast.makeText(NewPasswordActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(NewPasswordActivity.this, LandingPage.class);
                    intent.putExtra(USER_ID_KEY, mUserId);
                    startActivity(intent);
                } else {
                    Toast.makeText(NewPasswordActivity.this, "Please use a new password", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(NewPasswordActivity.this, "Invalid old password", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
