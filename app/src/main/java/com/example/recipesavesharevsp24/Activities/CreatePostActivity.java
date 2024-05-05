package com.example.recipesavesharevsp24.Activities;

import static com.example.recipesavesharevsp24.Activities.MyPostActivity.EDIT_POST_REQUEST_CODE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.recipesavesharevsp24.DB.AppDataBase;
import com.example.recipesavesharevsp24.DB.RecipeShareSaveDAO;
import com.example.recipesavesharevsp24.R;
import com.example.recipesavesharevsp24.databinding.ActivityCreatePostBinding;

import java.util.List;

public class CreatePostActivity extends AppCompatActivity {

    private static final String USER_ID_KEY = "com.example.recipesavesharevsp24.userIdKey";
    private static final String PREFERENCES_KEY = "com.example.recipesavesharevsp24.PREFERENCES_KEY";
    private ActivityCreatePostBinding binding;

    private TextView cookTextView;

    private TextView mMainDisplay;
    private EditText mRecipe;
    private EditText mServes;
    private EditText mIngredients;
    private Button mSubmit;
    private SharedPreferences mPreferences = null;

    private RecipeShareSaveDAO mRecipeShareSaveDAO;
    private List<RecipeShareSave> mRecipeShareSaveList;
    private int mUserId;
    private User mUser;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreatePostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getPrefs();
        getDataBase();


        mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);
        mUser = mRecipeShareSaveDAO.getUserByUserId(mUserId);
        cookTextView = findViewById(R.id.CookingTime);
        String data = getIntent().getStringExtra("data");
        cookTextView.setText(data);




        mMainDisplay = binding.createPostDisplay;
        mRecipe = binding.createPostRecipeEditText;
        mServes = binding.createPostServesEditText;
        mIngredients = binding.createPostIngredientsEditText;
        mSubmit = binding.createPostSubmitButton;

        mMainDisplay.setMovementMethod(new ScrollingMovementMethod());
        mIngredients.setMovementMethod(new ScrollingMovementMethod());

        refreshDisplay();

        mSubmit.setOnClickListener(v -> {
            RecipeShareSave log = getValuesFromDisplay();
            if (log != null) {
                log.setUserId(mUserId);
                submitRecipeShareSaveLog(log);
                refreshDisplay();
            }
        });
    }

    private void getDataBase() {
        mRecipeShareSaveDAO = AppDataBase.getInstance(this).RecipeShareSaveDAO();
        mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);
    }

    private void getPrefs() {
        mPreferences = this.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
    }

    private void submitRecipeShareSaveLog(RecipeShareSave log) {
        mRecipeShareSaveDAO.insert(log);
    }



    private RecipeShareSave getValuesFromDisplay() {
        String recipe = "";
        int serves = 0;
        String ingredients = "";

        recipe = mRecipe.getText().toString().trim();
        if (recipe.isEmpty()) {
            Toast.makeText(this, "Recipe name cannot be empty", Toast.LENGTH_SHORT).show();
            return null;
        }

        String servesInput = mServes.getText().toString().trim();
        if (!servesInput.isEmpty()) {
            try {
                serves = Integer.parseInt(servesInput);
            } catch (NumberFormatException e) {
                Log.d("Serves", "Couldn't convert serves");
                // Handle the case where the input is not a valid integer
                serves = 0; // Set serves to 0 if the input is invalid
            }
        }
        ingredients = mIngredients.getText().toString();
        return new RecipeShareSave(recipe, serves, ingredients, mUserId);
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
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mUser != null){
            MenuItem item = menu.findItem(R.id.item1);
            item.setTitle(mUser.getUserName());

        }
        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
        MenuItem likedPostsItem = menu.findItem(R.id.liked_posts);
        likedPostsItem.setVisible(false);
        return true;
    }

    private void logoutUser(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        alertBuilder.setMessage(R.string.logout);

        alertBuilder.setPositiveButton(getString(R.string.yes),
                (dialog, which) -> {
                    Intent intent = new Intent(CreatePostActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    clearUserFromPref();
                });
        alertBuilder.setNegativeButton(getString(R.string.no),
                (dialog, which) -> {

                });
        alertBuilder.create().show();

    }

    private void clearUserFromPref() {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.remove(USER_ID_KEY);
        editor.apply();
//        addUserToPreference(-1);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.item1) {
            logoutUser();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void updateDisplayForUpdatedPost(int updatedPostId) {
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_POST_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            int updatedPostId = data.getIntExtra("updatedPostId", -1);
            if (updatedPostId != -1) {
                updateDisplayForUpdatedPost(updatedPostId);
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}