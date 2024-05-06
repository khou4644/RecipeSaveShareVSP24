package com.example.recipesavesharevsp24.Activities;


import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class UserTest {
    private User user;

    @Before
    public void setUp() {
        // Create a User instance for testing
        user = new User("JohnDoe", "password123", false, false);
    }

    @Test
    public void getUserId() {
        int userId = user.getUserId();
        assertEquals(0, userId); // Initially, userId should be 0
    }

    @Test
    public void setUserId() {
        user.setUserId(1);
        assertEquals(1, user.getUserId());
    }

    @Test
    public void getUserName() {
        String username = user.getUserName();
        assertEquals("JohnDoe", username);
    }

    @Test
    public void setUserName() {
        user.setUserName("JaneDoe");
        assertEquals("JaneDoe", user.getUserName());
    }

    @Test
    public void getPassword() {
        String password = user.getPassword();
        assertEquals("password123", password);
    }

    @Test
    public void setPassword() {
        user.setPassword("newpassword");
        assertEquals("newpassword", user.getPassword());
    }

    @Test
    public void getIsAdmin() {
        boolean isAdmin = user.getIsAdmin();
        assertFalse(isAdmin);
    }

    @Test
    public void setIsAdmin() {
        user.setIsAdmin(true);
        assertTrue(user.getIsAdmin());
    }

    @Test
    public void isMisBanned() {
        boolean isBanned = user.isMisBanned();
        assertFalse(isBanned);
    }

    @Test
    public void setMisBanned() {
        user.setMisBanned(true);
        assertTrue(user.isMisBanned());
    }
}