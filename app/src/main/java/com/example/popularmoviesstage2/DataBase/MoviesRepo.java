package com.example.popularmoviesstage2.DataBase;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class MoviesRepo {
    private MovieDao mMovieDao;
    private LiveData<List<FavoriteMovie>> mAllMovies;

    public MoviesRepo(Application application){
        MovieDatabase mDb = MovieDatabase.getInstance(application);
        mMovieDao = mDb.movieDao();
        mAllMovies = mMovieDao.loadAllMovies();
    }

    public LiveData<List<FavoriteMovie>> getAllMovies(){
        return mAllMovies;
    }

    public void insert(FavoriteMovie favoriteMovie){
        new insertAsyncTask(mMovieDao).execute(favoriteMovie);
    }

    public void delete(FavoriteMovie favoriteMovie){
        new deleteAsyncTask(mMovieDao).execute(favoriteMovie);
    }

    public FavoriteMovie checkMovie(int id){
        return mMovieDao.loadMovieById(id);
    }


    private class insertAsyncTask extends AsyncTask<FavoriteMovie,Void,Void> {
        private MovieDao mMovieDao;

        public insertAsyncTask(MovieDao mMovieDao) {
            this.mMovieDao = mMovieDao;
        }

        @Override
        protected Void doInBackground(FavoriteMovie... favoriteMovies) {
            mMovieDao.insertMovie(favoriteMovies[0]);
            return null;
        }
    }

    private class deleteAsyncTask extends AsyncTask<FavoriteMovie,Void,Void> {
        private MovieDao mMovieDao;

        public deleteAsyncTask(MovieDao mMovieDao) {
            this.mMovieDao = mMovieDao;
        }

        @Override
        protected Void doInBackground(FavoriteMovie... favoriteMovies) {
            mMovieDao.deleteMovie(favoriteMovies[0]);
            return null;
        }
    }
}
