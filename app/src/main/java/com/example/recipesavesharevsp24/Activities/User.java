package com.example.recipesavesharevsp24.Activities;

import androidx.lifecycle.MutableLiveData;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.recipesavesharevsp24.DB.AppDataBase;

import java.io.Serializable;

@Entity(tableName = AppDataBase.USER_TABLE)
public class User {

    @PrimaryKey(autoGenerate = true)
    private int mUserId;

    private String mUserName;

    private String mPassword;

    private boolean mIsAdmin;

    private boolean misBanned;


    public User(String mUserName, String mPassword, boolean mIsAdmin, boolean misBanned) {
        this.mUserName = mUserName;
        this.mPassword = mPassword;
        this.mIsAdmin = mIsAdmin;
        this.misBanned = misBanned;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public boolean getIsAdmin() {
        return mIsAdmin;
    }

    public void setIsAdmin(boolean IsAdmin) {
        mIsAdmin = IsAdmin;
    }

    public boolean isMisBanned() {
        return misBanned;
    }

    public void setMisBanned(boolean isBanned) {
        misBanned = isBanned;
    }

}
