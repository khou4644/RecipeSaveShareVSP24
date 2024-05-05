package com.example.recipesavesharevsp24.Activities;



import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.recipesavesharevsp24.DB.AppDataBase;
import com.example.recipesavesharevsp24.DB.RecipeShareSaveDAO;
import com.example.recipesavesharevsp24.R;
import java.util.List;

///**
// * A simple {@link Fragment} subclass.
// * Use the {@link EditMyPostFragment#newInstance} factory method to
// * create an instance of this fragment.
// */

public class EditMyPostFragment extends DialogFragment {
    private RecipeShareSaveDAO mRecipeShareSaveDAO;
    private EditText mRecipeEditText;
    private EditText mServesEditText;
    private EditText mIngredientsEditText;
    private Button mUpdateButton;
    private Button mCloneButton;

    private int mPostId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mPostId = getArguments().getInt("postId", -1);
        }

        // Initialize the mRecipeShareSaveDAO
        mRecipeShareSaveDAO = Room.databaseBuilder(requireContext(), AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .RecipeShareSaveDAO();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_my_post, container, false);

        mRecipeEditText = view.findViewById(R.id.editTextRecipe);
        mServesEditText = view.findViewById(R.id.editTextServes);
        mIngredientsEditText = view.findViewById(R.id.editTextIngredients);
        mUpdateButton = view.findViewById(R.id.buttonUpdate);
        mCloneButton = view.findViewById(R.id.buttonClone);

        if (mPostId != -1) {
            List<RecipeShareSave> posts = mRecipeShareSaveDAO.getRecipeShareSaveById(mPostId);
            if (!posts.isEmpty()) {
                RecipeShareSave post = posts.get(0);
                mRecipeEditText.setText(post.getRecipe());
                mServesEditText.setText(String.valueOf(post.getServes()));
                mIngredientsEditText.setText(post.getIngredients());
            }
        }

        mUpdateButton.setOnClickListener(v -> {
            updatePost();
        });

        mCloneButton.setOnClickListener(v -> {
            clonePost();
        });

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
        refreshPostList();
    }

    private void clonePost() {
        String recipe = mRecipeEditText.getText().toString();
        int serves = Integer.parseInt(mServesEditText.getText().toString());
        String ingredients = mIngredientsEditText.getText().toString();

        RecipeShareSave clonedPost = new RecipeShareSave(recipe, serves, ingredients, getCurrentUserId());
        mRecipeShareSaveDAO.insert(clonedPost);

        Toast.makeText(requireContext(), "Post cloned successfully", Toast.LENGTH_SHORT).show();
        refreshPostList();
    }

    private int getCurrentUserId() {
        SharedPreferences preferences = requireContext().getSharedPreferences("com.example.recipesavesharevsp24.PREFERENCES_KEY", Context.MODE_PRIVATE);
        return preferences.getInt("com.example.recipesavesharevsp24.userIdKey", -1);
    }

    private void refreshPostList() {
        MyPostActivity activity = (MyPostActivity) getActivity();
        if (activity != null) {
            activity.refreshPostList();
        }
    }

    public interface OnDialogDismissListener {
        void onDialogDismiss();
    }

    private OnDialogDismissListener mOnDialogDismissListener;

    public void setOnDialogDismissListener(OnDialogDismissListener listener) {
        mOnDialogDismissListener = listener;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mOnDialogDismissListener != null) {
            mOnDialogDismissListener.onDialogDismiss();
        }
    }

    public static EditMyPostFragment newInstance(int postId) {
        EditMyPostFragment fragment = new EditMyPostFragment();
        Bundle args = new Bundle();
        args.putInt("postId", postId);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        return dialog;
    }

}