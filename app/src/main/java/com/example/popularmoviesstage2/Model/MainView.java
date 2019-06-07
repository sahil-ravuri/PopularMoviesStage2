package com.example.popularmoviesstage2.Model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.popularmoviesstage2.DataBase.FavoriteMovie;
import com.example.popularmoviesstage2.DataBase.MoviesRepo;

import java.util.List;

public class MainView extends AndroidViewModel {
    private MoviesRepo mMoviesRepo;
    private LiveData<List<FavoriteMovie>> mAllMovies;
    public MainView(Application application){
        super(application);
        mMoviesRepo = new MoviesRepo(application);
        mAllMovies = mMoviesRepo.getAllMovies();
    }

    public LiveData<List<FavoriteMovie>> getAllMovies(){return mAllMovies;}
    public void insertMovies(FavoriteMovie favoriteMovie)
    {
        mMoviesRepo.insert(favoriteMovie);
    }

    public void deleteMovie(FavoriteMovie favoriteMovie){
        mMoviesRepo.delete(favoriteMovie);
    }

    public FavoriteMovie check(int id){
        return mMoviesRepo.checkMovie(id);
    }
}
