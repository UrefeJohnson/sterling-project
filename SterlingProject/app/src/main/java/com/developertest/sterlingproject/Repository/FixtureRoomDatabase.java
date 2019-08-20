package com.developertest.sterlingproject.Repository;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.developertest.sterlingproject.Model.User;

@Database(entities = {User.class}, version = 1)
public abstract class FixtureRoomDatabase extends RoomDatabase {

    public static FixtureRoomDatabase instance;
    public abstract UserDAO userDAO();

    public static synchronized FixtureRoomDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    FixtureRoomDatabase.class, "fixture_database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}
