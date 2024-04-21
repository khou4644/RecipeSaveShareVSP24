package com.example.recipesavesharevsp24;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private static final String USER_ID_KEY = "com.example.privtestproj3.userIdKey";
    private static final String PREFERENCES_KEY = "com.example.privtestproj3.PREFERENCES_KEY";

    private Button mLoginButton;
    private Button mCreateAccButton;
    private SharedPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstancestate) {
        super.onCreate(savedInstancestate);
        setContentView(R.layout.activity_main);

        mPreferences = getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);

        mLoginButton = findViewById(R.id.button);
        mCreateAccButton = findViewById(R.id.createAccButton);

        mLoginButton.setOnClickListener(v -> {
            Intent intent = LoginActivity.intentFactory(MainActivity.this);
            startActivity(intent);
        });

        mCreateAccButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CreateAccountActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (checkForLoggedInUser()) {
            Intent intent = LandingPage.intentFactory(MainActivity.this, mPreferences.getInt(USER_ID_KEY, -1));
            startActivity(intent);
            finish();
        }
    }

    private boolean checkForLoggedInUser() {
        SharedPreferences preferences = getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
        int userId = preferences.getInt(USER_ID_KEY, -1);
        return userId != -1;
    }
}




