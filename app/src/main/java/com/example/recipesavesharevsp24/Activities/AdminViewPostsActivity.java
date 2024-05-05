package com.example.recipesavesharevsp24.Activities;

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

    private RecyclerView mPostRecyclerView;
    private AdminViewPostAdapter mAdminViewPostAdapter;
    private RecipeShareSaveDAO mRecipeShareSaveDAO;

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
    }



}