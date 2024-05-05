package com.example.recipesavesharevsp24.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.recipesavesharevsp24.DB.AppDataBase;
import com.example.recipesavesharevsp24.DB.RecipeShareSaveDAO;
import com.example.recipesavesharevsp24.R;
import com.example.recipesavesharevsp24.RecyclerView.AdminViewPostAdapter;

import java.util.List;

public class AdminViewPostsActivity extends AppCompatActivity {

    private static final String PREFERENCES_KEY = "com.example.recipesavesharevsp24.PREFERENCES_KEY";
    private static final String USER_ID_KEY = "com.example.recipesavesharevsp24.userIdKey";
    private RecyclerView mPostRecyclerView;
    private AdminViewPostAdapter mAdminViewPostAdapter;
    private RecipeShareSaveDAO mRecipeShareSaveDAO;
    private SharedPreferences mPreferences = null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_posts);

        mRecipeShareSaveDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .RecipeShareSaveDAO();

        mPostRecyclerView = findViewById(R.id.adminPostRecyclerView);
        mPostRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdminViewPostAdapter = new AdminViewPostAdapter(this, mRecipeShareSaveDAO);
        mPostRecyclerView.setAdapter(mAdminViewPostAdapter);

        LiveData<List<RecipeShareSave>> postList = mRecipeShareSaveDAO.getAllRecipeShareSave();
        postList.observe(this, posts -> {
            mAdminViewPostAdapter.setPosts(posts);
        });

        mAdminViewPostAdapter.setOnEditClickListener(postId -> {
            openEditAnyPostFragment(postId);
        });

    }

    private void openEditAnyPostFragment(int postId) {
        EditAnyPostFragment editAnyPostFragment = EditAnyPostFragment.newInstance(postId);
        editAnyPostFragment.show(getSupportFragmentManager(), "EditAnyPostFragment");
    }

    public void refreshPostList() {
        List<RecipeShareSave> posts = mRecipeShareSaveDAO.getAllRecipeShareSaves();
        mAdminViewPostAdapter.setPosts(posts);
    }


    private void getPrefs() {
        mPreferences = this.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
    }

}