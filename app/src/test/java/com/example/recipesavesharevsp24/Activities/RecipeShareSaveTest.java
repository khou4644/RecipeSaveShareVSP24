package com.example.recipesavesharevsp24.Activities;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

public class RecipeShareSaveTest {
    private RecipeShareSave recipeShareSave;

    @Before
    public void setUp() {
        // Create a RecipeShareSave instance for testing
        recipeShareSave = new RecipeShareSave("Baked Chicken", 4, "Chicken, Salt, Pepper", 1);
    }

    @Test
    public void getUserId() {
        int userId = recipeShareSave.getUserId();
        assertEquals(1, userId);
    }

    @Test
    public void setUserId() {
        recipeShareSave.setUserId(2);
        assertEquals(2, recipeShareSave.getUserId());
    }

    @Test
    public void testToString() {
        String expectedString = "Post# " + recipeShareSave.getLogId() + "\n" +
                "Recipe: " + recipeShareSave.getRecipe() + "\n" +
                "Serves: " + recipeShareSave.getServes() + "\n" +
                "Ingredients: " + "\n" + recipeShareSave.getIngredients() + "\n" +
                "Date: " + recipeShareSave.getDate() + "\n" +
                "User ID: " + recipeShareSave.getUserId() + "\n" +
                "=-=-=-=-=-=-\n";
        assertEquals(expectedString, recipeShareSave.toString());
    }

    @Test
    public void getLogId() {
        int logId = recipeShareSave.getLogId();
        assertEquals(0, logId); // Initially, logId should be 0
    }

    @Test
    public void setLogId() {
        recipeShareSave.setLogId(5);
        assertEquals(5, recipeShareSave.getLogId());
    }

    @Test
    public void getRecipe() {
        String recipe = recipeShareSave.getRecipe();
        assertEquals("Baked Chicken", recipe);
    }

    @Test
    public void setRecipe() {
        recipeShareSave.setRecipe("Grilled Salmon");
        assertEquals("Grilled Salmon", recipeShareSave.getRecipe());
    }

    @Test
    public void getServes() {
        int serves = recipeShareSave.getServes();
        assertEquals(4, serves);
    }

    @Test
    public void setServes() {
        recipeShareSave.setServes(6);
        assertEquals(6, recipeShareSave.getServes());
    }

    @Test
    public void getIngredients() {
        String ingredients = recipeShareSave.getIngredients();
        assertEquals("Chicken, Salt, Pepper", ingredients);
    }

    @Test
    public void setIngredients() {
        recipeShareSave.setIngredients("Salmon, Lemon, Dill");
        assertEquals("Salmon, Lemon, Dill", recipeShareSave.getIngredients());
    }

    @Test
    public void isReported() {
        assertFalse(recipeShareSave.isReported());
    }

    @Test
    public void setReported() {
        recipeShareSave.setReported(true);
        assertTrue(recipeShareSave.isReported());
    }

    @Test
    public void getDate() {
        assertNotNull(recipeShareSave.getDate());
    }

    @Test
    public void setDate() {
        Date newDate = new Date();
        recipeShareSave.setDate(newDate);
        assertEquals(newDate, recipeShareSave.getDate());
    }
}