package com.example.recipesavesharevsp24.Activities;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.recipesavesharevsp24.DB.AppDataBase;
import com.example.recipesavesharevsp24.DB.RecipeShareSaveDAO;
import com.example.recipesavesharevsp24.R;
import com.example.recipesavesharevsp24.RecyclerView.MyPostAdapter;
import java.util.ArrayList;
import java.util.List;


public class MyPostActivity extends AppCompatActivity {
    private static final String MENU_ITEM_LIKED_POSTS = "liked_posts";
    private static final String USER_ID_KEY = "com.example.recipesavesharevsp24.userIdKey";
    private static final String PREFERENCES_KEY = "com.example.recipesavesharevsp24.PREFERENCES_KEY";
    private RecyclerView mPostRecyclerView;
    //private PostAdapter mPostAdapter;
    private MyPostAdapter mMyPostAdapter;
    private RecipeShareSaveDAO mRecipeShareSaveDAO;
    private SharedPreferences mPreferences = null;
    private User mUser;
    public static final int EDIT_POST_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypost);

        getPrefs();
        getDataBase();

        mPostRecyclerView = findViewById(R.id.postRecyclerView);
        mPostRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        int currentUserId = mPreferences.getInt(USER_ID_KEY, -1);
        mUser = mRecipeShareSaveDAO.getUserByUserId(currentUserId);

        mMyPostAdapter = new MyPostAdapter(this, new ArrayList<>(), mRecipeShareSaveDAO);
        mPostRecyclerView.setAdapter(mMyPostAdapter);

        LiveData<List<RecipeShareSave>> postList = mRecipeShareSaveDAO.getPostsByUserIdLiveData(currentUserId);
        postList.observe(this, posts -> {
            mMyPostAdapter.setPosts(posts);
        });

        // EditMyPostFragment
        mMyPostAdapter.setOnEditClickListener(postId -> {
            showEditFragment(postId);
        });
    }

    private void showEditFragment(int postId) {
        EditMyPostFragment fragment = EditMyPostFragment.newInstance(postId);
        fragment.setOnDialogDismissListener(this::refreshPostList);
        fragment.show(getSupportFragmentManager(), "EditMyPostFragment");
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.item1) {
            logoutUser();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logoutUser(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setMessage(R.string.logout);
        alertBuilder.setPositiveButton(getString(R.string.yes),
                (dialog, which) -> {

                    Intent intent = new Intent(MyPostActivity.this, MainActivity.class);
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
    }

    void refreshPostList() {
        int currentUserId = mPreferences.getInt(USER_ID_KEY, -1);
        LiveData<List<RecipeShareSave>> postList = mRecipeShareSaveDAO.getPostsByUserIdLiveData(currentUserId);
        postList.observe(this, posts -> {
            mMyPostAdapter.setPosts(posts);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshPostList();
    }

    private void getPrefs() {
        mPreferences = this.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
    }

    private void getDataBase() {
        mRecipeShareSaveDAO = AppDataBase.getInstance(this).RecipeShareSaveDAO();
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}