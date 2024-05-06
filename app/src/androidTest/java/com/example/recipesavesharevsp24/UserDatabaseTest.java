package com.example.recipesavesharevsp24;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.recipesavesharevsp24.Activities.User;
import com.example.recipesavesharevsp24.DB.AppDataBase;
import com.example.recipesavesharevsp24.DB.RecipeShareSaveDAO;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class UserDatabaseTest {
    private RecipeShareSaveDAO recipeShareSaveDAO;
    private AppDataBase appDatabase;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDataBase.class).build();
        recipeShareSaveDAO = appDatabase.RecipeShareSaveDAO();
    }

    @After
    public void closeDb() {
        appDatabase.close();
    }

    @Test
    public void testInsertUser() {
        User user = new User("testuser", "password", false, false);
        recipeShareSaveDAO.insert(user);
        User insertedUser = recipeShareSaveDAO.getUserByUsername("testuser");
        assertNotNull(insertedUser);
        assertEquals("testuser", insertedUser.getUserName());
    }
}