package com.example.recipesavesharevsp24.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;



import com.example.recipesavesharevsp24.DB.AppDataBase;
import com.example.recipesavesharevsp24.DB.RecipeShareSaveDAO;
import com.example.recipesavesharevsp24.R;

public class CreateAccountActivity extends AppCompatActivity {
        private EditText mUsernameEditText;
        private EditText mPasswordEditText;
        private Button mCreateAccountButton;

        private RecipeShareSaveDAO mRecipeShareSaveDAO;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_account_create);

            mUsernameEditText = findViewById(R.id.editTextCreateUserName);
            mPasswordEditText = findViewById(R.id.editTextCreatePassword);
            mCreateAccountButton = findViewById(R.id.buttonCreateAccount);

            mRecipeShareSaveDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build()
                    .RecipeShareSaveDAO();

            mCreateAccountButton.setOnClickListener(v -> {
                String username = mUsernameEditText.getText().toString();
                String password = mPasswordEditText.getText().toString();

                if (isUsernameDuplicate(username)) {
                    Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show();
                } else {
                    User newUser = new User(username, password, false, false);
                    mRecipeShareSaveDAO.insert(newUser);
                    Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = LoginActivity.intentFactory(CreateAccountActivity.this);
                    startActivity(intent);
                    finish();
                }
            });
        }

        private boolean isUsernameDuplicate(String username) {
            User user = mRecipeShareSaveDAO.getUserByUsername(username);
            return user != null;
        }
    }
