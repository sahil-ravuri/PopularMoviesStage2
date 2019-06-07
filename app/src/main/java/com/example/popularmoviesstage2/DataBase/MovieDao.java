package com.example.popularmoviesstage2.DataBase;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@android.arch.persistence.room.Dao
public interface MovieDao {
    @Query("SELECT * FROM FavMovies ORDER BY id")
    LiveData<List<FavoriteMovie>> loadAllMovies();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovie(FavoriteMovie favMovie);


    @Delete
    void deleteMovie(FavoriteMovie favMovie);

    @Query("SELECT * FROM FavMovies WHERE id = :id")
    FavoriteMovie loadMovieById(int id);
}
