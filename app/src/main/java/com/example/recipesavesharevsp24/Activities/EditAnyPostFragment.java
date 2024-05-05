package com.example.recipesavesharevsp24.Activities;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.fragment.app.DialogFragment;
import androidx.room.Room;

import com.example.recipesavesharevsp24.DB.AppDataBase;
import com.example.recipesavesharevsp24.DB.RecipeShareSaveDAO;
import com.example.recipesavesharevsp24.R;



public class EditAnyPostFragment extends DialogFragment {
    private static final String ARG_POST_ID = "postId";

    private RecipeShareSaveDAO mRecipeShareSaveDAO;
    private EditText mRecipeEditText;
    private EditText mServesEditText;
    private EditText mIngredientsEditText;
    private Button mUpdateButton;

    private int mPostId;

    public static EditAnyPostFragment newInstance(int postId) {
        EditAnyPostFragment fragment = new EditAnyPostFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POST_ID, postId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPostId = getArguments().getInt(ARG_POST_ID);
        }
        mRecipeShareSaveDAO = Room.databaseBuilder(requireContext(), AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .RecipeShareSaveDAO();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_any_post, container, false);
        mRecipeEditText = view.findViewById(R.id.editTextRecipe);
        mServesEditText = view.findViewById(R.id.editTextServes);
        mIngredientsEditText = view.findViewById(R.id.editTextIngredients);
        mUpdateButton = view.findViewById(R.id.buttonUpdate);

        // Load the post details into the UI
        RecipeShareSave post = mRecipeShareSaveDAO.getRecipeShareSaveById(mPostId).get(0);
        mRecipeEditText.setText(post.getRecipe());
        mServesEditText.setText(String.valueOf(post.getServes()));
        mIngredientsEditText.setText(post.getIngredients());

        mUpdateButton.setOnClickListener(v -> updatePost());

        return view;
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

        Toast.makeText(requireContext(), "Post updated successfully", Toast.LENGTH_SHORT).show();

        // Refresh the post list in AdminViewPostsActivity
        AdminViewPostsActivity activity = (AdminViewPostsActivity) getActivity();
        if (activity != null) {
            activity.refreshPostList();
        }

        dismiss();
    }
}