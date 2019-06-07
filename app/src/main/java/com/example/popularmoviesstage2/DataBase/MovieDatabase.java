package com.example.popularmoviesstage2.DataBase;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {FavoriteMovie.class}, version = 3, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "movieslist";
    private static MovieDatabase sInstance;

    public abstract MovieDao movieDao();

    public static MovieDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (MovieDatabase.class) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        MovieDatabase.class, MovieDatabase.DATABASE_NAME)
                        .allowMainThreadQueries()
                        .build();
            }
        }
        return sInstance;
    }
}
