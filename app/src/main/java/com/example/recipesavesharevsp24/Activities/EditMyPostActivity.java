package com.example.recipesavesharevsp24.Activities;
import static com.example.recipesavesharevsp24.Activities.MyPostActivity.EDIT_POST_REQUEST_CODE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.recipesavesharevsp24.DB.AppDataBase;
import com.example.recipesavesharevsp24.DB.RecipeShareSaveDAO;
import com.example.recipesavesharevsp24.R;
import com.example.recipesavesharevsp24.RecyclerView.MyPostAdapter;
import com.example.recipesavesharevsp24.RecyclerView.PostAdapter;

import java.util.ArrayList;
import java.util.List;
public class EditMyPostActivity extends AppCompatActivity {

    private RecipeShareSaveDAO mRecipeShareSaveDAO;
    private EditText mRecipeEditText;
    private EditText mServesEditText;
    private EditText mIngredientsEditText;
    private Button mUpdateButton;
    private Button mCloneButton;

    private int mPostId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_post);

        mRecipeShareSaveDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .RecipeShareSaveDAO();

        mRecipeEditText = findViewById(R.id.editTextRecipe);
        mServesEditText = findViewById(R.id.editTextServes);
        mIngredientsEditText = findViewById(R.id.editTextIngredients);
        mUpdateButton = findViewById(R.id.buttonUpdate);
        mCloneButton = findViewById(R.id.buttonClone);

        mPostId = getIntent().getIntExtra("postId", -1);

        if (mPostId != -1) {
            RecipeShareSave post = mRecipeShareSaveDAO.getRecipeShareSaveById(mPostId).get(0);
            mRecipeEditText.setText(post.getRecipe());
            mServesEditText.setText(String.valueOf(post.getServes()));
            mIngredientsEditText.setText(post.getIngredients());
        }

        mUpdateButton.setOnClickListener(v -> {
            updatePost();
        });

        mCloneButton.setOnClickListener(v -> {
            clonePost();
        });
    }

    private void updatePost() {
        String recipe = mRecipeEditText.getText().toString();
        int serves = Integer.parseInt(mServesEditText.getText().toString());
        String ingredients = mIngredientsEditText.getText().toString();

        RecipeShareSave updatedPost = mRecipeShareSaveDAO.getRecipeShareSaveById(mPostId).get(0);
        updatedPost.setRecipe(recipe);
        updatedPost.setServes(serves);
        updatedPost.setIngredients(ingredients);

        mRecipeShareSaveDAO.updateRecipeShareSave(updatedPost);

        Toast.makeText(this, "Post updated successfully", Toast.LENGTH_SHORT).show();

        // Set the result and pass the updated post ID
        Intent resultIntent = new Intent();
        resultIntent.putExtra("updatedPostId", mPostId);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void clonePost() {
        String recipe = mRecipeEditText.getText().toString();
        int serves = Integer.parseInt(mServesEditText.getText().toString());
        String ingredients = mIngredientsEditText.getText().toString();

        RecipeShareSave clonedPost = new RecipeShareSave(recipe, serves, ingredients, getCurrentUserId());
        mRecipeShareSaveDAO.insert(clonedPost);

        // Set the result and pass the cloned post ID
        Intent resultIntent = new Intent();
        resultIntent.putExtra("clonedPostId", clonedPost.getLogId());
        setResult(RESULT_OK, resultIntent);

        Toast.makeText(this, "Post cloned successfully", Toast.LENGTH_SHORT).show();
        finish();
    }

    private int getCurrentUserId() {
        SharedPreferences preferences = getSharedPreferences("com.example.recipesavesharevsp24.PREFERENCES_KEY", Context.MODE_PRIVATE);
        return preferences.getInt("com.example.recipesavesharevsp24.userIdKey", -1);
    }
}