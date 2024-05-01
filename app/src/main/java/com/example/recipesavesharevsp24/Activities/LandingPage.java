package com.example.recipesavesharevsp24.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;


import com.example.recipesavesharevsp24.DB.AppDataBase;
import com.example.recipesavesharevsp24.DB.RecipeShareSaveDAO;
import com.example.recipesavesharevsp24.R;
import com.example.recipesavesharevsp24.databinding.PageLandingBinding;

import java.util.List;

public class LandingPage extends AppCompatActivity {

    private static final String USER_ID_KEY = "com.example.recipesavesharevsp24.userIdKey";
    private static final String PREFERENCES_KEY = "com.example.recipesavesharevsp24.PREFERENCES_KEY";
    private PageLandingBinding binding;

    private TextView mMainDisplay;

    private EditText mRecipe;
    private EditText mServes;
    private EditText mIngredients;

    private Button mSubmit;

    private Button mAdminButton;

    private Button mViewPostsButton;

    private Button mViewMyPostsButton;

    private RecipeShareSaveDAO mRecipeShareSaveDAO;

    private List<RecipeShareSave> mRecipeShareSaveList;

    private int mUserId = -1;

    private SharedPreferences mPreferences = null;
    private User mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = PageLandingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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

        mMainDisplay = binding.mainRecipeShareSaveDisplay;
        mRecipe = binding.mainRecipeEditText;
        mServes = binding.mainServesEditText;
        mIngredients = binding.mainIngredientsEditText;
        mSubmit = binding.mainSubmitButton;

        mViewPostsButton = findViewById(R.id.viewPostsButton);
        mViewPostsButton.setOnClickListener(v -> {
            Intent intent = new Intent(LandingPage.this, PostActivity.class);
            startActivity(intent);
        });
        mViewMyPostsButton = findViewById(R.id.viewMyPostsButton);
        mViewMyPostsButton.setOnClickListener(v -> {
            Intent intent = new Intent(LandingPage.this, MyPostActivity.class);
            startActivity(intent);
        });

        mMainDisplay.setMovementMethod(new ScrollingMovementMethod());
        mIngredients.setMovementMethod(new ScrollingMovementMethod());

        refreshDisplay();

        mSubmit.setOnClickListener((v) -> {
            RecipeShareSave log = getValuesFromDisplay();
            log.setUserId(mUser.getUserId());
            submitRecipeShareSaveLog();
            refreshDisplay();
        });
    }

    private void loginUser(int userId) {
        mUser = mRecipeShareSaveDAO.getUserByUserId(userId);
        if (mUser == null) {
            // Check if we have any users at all
            List<User> users = mRecipeShareSaveDAO.getAllUsers();

            // Retrieve the user again after creating the predefined users
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

    //TODO finish this
//    private void addUserToPreference(int userId) {
//        if (mPreferences == null) {
//            getPrefs();
//        }
//        SharedPreferences.Editor editor = mPreferences.edit();
//        editor.putInt(USER_ID_KEY, userId);
//        editor.apply(); // or editor.commit();
//    }

    private void getDataBase() {
        mRecipeShareSaveDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
                .RecipeShareSaveDAO();
    }

//    private void checkForUser() {
//        //do we have a  user in the intent?
//        mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);
//
//        //Do we have a user in the preferences?
//        if(mUserId != -1){
//            return;
//        }
//
//        if (mPreferences == null ){
//            getPrefs();
//        }
//            mUserId =mPreferences.getInt(USER_ID_KEY, -1);
//
//
//        if (mUserId != -1){
//            return;
//        }
//
//        //do we have any users at all?
//        List<User> users = mRecipeShareSaveDAO.getAllUsers();
//        if (users.size() <= 0){
//            User defaultUser = new User("testuser1", "testuser1", false);
//            User defaultAdmin = new User("admin2", "admin2", true);
//            mRecipeShareSaveDAO.insert(defaultUser, defaultAdmin);
//        }
//
//        Intent intent = LoginActivity.intentFactory(this);
//        startActivity(intent);
//
//    }

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

    private void submitRecipeShareSaveLog() {
        String exercise = mRecipe.getText().toString();
        int serves = Integer.parseInt(mServes.getText().toString());
        String ingredients = mIngredients.getText().toString(); // Get the ingredients from the EditText

        RecipeShareSave log = new RecipeShareSave(exercise, serves, ingredients, mUserId);
        mRecipeShareSaveDAO.insert(log);
    }

    private RecipeShareSave getValuesFromDisplay() {
        String recipe= "No record found";
        int serves = 0;
        String ingredients = ""; // Initialize ingredients as an empty string

        recipe = mRecipe.getText().toString();
        try {
            serves = Integer.parseInt((mServes.getText().toString()));
        } catch (NumberFormatException e) {
            Log.d("Serves", "Couldn't convert serves");
        }
        ingredients = mIngredients.getText().toString(); // Get the ingredients from the EditText
        RecipeShareSave log = new RecipeShareSave(recipe, serves, ingredients, mUserId);
        return log;
    }

    private void refreshDisplay() {
        mRecipeShareSaveList = mRecipeShareSaveDAO.getRecipeShareSaveByUserId(mUserId);
        if (!mRecipeShareSaveList.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (RecipeShareSave log : mRecipeShareSaveList) {
                sb.append(log.toString());
            }
            mMainDisplay.setText(sb.toString());
        } else {
            mMainDisplay.setText(R.string.no_logs_message);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
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
}