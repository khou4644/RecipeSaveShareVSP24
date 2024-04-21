package com.example.recipesavesharevsp24.DB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.recipesavesharevsp24.RecipeShareSave;
import com.example.recipesavesharevsp24.User;

@Database(entities = {RecipeShareSave.class, User.class}, version = 1, exportSchema = true)
@TypeConverters({com.example.recipesavesharevsp24.DB.DateTypeConverter.class})
public abstract class AppDataBase extends RoomDatabase {
    public static final String DATABASE_NAME = "RecipeShareSave.db";
    public static final String RECIPESHARESAVE_TABLE = "RECIPESHARESAVE_TABLE";

    public static final String USER_TABLE = "USER_TABLE";

    private static volatile AppDataBase instance;
    private static final Object LOCK = new Object();

    public abstract RecipeShareSaveDAO RecipeShareSaveDAO();

    public static  AppDataBase getInstance(Context context){
        if(instance == null){
            synchronized (LOCK){
                if (instance == null){
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDataBase.class,
                            DATABASE_NAME).build();
                }
            }
        }
        return instance;
    }
}
